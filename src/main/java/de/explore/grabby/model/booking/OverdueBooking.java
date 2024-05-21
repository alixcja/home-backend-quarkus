package de.explore.grabby.model.booking;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

//@Entity
public class OverdueBooking {
    private int overdueBookingId;
    @ManyToOne
    private Booking overdueBooking;
    private LocalDateTime sendLastReminder;

    public OverdueBooking() {
    }

    public int getOverdueBookingId() {
        return overdueBookingId;
    }

    public void setOverdueBookingId(int overdueBookingId) {
        this.overdueBookingId = overdueBookingId;
    }

    public Booking getOverdueBooking() {
        return overdueBooking;
    }

    public void setOverdueBooking(Booking overdueBooking) {
        this.overdueBooking = overdueBooking;
    }

    public LocalDateTime getSendLastReminder() {
        return sendLastReminder;
    }

    public void setSendLastReminder(LocalDateTime sendLastReminder) {
        this.sendLastReminder = sendLastReminder;
    }
}
