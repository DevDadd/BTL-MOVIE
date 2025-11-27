package com.mycompany.movieapp.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notification {
    private String message;
    private LocalDateTime date;
    private String recipient;
    private boolean sent;

    public Notification() {
        this.date = LocalDateTime.now();
        this.sent = false;
    }

    public Notification(String message, LocalDateTime date) {
        this.message = message;
        this.date = date;
        this.sent = false;
    }

    public boolean sendEmail(Customer customer) {
        if (customer == null || customer.getEmail() == null) {
            return false;
        }

        this.recipient = customer.getEmail();

        System.out.println("=== GỬI EMAIL ===");
        System.out.println("Đến: " + recipient);
        System.out.println("Tiêu đề: Thông báo từ hệ thống đặt vé");
        System.out.println("Nội dung:\n" + message);
        System.out.println("Thời gian: " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        System.out.println("=================");

        this.sent = true;
        return true;
    }

    public boolean sendNotification(Customer customer) {
        if (customer == null) {
            return false;
        }

        System.out.println("=== PUSH NOTIFICATION ===");
        System.out.println("User: " + customer.getUsername());
        System.out.println("Message: " + message);
        System.out.println("=========================");

        this.sent = true;
        return true;
    }

    public boolean sendSMS(Customer customer) {
        if (customer == null || customer.getPhoneNumber() == null) {
            return false;
        }

        System.out.println("=== GỬI SMS ===");
        System.out.println("SĐT: " + customer.getPhoneNumber());
        System.out.println("Nội dung: " + message);
        System.out.println("===============");

        this.sent = true;
        return true;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public boolean isSent() { return sent; }
    public void setSent(boolean sent) { this.sent = sent; }

    @Override
    public String toString() {
        return "Notification: " + message.substring(0, Math.min(50, message.length())) + "...";
    }
}