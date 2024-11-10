package de.explore.grabby.booking.model.entity;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Game.class, name = "game"),
        @JsonSubTypes.Type(value = Console.class, name = "console"),
        @JsonSubTypes.Type(value = ConsoleAccessory.class, name = "accessory")
})
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

    @Column(insertable = false, updatable = false)
    private String type;

    @Column(name = "isArchived")
    private Boolean isArchived = false;

    @Column(name = "addedOn")
    private LocalDate addedOn = LocalDate.now();

    public BookingEntity() {
    }

    public BookingEntity(String name, String description, String type) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.isArchived = false;
        this.addedOn = LocalDate.now();
    }

    public int getId() {
        return id;
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

    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean archived) {
        isArchived = archived;
    }

    public LocalDate getAddedOn() {
        return addedOn;
    }
}
