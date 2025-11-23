package com.mycompany.movieapp.services;

import com.mycompany.movieapp.models.*;
import java.util.List;
import java.util.ArrayList;

public class CustomerService {
    
    /**
     * Get booking history for a customer
     */
    public static List<Booking> getBookingHistory(Customer customer) {
        if (customer == null) {
            return new ArrayList<>();
        }
        return BookingService.getBookingHistory(customer);
    }
    
    /**
     * Get a specific booking by ID for a customer
     */
    public static Booking getBookingById(Customer customer, int bookingId) {
        if (customer == null) {
            return null;
        }
        return BookingService.getBookingById(customer, bookingId);
    }
    
    /**
     * Add booking to customer history and update loyalty points
     */
    public static void addBookingToHistory(Customer customer, Booking booking) {
        if (customer == null || booking == null) {
            return;
        }
        
        customer.addBooking(booking);
    }
    
    /**
     * Calculate loyalty points from booking total price
     */
    public static int calculateLoyaltyPoints(double totalPrice) {
        return (int)(totalPrice / 10000);
    }
    
    /**
     * Get loyalty discount for a customer
     */
    public static int getLoyaltyDiscount(Customer customer) {
        if (customer == null) {
            return 0;
        }
        return customer.getLoyaltyDiscount();
    }
    
    /**
     * Check if customer can book
     */
    public static boolean canBook(Customer customer) {
        if (customer == null) {
            return false;
        }
        return customer.canBook();
    }
    
    /**
     * Get customer loyalty points
     */
    public static int getLoyaltyPoints(Customer customer) {
        if (customer == null) {
            return 0;
        }
        return customer.getLoyaltyPoints();
    }
}

