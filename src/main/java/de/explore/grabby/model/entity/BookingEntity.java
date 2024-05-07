package de.explore.grabby.model.entity;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

import java.util.Date;

public abstract class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENTITY_SEQ")
    @SequenceGenerator(name = "ENTITY_SEQ", sequenceName = "BOOKING_ENTITY_TABLE_SEQ", allocationSize = 1)
    private int id;
    private String name;
    private String description;
    private String type;
    private Boolean isArchived;
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
