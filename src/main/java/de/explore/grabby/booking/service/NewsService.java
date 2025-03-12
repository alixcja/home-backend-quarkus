package de.explore.grabby.booking.service;

import de.explore.grabby.booking.model.news.News;
import de.explore.grabby.booking.model.news.NewsType;
import de.explore.grabby.booking.repository.BookingRepository;
import de.explore.grabby.booking.repository.entity.BookingEntityRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class NewsService {

  @Inject
  BookingRepository bookingRepository;

  @Inject
  BookingEntityRepository bookingEntityRepository;

  public List<News> getAllCurrentNews(String identifier) {
    List<News> allCurrentNews = new ArrayList<>();

    allCurrentNews.addAll(fetchBookingsStartingToday(identifier));
    allCurrentNews.addAll(fetchNewEntities());

    if (hasSoonOverdueBookings(identifier)) {
      allCurrentNews.add(new News(NewsType.NEWS_SOON_OVERDUE_BOOKINGS));
    }

    if (hasOverdueBookings(identifier)) {
      allCurrentNews.add(new News(NewsType.NEWS_OVERDUE_BOOKINGS));
    }

    return allCurrentNews;
  }

  private List<News> fetchNewEntities() {
    return bookingEntityRepository.newEntityWasAdded()
            .stream()
            .map(entity -> new News(NewsType.NEWS_NEW_ENTITY, entity))
            .toList();
  }

  private List<News> fetchBookingsStartingToday(String identifier) {
    return bookingRepository.aBookingForUserStartsToday(identifier)
            .stream()
            .map(booking -> new News(NewsType.NEWS_BOOKING_STARTS_TODAY, booking.getBookingEntity()))
            .toList();
  }

  private boolean hasSoonOverdueBookings(String identifier) {
    return !bookingRepository.findSoonOverdueBookingsOfUser(identifier).isEmpty();
  }

  private boolean hasOverdueBookings(String identifier) {
    return !bookingRepository.listAllOverdueBookings(identifier).isEmpty();
  }
}