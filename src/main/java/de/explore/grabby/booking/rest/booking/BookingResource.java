package de.explore.grabby.booking.rest.booking;

import de.explore.grabby.booking.model.booking.Booking;
import de.explore.grabby.booking.repository.BookingRepository;
import de.explore.grabby.booking.service.BookingService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

// TODO: Add responses and document them
@Path("/bookings")
public class BookingResource {

  public static final String OVERDUE_STATUS = "overdue";
  private final BookingRepository bookingRepository;
  private final BookingService service;
  private final JsonWebToken jwt;

  @Inject
  public BookingResource(BookingRepository bookingRepository, BookingService service, JsonWebToken jwt) {
    this.bookingRepository = bookingRepository;
    this.service = service;
    this.jwt = jwt;
  }

  @Path("/{id}")
  @GET
  public Booking getBookingByID(@PathParam("id") long id) {
    return bookingRepository.findById(id);
  }

  @GET
  public List<Booking> getAllBookings(@QueryParam("status") String status) {
    if (status != null && status.equals(OVERDUE_STATUS)) {
      return bookingRepository.listAllOverdueBookings(jwt.getSubject());
    }
    return bookingRepository.listAllCurrentAndInFutureBookings(jwt.getSubject());
  }

  @Path("/all")
  @GET
  public List<Booking> getAllCurrentAndInFutureBookings() {
    return bookingRepository.listAllBookings(jwt.getSubject());
  }

  @Path("/new")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public void createBookings(List<Booking> newBookings) {
    bookingRepository.create(newBookings, jwt.getSubject());
  }

  @Path("/cancel/{id}")
  @PUT
  public void cancelBookingById(@PathParam("id") long id) {
    bookingRepository.cancelById(id);
  }

  @Path("/return/{id}")
  @PUT
  public void returnBookingById(@PathParam("id") long id) {
    bookingRepository.returnById(id);
  }

  @Path("/extend/{id}")
  @PUT
  public Boolean extendBookingById(@PathParam("id") long id, int requestedDays) {
    return service.extendById(id, requestedDays);
  }
}
