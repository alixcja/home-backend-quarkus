package de.explore.grabby.model.booking;

import de.explore.grabby.model.entity.Entity;

import java.time.LocalDateTime;

//@Entity
public class Booking {
    private int bookingId;
    private String userId;
    private Entity bookedEntity;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean wasCancelled;
    private Boolean wasReturned;

    public Booking() {
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

    public Boolean getWasReturned() {
        return wasReturned;
    }

    public void setWasReturned(Boolean wasReturned) {
        this.wasReturned = wasReturned;
    }
}
