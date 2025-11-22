package com.mycompany.movieapp.services;

import com.mycompany.movieapp.models.*;
import com.mycompany.movieapp.exceptions.*;
import java.util.*;

public class BookingService {

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

        return booking;
    }

    private static int bookingIdCounter = 1000;

    private static int generateBookingId() {
        return bookingIdCounter++;
    }

    public static boolean confirmAndPayBooking(Booking booking, String paymentMethod) {
        try {
            // Confirm booking
            if (!booking.confirmBooking()) {
                return false;
            }

            // Process payment
            Payment payment = new Payment();
            payment.setBooking(booking);
            payment.setMethod(paymentMethod);
            payment.setAmount(booking.getTotalPrice());

            if (payment.processPayment()) {
                booking.setPayment(payment);
                return true;
            }

            return false;

        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        }
    }
}