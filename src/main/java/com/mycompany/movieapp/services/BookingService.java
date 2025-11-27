package com.mycompany.movieapp.services;

import com.mycompany.movieapp.models.*;
import com.mycompany.movieapp.exceptions.*;
import java.util.*;

public class BookingService {
    private static List<Booking> allBookings = new ArrayList<>();
    private static int bookingIdCounter = 1000;

    public static Booking createBooking(Customer customer, Schedule schedule, List<ShowSeat> selectedSeats)
            throws InvalidBookingException, SeatNotAvailableException {

        if (customer == null || !customer.canBook()) {
            throw new InvalidBookingException("Khách hàng không hợp lệ hoặc bị khóa tài khoản!");
        }

        if (schedule == null || !schedule.isAvailableForBooking()) {
            throw new InvalidBookingException("Suất chiếu không khả dụng!");
        }

        if (selectedSeats == null || selectedSeats.isEmpty()) {
            throw new InvalidBookingException("Vui lòng chọn ít nhất một ghế!");
        }

        if (selectedSeats.size() > 10) {
            throw new InvalidBookingException("Chỉ được đặt tối đa 10 ghế mỗi lần!");
        }

        Booking booking = new Booking();
        booking.setBookingId(generateBookingId());
        booking.setCustomer(customer);
        booking.setSchedule(schedule);

        for (ShowSeat seat : selectedSeats) {
            booking.addSeat(seat);
        }
        
        allBookings.add(booking);
        return booking;
    }

    private static int generateBookingId() {
        return bookingIdCounter++;
    }

    public static boolean confirmAndPayBooking(Booking booking, String paymentMethod) {
        try {
            if (!booking.confirmBooking()) {
                return false;
            }

            return PaymentService.processPayment(booking, paymentMethod);

        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        }
    }
    
    public static Booking getBookingById(Customer customer, int bookingId) {
        if (customer == null) {
            return null;
        }
        return customer.getBookingHistory().stream()
                .filter(b -> b.getBookingId() == bookingId)
                .findFirst()
                .orElse(null);
    }
    
    public static List<Booking> getBookingHistory(Customer customer) {
        if (customer == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(customer.getBookingHistory());
    }
    
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
    
    public static List<Booking> getAllBookings() {
        return new ArrayList<>(allBookings);
    }
    
    public static List<Booking> getBookingsBySchedule(Schedule schedule) {
        if (schedule == null) {
            return new ArrayList<>();
        }
        return allBookings.stream()
                .filter(b -> b.getSchedule().equals(schedule))
                .collect(java.util.stream.Collectors.toList());
    }
}