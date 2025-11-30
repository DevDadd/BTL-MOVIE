package com.mycompany.movieapp.models;

import com.mycompany.movieapp.services.MovieService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Customer extends User {
    private List<Booking> bookingHistory;
    private int loyaltyPoints;

    public Customer(int userId, String username, String password, String email,
                    String phoneNumber, String address) {
        super(userId, username, password, email, phoneNumber, address);
        this.bookingHistory = new ArrayList<>();
        this.loyaltyPoints = 0;
    }

    @Override
    public String getDashboardInfo() {
        return String.format("CUSTOMER DASHBOARD\nTổng booking: %d\nĐiểm tích lũy: %d",
                bookingHistory.size(), loyaltyPoints);
    }

    public List<Movie> searchMovie(String keyword, List<Movie> allMovies) {
        return MovieService.searchMovies(keyword, allMovies);
    }

    public List<Schedule> viewSchedules(int movieId, List<Schedule> allSchedules) {
        return allSchedules.stream()
                .filter(s -> s.getMovie().getMovieId() == movieId)
                .filter(Schedule::isAvailableForBooking)
                .collect(Collectors.toList());
    }


    public List<Booking> viewBookingHistory() {
        return new ArrayList<>(bookingHistory);
    }

    public void addBooking(Booking booking) {
        if (booking != null) {
            bookingHistory.add(booking);
            loyaltyPoints += (int)(booking.getTotalPrice() / 10000);
        }
    }

    public Booking viewBooking(int bookingId) {
        return bookingHistory.stream()
                .filter(b -> b.getBookingId() == bookingId)
                .findFirst()
                .orElse(null);
    }


    public boolean makePayment(int bookingId, String paymentMethod, double amount) {
        Booking booking = viewBooking(bookingId);
        if (booking == null) {
            System.out.println("Không tìm thấy booking!");
            return false;
        }

        if (booking.getTotalPrice() != amount) {
            System.out.println("Số tiền không khớp!");
            return false;
        }

        Payment payment = new Payment(booking, paymentMethod, amount);
        return payment.processPayment();
    }

    public boolean canBook() {
        return isActive() && status != com.mycompany.movieapp.enums.AccountStatus.BLACKLIST;
    }

    public int getLoyaltyDiscount() {
        int discount = loyaltyPoints / 100;
        return Math.min(discount, 20);
    }


    public List<Booking> getBookingHistory() { return bookingHistory; }
    public void setBookingHistory(List<Booking> bookingHistory) { this.bookingHistory = bookingHistory; }

    public int getLoyaltyPoints() { return loyaltyPoints; }
    public void setLoyaltyPoints(int loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }

    @Override
    public String toString() {
        return username + " - Điểm: " + loyaltyPoints;
    }
}
