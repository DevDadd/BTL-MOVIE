package com.mycompany.movieapp.services;

import com.mycompany.movieapp.models.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentService {
    private static List<Payment> allPayments = new ArrayList<>();
    private static int paymentIdCounter = 1;
    
    public static boolean processPayment(Booking booking, String paymentMethod) {
        if (booking == null) {
            System.out.println("Booking không hợp lệ!");
            return false;
        }
        
        if (booking.getTotalPrice() <= 0) {
            System.out.println("Số tiền không hợp lệ!");
            return false;
        }

        Payment payment = new Payment(booking, paymentMethod, booking.getTotalPrice());



        if (payment.processPayment()) {
            booking.setPayment(payment);
            allPayments.add(payment);
            return true;
        }
        
        return false;
    }
    
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
    
    public static boolean refundPayment(Payment payment) {
        if (payment == null) {
            return false;
        }
        return payment.refund();
    }
    
    public static Payment getPaymentById(int paymentId) {
        return allPayments.stream()
                .filter(p -> p.getPaymentId() == paymentId)
                .findFirst()
                .orElse(null);
    }
    
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
    
    public static List<Payment> getAllPayments() {
        return new ArrayList<>(allPayments);
    }
    
    private static int generatePaymentId() {
        return paymentIdCounter++;
    }
}

