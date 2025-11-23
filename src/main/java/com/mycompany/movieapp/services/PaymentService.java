package com.mycompany.movieapp.services;

import com.mycompany.movieapp.models.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentService {
    private static List<Payment> allPayments = new ArrayList<>();
    private static int paymentIdCounter = 1;
    
    /**
     * Process payment for a booking
     * @param booking The booking to pay for
     * @param paymentMethod The payment method (MOMO, VISA, MASTERCARD, CASH)
     * @return true if payment successful, false otherwise
     */
    public static boolean processPayment(Booking booking, String paymentMethod) {
        if (booking == null) {
            System.out.println("Booking không hợp lệ!");
            return false;
        }
        
        if (booking.getTotalPrice() <= 0) {
            System.out.println("Số tiền không hợp lệ!");
            return false;
        }
        
        Payment payment = new Payment();
        payment.setPaymentId(generatePaymentId());
        payment.setBooking(booking);
        payment.setMethod(paymentMethod);
        payment.setAmount(booking.getTotalPrice());
        
        if (payment.processPayment()) {
            booking.setPayment(payment);
            allPayments.add(payment);
            return true;
        }
        
        return false;
    }
    
    /**
     * Process payment for a booking by booking ID
     * @param customer The customer making the payment
     * @param bookingId The booking ID
     * @param paymentMethod The payment method
     * @param amount The amount to pay
     * @return true if payment successful, false otherwise
     */
    public static boolean makePayment(Customer customer, int bookingId, String paymentMethod, double amount) {
        if (customer == null) {
            return false;
        }
        
        Booking booking = BookingService.getBookingById(customer, bookingId);
        if (booking == null) {
            System.out.println("Không tìm thấy booking!");
            return false;
        }
        
        if (booking.getTotalPrice() != amount) {
            System.out.println("Số tiền không khớp!");
            return false;
        }
        
        return processPayment(booking, paymentMethod);
    }
    
    /**
     * Refund a payment
     * @param payment The payment to refund
     * @return true if refund successful, false otherwise
     */
    public static boolean refundPayment(Payment payment) {
        if (payment == null) {
            return false;
        }
        return payment.refund();
    }
    
    /**
     * Get payment by ID
     * @param paymentId The payment ID
     * @return The Payment object if found, null otherwise
     */
    public static Payment getPaymentById(int paymentId) {
        return allPayments.stream()
                .filter(p -> p.getPaymentId() == paymentId)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Get all payments for a customer
     * @param customer The customer
     * @return List of payments
     */
    public static List<Payment> getPaymentsByCustomer(Customer customer) {
        if (customer == null) {
            return new ArrayList<>();
        }
        List<Payment> customerPayments = new ArrayList<>();
        for (Booking booking : customer.getBookingHistory()) {
            if (booking.getPayment() != null) {
                customerPayments.add(booking.getPayment());
            }
        }
        return customerPayments;
    }
    
    /**
     * Get all payments
     * @return List of all payments
     */
    public static List<Payment> getAllPayments() {
        return new ArrayList<>(allPayments);
    }
    
    private static int generatePaymentId() {
        return paymentIdCounter++;
    }
}

