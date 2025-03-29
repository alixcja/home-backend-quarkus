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
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.NO_CONTENT;

@Path("/bookings")
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

  @Path("/{id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @APIResponse(responseCode = "200", description = "Got successfully booking by id")
  @APIResponse(responseCode = "404", description = "No booking found for provided id")
  public Response getBookingByID(@PathParam("id") long id) {
    Booking byId = bookingRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
    return Response.ok(byId).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @APIResponse(responseCode = "200", description = "Got successfully bookings")
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
  @APIResponse(responseCode = "201", description = "Successfully booked entities")
  @APIResponse(responseCode = "400", description = "Invalid input")
  public Response createBookings(@NotNull @Valid List<Booking> newBookings) {
    verifyUserDoesNotHaveMoreThanFiveBookings(newBookings.size());
    bookingRepository.create(newBookings, jwt.getSubject());
    return Response.status(CREATED).build();
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

  @Path("/cancel/{id}")
  @PUT
  @APIResponse(responseCode = "204", description = "Successfully cancelled booking")
  @APIResponse(responseCode = "400", description = "Too late to cancel booking")
  @APIResponse(responseCode = "404", description = "No booking found for provided id")
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

  @Path("/extend/{id}")
  @PUT
  @APIResponse(responseCode = "204", description = "Successfully cancelled booking")
  @APIResponse(responseCode = "400", description = "Invalid input")
  @APIResponse(responseCode = "404", description = "No booking found for provided id")
  public Response extendBookingById(@PathParam("id") long id, int requestedDays) {
    ensureBookingExists(id);
    if (requestedDays > MAX_REQUEST_DAYS) {
      throw new BadRequestException("Booking cannot be extended by more then 7 days");
    }
    boolean successfullyExtended = service.extendById(id, requestedDays);

    if (!successfullyExtended) {
      throw new BadRequestException("Booking was already booked");
    }
    return Response.status(NO_CONTENT).build();
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
}
