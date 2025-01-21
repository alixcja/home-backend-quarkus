package de.explore.grabby.booking.model;

import de.explore.grabby.booking.model.entity.BookingEntity;
import jakarta.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAVORITE_SEQ")
    @SequenceGenerator(name = "FAVORITE_SEQ", sequenceName = "FAVORITE_TABLE_SEQ", allocationSize = 1)
    @Column(name = "id")
    private int favoriteId;

    @Column(name = "user_id")
    private String userId;

    @JoinColumn(name = "bookingEntity_id")
    @ManyToOne
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
