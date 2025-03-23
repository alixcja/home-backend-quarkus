package de.explore.grabby.booking.model.news;

import de.explore.grabby.booking.model.Booking;
import de.explore.grabby.booking.model.entity.BookingEntity;

public class News {
  private NewsType type;

  private BookingEntity entity;

  private Booking booking;

  public News(NewsType type) {
    this.type = type;
  }

  public News(NewsType type, BookingEntity entity) {
    this.type = type;
    this.entity = entity;
  }

  public News(NewsType type, Booking booking) {
    this.type = type;
    this.booking = booking;
  }

  public NewsType getType() {
    return type;
  }

  public void setType(NewsType type) {
    this.type = type;
  }

  public BookingEntity getEntity() {
    return entity;
  }

  public void setEntity(BookingEntity entity) {
    this.entity = entity;
  }

  public Booking getBooking() {
    return booking;
  }

  public void setBooking(Booking booking) {
    this.booking = booking;
  }
}
