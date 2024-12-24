package de.explore.grabby.booking.rest.booking;

import de.explore.grabby.booking.model.booking.Booking;
import de.explore.grabby.booking.repository.BookingRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/bookings")
public class BookingResource {

  private final BookingRepository bookingRepository;
  private final JsonWebToken jwt;

  @Inject
  public BookingResource(BookingRepository bookingRepository, JsonWebToken jwt) {
    this.bookingRepository = bookingRepository;
    this.jwt = jwt;
  }

  @Path("/{id}")
  @GET
  public Booking getBookingByID(@PathParam("id") long id) {
    return bookingRepository.findById(id);
  }

  @GET
  public List<Booking> getAllBookings() {
    return bookingRepository.listAllCurrentAndInFutureBookings(jwt.getSubject());
  }

  @Path("/overdue")
  @GET
  public List<Booking> getAllOverdueBookings() {
    return bookingRepository.listAllOverdueBookings(jwt.getSubject());
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
    return bookingRepository.extendById(id, requestedDays);
  }
}
