package de.explore.grabby.model;

import de.explore.grabby.model.entity.BookingEntity;

//@Entity
public class Favorite {
    private int favoriteId;
    private String userId;
    private BookingEntity favorite;

    public Favorite() {
    }

    public Favorite(String userId, BookingEntity favorite) {
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

    public BookingEntity getFavorite() {
        return favorite;
    }

    public void setFavorite(BookingEntity favorite) {
        this.favorite = favorite;
    }
}
