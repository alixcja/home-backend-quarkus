package de.explore.grabby.lunch.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class OpeningHours extends PanacheEntity implements Serializable {

  private Weekday weekday;

  @ManyToOne
  @JoinColumn(name = "shop_id")
  private Shop shop;

  @Column(name = "from_date")
  private LocalTime from;

  @Column(name = "to_date")
  private LocalTime to;

  @Column(name = "last_updated")
  @UpdateTimestamp
  private LocalDate lastUpdated;


  public OpeningHours() {
  }

  public OpeningHours(Weekday weekday, Shop shop, LocalTime from, LocalTime to) {
    this.weekday = weekday;
    this.from = from;
    this.to = to;
  }

  public Weekday getWeekday() {
    return weekday;
  }

  public void setWeekday(Weekday weekday) {
    this.weekday = weekday;
  }

  public Shop getShop() {
    return shop;
  }

  public void setShop(Shop shop) {
    this.shop = shop;
  }

  public LocalTime getFrom() {
    return from;
  }

  public void setFrom(LocalTime from) {
    this.from = from;
  }

  public LocalTime getTo() {
    return to;
  }

  public void setTo(LocalTime to) {
    this.to = to;
  }

  public LocalDate getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(LocalDate lastUpdated) {
    this.lastUpdated = lastUpdated;
  }
}
