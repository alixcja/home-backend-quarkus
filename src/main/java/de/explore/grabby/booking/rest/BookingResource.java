package de.explore.grabby.booking.rest;

import de.explore.grabby.booking.model.Booking;
import de.explore.grabby.booking.repository.BookingRepository;
import de.explore.grabby.booking.service.BookingService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.NO_CONTENT;

@Path("/bookings")
@Tag(name = "Bookings", description = "Operations related to user bookings")
public class BookingResource {

  private static final String OVERDUE_STATUS = "overdue";
  private static final String UPCOMING_STATUS = "upcoming";
  private static final int MAX_REQUEST_DAYS = 7;
  private final BookingRepository bookingRepository;
  private final BookingService service;
  private final JsonWebToken jwt;

  @Inject
  public BookingResource(BookingRepository bookingRepository, BookingService service, JsonWebToken jwt) {
    this.bookingRepository = bookingRepository;
    this.service = service;
    this.jwt = jwt;
  }

  // TODO: Add endpoint to adjust start- and / or enddate of a not active booking?

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Get booking by ID", description = "Retrieves a single booking by its unique ID.")
  @APIResponses({
          @APIResponse(responseCode = "200", description = "Booking retrieved successfully",
                  content = @Content(schema = @Schema(implementation = Booking.class))),
          @APIResponse(responseCode = "404", description = "No booking found for provided id")
  })
  public Response getBookingByID(@PathParam("id") long id) {
    Booking byId = bookingRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
    return Response.ok(byId).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Get bookings", description = "Retrieves bookings, optionally filtered by status (e.g., overdue or upcoming).")
  @Parameter(name = "status", description = "Optional booking status filter: 'overdue' or 'upcoming'")
  @APIResponses({
          @APIResponse(responseCode = "200", description = "Bookings retrieved successfully",
                  content = @Content(schema = @Schema(implementation = Booking[].class)))
  })
  public Response getBookings(@QueryParam("status") String status) {
    List<Booking> bookings = new ArrayList<>();
    if (status != null && status.equals(OVERDUE_STATUS)) {
      bookings = bookingRepository.listAllOverdueBookings(jwt.getSubject());
    } else if (status != null && status.equals(UPCOMING_STATUS)) {
      bookings = bookingRepository.listAllCurrentAndInFutureBookings(jwt.getSubject());
    } else {
      bookings = bookingRepository.listAllBookings(jwt.getSubject());
    }
    return Response.ok(bookings).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(summary = "Create bookings", description = "Creates one or more new bookings for the user.")
  @APIResponses({
          @APIResponse(responseCode = "201", description = "Bookings created successfully"),
          @APIResponse(responseCode = "400", description = "Invalid input or booking limit reached")
  })
  public Response createBookings(@NotNull @Valid List<Booking> newBookings) {
    verifyUserDoesNotHaveMoreThanFiveBookings(newBookings.size());
    bookingRepository.create(newBookings, jwt.getSubject());
    return Response.status(CREATED).build();
  }

  @PUT
  @Path("/cancel/{id}")
  @Operation(summary = "Cancel booking", description = "Cancels a booking if it hasn’t started yet.")
  @APIResponses({
          @APIResponse(responseCode = "204", description = "Booking cancelled successfully"),
          @APIResponse(responseCode = "400", description = "Booking already active"),
          @APIResponse(responseCode = "404", description = "Booking not found")
  })
  public Response cancelBookingById(@PathParam("id") long id) {
    ensureBookingExists(id);
    ensureBookingIsNotActive(id);
    bookingRepository.cancelById(id);
    return Response.status(NO_CONTENT).build();
  }

  @Path("/return/{id}")
  @PUT
  @APIResponse(responseCode = "204", description = "Successfully cancelled booking")
  @APIResponse(responseCode = "400", description = "Booking is not active")
  @APIResponse(responseCode = "404", description = "No booking found for provided id")
  public Response returnBookingById(@PathParam("id") long id) {
    ensureBookingExists(id);
    ensureBookingIsExpired(id);
    bookingRepository.returnById(id);
    return Response.status(NO_CONTENT).build();
  }

  @PUT
  @Path("/extend/{id}")
  @Operation(summary = "Extend booking", description = "Extends a booking by a given number of days (up to 7).")
  @Parameter(name = "requestedDays", description = "Number of days to extend (max 7)", required = true)
  @APIResponses({
          @APIResponse(responseCode = "204", description = "Booking extended successfully"),
          @APIResponse(responseCode = "400", description = "Invalid extension or booking already booked"),
          @APIResponse(responseCode = "404", description = "Booking not found")
  })
  public Response extendBookingById(@PathParam("id") long id, int requestedDays) {
    ensureBookingExists(id);
    ensureRequestedDaysAreNotExceeded(requestedDays);

    boolean successfullyExtended = service.extendById(id, requestedDays);

    if (!successfullyExtended) {
      throw new BadRequestException("Booking was already booked");
    }
    return Response.status(NO_CONTENT).build();
  }

  private void ensureRequestedDaysAreNotExceeded(int requestedDays) {
    if (requestedDays > MAX_REQUEST_DAYS) {
      throw new BadRequestException("Booking cannot be extended by more then 7 days");
    }
  }

  private void ensureBookingExists(Long id) {
    bookingRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
  }

  private void ensureBookingIsExpired(long id) {
    Booking bookingToReturn = bookingRepository.findById(id);
    boolean isStartDateBeforeNow = bookingToReturn.getStartDate().isBefore(LocalDate.now());
    if (!isStartDateBeforeNow) {
      throw new BadRequestException("Booking is not active");
    }
  }

  private void ensureBookingIsNotActive(long id) {
    Booking bookingToCancel = bookingRepository.findById(id);
    boolean isStartDateAfterNow = bookingToCancel.getStartDate().isAfter(LocalDate.now());

    if (!isStartDateAfterNow) {
      throw new BadRequestException("Booking is already active");
    }
  }

  private void verifyUserDoesNotHaveMoreThanFiveBookings(int newBookingsSize) {
    long currentBookingsSize = bookingRepository.listByUser(jwt.getSubject());
    if (currentBookingsSize > 5) {
      throw new BadRequestException("Booking limit has been reached");
    }
    if (currentBookingsSize + newBookingsSize > 5) {
      throw new BadRequestException("Booking limit would be reached");
    }
  }
}
