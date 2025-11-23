package com.mycompany.movieapp.services;

import com.mycompany.movieapp.models.*;
import com.mycompany.movieapp.exceptions.*;
import java.util.*;

public class BookingService {
    private static List<Booking> allBookings = new ArrayList<>();
    private static int bookingIdCounter = 1000;

    /**
     * Create a new booking
     */
    public static Booking createBooking(Customer customer, Schedule schedule, List<ShowSeat> selectedSeats)
            throws InvalidBookingException, SeatNotAvailableException {

        // Validate customer
        if (customer == null || !customer.canBook()) {
            throw new InvalidBookingException("Khách hàng không hợp lệ hoặc bị khóa tài khoản!");
        }

        // Validate schedule
        if (schedule == null || !schedule.isAvailableForBooking()) {
            throw new InvalidBookingException("Suất chiếu không khả dụng!");
        }

        // Validate seats
        if (selectedSeats == null || selectedSeats.isEmpty()) {
            throw new InvalidBookingException("Vui lòng chọn ít nhất một ghế!");
        }

        // Giới hạn số ghế tối đa
        if (selectedSeats.size() > 10) {
            throw new InvalidBookingException("Chỉ được đặt tối đa 10 ghế mỗi lần!");
        }

        // Create booking
        Booking booking = new Booking();
        booking.setBookingId(generateBookingId());
        booking.setCustomer(customer);
        booking.setSchedule(schedule);

        // Add seats
        for (ShowSeat seat : selectedSeats) {
            booking.addSeat(seat);
        }
        
        allBookings.add(booking);
        return booking;
    }

    private static int generateBookingId() {
        return bookingIdCounter++;
    }

    /**
     * Confirm and pay for a booking
     */
    public static boolean confirmAndPayBooking(Booking booking, String paymentMethod) {
        try {
            // Confirm booking
            if (!booking.confirmBooking()) {
                return false;
            }

            // Process payment using PaymentService
            return PaymentService.processPayment(booking, paymentMethod);

        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get booking by ID for a customer
     */
    public static Booking getBookingById(Customer customer, int bookingId) {
        if (customer == null) {
            return null;
        }
        return customer.getBookingHistory().stream()
                .filter(b -> b.getBookingId() == bookingId)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Get all bookings for a customer
     */
    public static List<Booking> getBookingHistory(Customer customer) {
        if (customer == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(customer.getBookingHistory());
    }
    
    /**
     * Cancel a booking
     */
    public static boolean cancelBooking(Booking booking) {
        if (booking == null) {
            return false;
        }
        
        if (!booking.isCancelable()) {
            System.out.println("Không thể hủy booking này!");
            return false;
        }
        
        boolean canceled = booking.cancelBooking();
        if (canceled && booking.getPayment() != null) {
            PaymentService.refundPayment(booking.getPayment());
        }
        
        return canceled;
    }
    
    /**
     * Get all bookings
     */
    public static List<Booking> getAllBookings() {
        return new ArrayList<>(allBookings);
    }
    
    /**
     * Get bookings by schedule
     */
    public static List<Booking> getBookingsBySchedule(Schedule schedule) {
        if (schedule == null) {
            return new ArrayList<>();
        }
        return allBookings.stream()
                .filter(b -> b.getSchedule().equals(schedule))
                .collect(java.util.stream.Collectors.toList());
    }
}