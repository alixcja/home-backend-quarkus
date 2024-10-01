package de.explore.grabby.booking.model.booking;

import de.explore.grabby.booking.model.entity.BookingEntity;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Booking implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOKING_SEQ")
    @SequenceGenerator(name = "BOOKING_SEQ", sequenceName = "BOOKING_TABLE_SEQ", allocationSize = 1)
    @Column(name = "id")
    private int bookingId;
    @Column(name = "user_id")
    private String userId;
    @JoinColumn(name = "bookingEntity_id")
    @ManyToOne
    private BookingEntity bookedBookingEntity;
    // TODO - Change these to LocalDAte
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime bookingDate;
    private Boolean isCancelled;
    private Boolean isReturned;
    // field to count how often booking was extended

    public Booking() {
    }

    public Booking(String userId, BookingEntity bookedBookingEntity, LocalDateTime startDate, LocalDateTime endDate) {
        this.userId = userId;
        this.bookedBookingEntity = bookedBookingEntity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCancelled = false;
        this.isReturned = false;
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

    public Boolean getIsReturned() {
        return isReturned;
    }

    public void setIsReturned(Boolean isReturned) {
        this.isReturned = isReturned;
    }
}
