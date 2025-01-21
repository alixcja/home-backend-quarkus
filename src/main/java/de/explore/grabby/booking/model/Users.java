package de.explore.grabby.booking.model;

import jakarta.persistence.*;

@Entity
public class Users {

  // TODO: Change it so subject will be unique and used as id
  @Id
  private String id;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "first_name")
  private String firstName;

  public Users() {
  }

  public Users(String id, String lastName, String firstName) {
    this.id = id;
    this.lastName = lastName;
    this.firstName = firstName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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
