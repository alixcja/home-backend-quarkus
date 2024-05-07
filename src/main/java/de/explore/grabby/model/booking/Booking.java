package de.explore.grabby.model.booking;

import de.explore.grabby.model.entity.BookingEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOKING_SEQ")
    @SequenceGenerator(name = "BOOKING_SEQ", sequenceName = "BOOKING_TABLE_SEQ", allocationSize = 1)
    private int bookingId;
    private String userId;
    private BookingEntity bookedBookingEntity;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime bookingDate;
    private Boolean isCancelled;
    private Optional<Boolean> isReturned;

    public Booking() {
    }

    public Booking(String userId, BookingEntity bookedBookingEntity, LocalDateTime startDate, LocalDateTime endDate) {
        this.userId = userId;
        this.bookedBookingEntity = bookedBookingEntity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCancelled = false;
        this.isReturned = Optional.of(false);
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

    public BookingEntity getBookedBookingEntity() {
        return bookedBookingEntity;
    }

    public void setBookedBookingEntity(BookingEntity bookedBookingEntity) {
        this.bookedBookingEntity = bookedBookingEntity;
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

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Optional<Boolean> getIsReturned() {
        return isReturned;
    }

    public void setIsReturned(Optional<Boolean> isReturned) {
        this.isReturned = isReturned;
    }
}
