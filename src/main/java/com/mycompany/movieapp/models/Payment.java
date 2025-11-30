package com.mycompany.movieapp.models;

import com.mycompany.movieapp.enums.PaymentStatus;
import com.mycompany.movieapp.exceptions.PaymentException;
import java.time.LocalDateTime;

public class Payment {
    private int paymentId;
    private Booking booking;
    private String method;
    private double amount;
    private LocalDateTime time;
    private PaymentStatus status;
    private String transactionId;

    public Payment(Booking booking, String method, double amount) {
        this.booking = booking;
        this.method = method;
        this.amount = amount;
        this.time = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
    }

    public boolean processPayment() {
        try {
            if (booking == null) {
                throw new PaymentException("Booking không hợp lệ!");
            }

            if (amount != booking.getTotalPrice()) {
                throw new PaymentException("Số tiền không khớp!");
            }

            if (!isValidPaymentMethod()) {
                throw new PaymentException("Phương thức thanh toán không hợp lệ!");
            }

            boolean paymentSuccess = false;

            switch (method.toUpperCase()) {
                case "MOMO":
                    paymentSuccess = processMoMo();
                    break;
                case "VISA":
                case "MASTERCARD":
                    paymentSuccess = processCreditCard();
                    break;
                case "CASH":
                    paymentSuccess = processCash();
                    break;
                default:
                    throw new PaymentException("Phương thức không hỗ trợ!");
            }

            if (paymentSuccess) {
                this.status = PaymentStatus.CONFIRMED;
                this.transactionId = generateTransactionId();
                booking.setStatus(com.mycompany.movieapp.enums.BookingStatus.CONFIRMED);
                sendNotification();
                return true;
            } else {
                this.status = PaymentStatus.CANCELED;
                return false;
            }

        } catch (PaymentException e) {
            System.out.println("Lỗi: " + e.getMessage());
            this.status = PaymentStatus.CANCELED;
            return false;
        }
    }

    private boolean processMoMo() {
        System.out.println("Đang xử lý MoMo...");
        return true;
    }

    private boolean processCreditCard() {
        System.out.println("Đang xử lý thẻ...");
        return true;
    }

    private boolean processCash() {
        System.out.println("Thanh toán tiền mặt");
        return true;
    }

    private boolean isValidPaymentMethod() {
        String[] validMethods = {"MOMO", "VISA", "MASTERCARD", "CASH"};
        for (String valid : validMethods) {
            if (valid.equalsIgnoreCase(method)) {
                return true;
            }
        }
        return false;
    }

    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis();
    }


    private void sendNotification() {
        Notification notification = new Notification(
                "Thanh toán thành công! Mã GD: " + transactionId,
                LocalDateTime.now()
        );
        notification.sendEmail(booking.getCustomer());
    }

    public boolean refund() {
        if (status != PaymentStatus.CONFIRMED) {
            return false;
        }

        System.out.println("Hoàn tiền: " + String.format("%,.2f VNĐ", amount));
        this.status = PaymentStatus.REFUNDED;

        Notification notification = new Notification(
                "Đã hoàn tiền " + String.format("%,.2f VNĐ", amount),
                LocalDateTime.now()
        );
        notification.sendEmail(booking.getCustomer());

        return true;
    }


    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public Booking getBooking() { return booking; }
    public String getMethod() { return method; }
    public double getAmount() { return amount; }
    public LocalDateTime getTime() { return time; }
    public PaymentStatus getStatus() { return status; }
    public String getTransactionId() { return transactionId; }

    @Override
    public String toString() {
        return "Payment #" + paymentId + " - " + status.getDisplayName();
    }
}