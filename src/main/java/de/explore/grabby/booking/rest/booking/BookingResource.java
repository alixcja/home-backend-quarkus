package de.explore.grabby.booking.rest.booking;

import de.explore.grabby.booking.model.booking.Booking;
import de.explore.grabby.booking.repository.BookingRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import java.util.List;

@Path("/bookings")
public class BookingResource {

  private final BookingRepository bookingRepository;

  @Inject
  public BookingResource(BookingRepository bookingRepository) {
    this.bookingRepository = bookingRepository;
  }

  @Path("/{id}")
  @GET
  public Booking getBookingByID(@PathParam("id") long id) {
    return bookingRepository.findById(id);
  }

  // TODO - Implement user logic
  @GET
  public List<Booking> getAllBookings() {
    return bookingRepository.listAll();
  }

  @Path("/new")
  @POST
  public void createBookings(List<Booking> newBookings) {
    bookingRepository.create(newBookings);
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
