package de.explore.grabby.booking.model;

import jakarta.persistence.*;

@Entity
public class Users {

  @Id
  private String identifier;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "first_name")
  private String firstName;

  public Users() {
  }

  public Users(String identifier, String lastName, String firstName) {
    this.identifier = identifier;
    this.lastName = lastName;
    this.firstName = firstName;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
}
