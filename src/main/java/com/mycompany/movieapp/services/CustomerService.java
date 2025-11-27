package com.mycompany.movieapp.services;

import com.mycompany.movieapp.models.*;
import java.util.List;
import java.util.ArrayList;

public class CustomerService {
    
    public static List<Booking> getBookingHistory(Customer customer) {
        if (customer == null) {
            return new ArrayList<>();
        }
        return BookingService.getBookingHistory(customer);
    }
    
    public static Booking getBookingById(Customer customer, int bookingId) {
        if (customer == null) {
            return null;
        }
        return BookingService.getBookingById(customer, bookingId);
    }
    
    public static void addBookingToHistory(Customer customer, Booking booking) {
        if (customer == null || booking == null) {
            return;
        }
        
        customer.addBooking(booking);
    }
    
    public static int calculateLoyaltyPoints(double totalPrice) {
        return (int)(totalPrice / 10000);
    }
    
    public static int getLoyaltyDiscount(Customer customer) {
        if (customer == null) {
            return 0;
        }
        return customer.getLoyaltyDiscount();
    }
    
    public static boolean canBook(Customer customer) {
        if (customer == null) {
            return false;
        }
        return customer.canBook();
    }
    
    public static int getLoyaltyPoints(Customer customer) {
        if (customer == null) {
            return 0;
        }
        return customer.getLoyaltyPoints();
    }
}

