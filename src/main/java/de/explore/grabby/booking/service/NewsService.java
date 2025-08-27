package de.explore.grabby.booking.service;

import de.explore.grabby.booking.model.news.News;
import de.explore.grabby.booking.model.news.NewsType;
import de.explore.grabby.booking.repository.BookingRepository;
import de.explore.grabby.booking.repository.entity.BookingEntityRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class NewsService {

  private static final Logger LOG = LoggerFactory.getLogger(NewsService.class);
  @Inject
  BookingRepository bookingRepository;

  @Inject
  BookingEntityRepository bookingEntityRepository;

  public List<News> getAllCurrentNews(String identifier) {
    List<News> allCurrentNews = new ArrayList<>();

    allCurrentNews.addAll(fetchBookingsStartingToday(identifier));
    allCurrentNews.addAll(fetchNewEntities());
    allCurrentNews.addAll(fetchSoonOverdueBookings(identifier));
    allCurrentNews.addAll(fetchOverdueBookings(identifier));

    LOG.info("Found {} news for user with subject {}", allCurrentNews.size(), identifier);
    return allCurrentNews;
  }

  private List<News> fetchNewEntities() {
    return bookingEntityRepository.listAllNewEntities()
            .stream()
            .map(entity
                    -> new News(NewsType.NEWS_NEW_ENTITY, entity))
            .toList();
  }

  private List<News> fetchBookingsStartingToday(String identifier) {
    return bookingRepository.aBookingForUserStartsToday(identifier)
            .stream()
            .map(booking
                    -> new News(NewsType.NEWS_BOOKING_STARTS_TODAY, booking))
            .toList();
  }

  private List<News> fetchSoonOverdueBookings(String identifier) {
    return bookingRepository.findSoonOverdueBookingsOfUser(identifier)
            .stream()
            .map(booking
                    -> new News(NewsType.NEWS_SOON_OVERDUE_BOOKINGS, booking))
            .toList();
  }

  private List<News> fetchOverdueBookings(String identifier) {
    return bookingRepository.listAllOverdueBookings(identifier)
            .stream()
            .map(booking
                    -> new News(NewsType.NEWS_OVERDUE_BOOKINGS, booking))
            .toList();
  }
}