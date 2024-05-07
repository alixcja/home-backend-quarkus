package de.explore.grabby.model.booking;

import de.explore.grabby.model.entity.Entity;

import java.time.LocalDateTime;
import java.util.Optional;

//@Entity
public class Booking {
    private int bookingId;
    private String userId;
    private Entity bookedEntity;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean wasCancelled;
    private Optional<Boolean> wasReturned;

    public Booking() {
    }

    public Booking(String userId, Entity bookedEntity, LocalDateTime startDate, LocalDateTime endDate) {
        this.userId = userId;
        this.bookedEntity = bookedEntity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.wasCancelled = false;
        this.wasReturned = Optional.of(false);
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Entity getBookedEntity() {
        return bookedEntity;
    }

    public void setBookedEntity(Entity bookedEntity) {
        this.bookedEntity = bookedEntity;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean getWasCancelled() {
        return wasCancelled;
    }

    public void setWasCancelled(Boolean wasCancelled) {
        this.wasCancelled = wasCancelled;
    }

    public Optional<Boolean> getWasReturned() {
        return wasReturned;
    }

    public void setWasReturned(Boolean wasReturned) {
        this.wasReturned = Optional.ofNullable(wasReturned);
    }
}
