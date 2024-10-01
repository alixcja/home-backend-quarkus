package de.explore.grabby.booking.rest.entity;

import de.explore.grabby.booking.model.entity.BookingEntity;
import de.explore.grabby.booking.repository.entity.BookingEntityRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/entities")
public class BookingEntityResource {

    @Inject
    BookingEntityRepository bookingEntityRepository;

    @Path("/{id}")
    @GET
    public BookingEntity findById(@PathParam("id") long id) {
        return bookingEntityRepository.findById(id);
    }

    @Path("/archive/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void archiveEntity(@PathParam("id") long id) {
        bookingEntityRepository.archiveEntityById(id);
    }

    @Path("/all")
    @GET
    public List<BookingEntity> getAllEntities() {
        return bookingEntityRepository.listAll();
    }

}
