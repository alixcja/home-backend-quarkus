package de.explore.grabby.booking.model.news;

import de.explore.grabby.booking.model.entity.BookingEntity;

public class News {
  private NewsType type;

  private BookingEntity entity;

  public News(NewsType type) {
    this.type = type;
  }

  public News(NewsType type, BookingEntity entity) {
    this.type = type;
    this.entity = entity;
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
}
