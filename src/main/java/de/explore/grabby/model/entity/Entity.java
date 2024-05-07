package de.explore.grabby.model.entity;


import java.util.Date;

//@Entity
public abstract class Entity {
    private int entityID;
    private String name;
    private String description;
    private String type;
    private Boolean isArchived;
    private Date addedOn;

    public Entity() {
    }

    public Entity(String name, String description, String type) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.isArchived = false;
        this.addedOn = new Date();
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
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
}
