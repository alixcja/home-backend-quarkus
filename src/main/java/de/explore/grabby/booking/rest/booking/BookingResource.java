package de.explore.grabby.booking.rest.booking;

import de.explore.grabby.booking.model.booking.Booking;
import de.explore.grabby.booking.repository.BookingRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import java.time.LocalDate;
import java.util.List;

@Path("/bookings")
public class BookingResource {

    @Inject
    BookingRepository bookingRepository;

    @Path("/{id}")
    @GET
    public Booking getByID(@PathParam("id") long id) {
        return bookingRepository.findById(id);
    }

    // TODO - Implement user logic
    @Path("/all")
    @GET
    public List<Booking> getByUserID() {
        return bookingRepository.listAll();
    }

    @Path("/create")
    @POST
    public void createBookings(List<Booking> newBookings) {
        bookingRepository.create(newBookings);
    }

    @Path("/cancel/{id}")
    @PUT
    public void cancelById(@PathParam("id") long id) {
        bookingRepository.cancelById(id);
    }

    @Path("/return/{id}")
    @PUT
    public void returnById(@PathParam("id") long id) {
        bookingRepository.returnById(id);
    }

    @Path("/extend/{id}")
    @PUT
    public void extendById(@PathParam("id") long id, int requestedDays) {
        bookingRepository.extendById(id, requestedDays);
    }

    // return
    // update / extend -> only if it is not already book by another user
    // book
}
