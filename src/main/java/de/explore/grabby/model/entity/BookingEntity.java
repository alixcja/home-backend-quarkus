package de.explore.grabby.model.entity;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
public abstract class BookingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENTITY_SEQ")
    @SequenceGenerator(name = "ENTITY_SEQ", sequenceName = "BOOKING_ENTITY_TABLE_SEQ", allocationSize = 1)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Column(name = "isArchived")
    private Boolean isArchived;

    @Column(name = "addedOn")
    private Date addedOn;

    public BookingEntity() {
    }

    public BookingEntity(String name, String description, String type) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.isArchived = false;
        this.addedOn = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }

    public Date getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(Date addedOn) {
        this.addedOn = addedOn;
    }
}
