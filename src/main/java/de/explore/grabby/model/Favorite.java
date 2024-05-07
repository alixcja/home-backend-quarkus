package de.explore.grabby.model;

import de.explore.grabby.model.entity.Entity;

//@Entity
public class Favorite {
    private int favoriteId;
    private String userId;
    private Entity favorite;

    public Favorite() {
    }

    public Favorite(String userId, Entity favorite) {
        this.userId = userId;
        this.favorite = favorite;
    }

    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Entity getFavorite() {
        return favorite;
    }

    public void setFavorite(Entity favorite) {
        this.favorite = favorite;
    }
}
