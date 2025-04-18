package de.explore.grabby.lunch.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

import java.io.Serializable;

@Entity
public class Address extends PanacheEntity implements Serializable {

  @OneToOne(mappedBy = "address")
  private Shop shop;

  @Column(name = "street_number")
  private String streetNumber;

  @Column(name = "street_name")
  private String streetName;

  @Column(name = "postal_code")
  private int postalCode;

  private String city;

  public Address() {
  }

  public String getStreetNumber() {
    return streetNumber;
  }

  public void setStreetNumber(String streetNumber) {
    this.streetNumber = streetNumber;
  }

  public String getStreetName() {
    return streetName;
  }

  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }

  public int getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(int postalCode) {
    this.postalCode = postalCode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }
}
