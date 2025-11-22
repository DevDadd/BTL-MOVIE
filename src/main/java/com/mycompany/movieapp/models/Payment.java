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

    public Payment() {
        this.time = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
    }

    public Payment(int paymentId, Booking booking, String method, double amount) {
        this.paymentId = paymentId;
        this.booking = booking;
        this.method = method;
        this.amount = amount;
        this.time = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
    }

    // Business Methods
    public boolean processPayment() {
        try {
            if (booking == null) {
                throw new PaymentException("Booking không hợp lệ!");
            }

            if (amount != booking.getTotalPrice()) {
                throw new PaymentException("Số tiền không khớp với tổng giá booking!");
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
                    throw new PaymentException("Phương thức thanh toán không được hỗ trợ!");
            }

            if (paymentSuccess) {
                this.status = PaymentStatus.CONFIRMED;
                this.transactionId = generateTransactionId();
                booking.setStatus(com.mycompany.movieapp.enums.BookingStatus.CONFIRMED);
                sendReceipt();
                return true;
            } else {
                this.status = PaymentStatus.CANCELED;
                return false;
            }

        } catch (PaymentException e) {
            System.out.println("Lỗi thanh toán: " + e.getMessage());
            this.status = PaymentStatus.CANCELED;
            return false;
        }
    }

    private boolean processMoMo() {
        System.out.println("Đang xử lý thanh toán MoMo...");
        return true;
    }

    private boolean processCreditCard() {
        System.out.println("Đang xử lý thanh toán thẻ...");
        return true;
    }

    private boolean processCash() {
        System.out.println("Thanh toán tiền mặt tại quầy");
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
        return "TXN" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    public boolean sendReceipt() {
        if (status != PaymentStatus.CONFIRMED) {
            return false;
        }

        String receipt = generateReceipt();

        Notification notification = new Notification(
                "Thanh toán thành công!\n" + receipt,
                LocalDateTime.now()
        );

        return notification.sendEmail(booking.getCustomer());
    }

    private String generateReceipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("========== HÓA ĐƠN THANH TOÁN ==========\n");
        receipt.append("Mã giao dịch: ").append(transactionId).append("\n");
        receipt.append("Thời gian: ").append(time).append("\n");
        receipt.append("Phương thức: ").append(method).append("\n");
        receipt.append("Số tiền: ").append(String.format("%,.0f VNĐ", amount)).append("\n");
        receipt.append("\n");
        receipt.append(booking.getBookingInfo());
        receipt.append("========================================\n");
        return receipt.toString();
    }

    public boolean refund() {
        if (status != PaymentStatus.CONFIRMED) {
            return false;
        }

        System.out.println("Đang xử lý hoàn tiền " + String.format("%,.0f VNĐ", amount));

        this.status = PaymentStatus.REFUNDED;

        Notification notification = new Notification(
                "Đã hoàn tiền " + String.format("%,.0f VNĐ", amount) + " cho booking #" + booking.getBookingId(),
                LocalDateTime.now()
        );
        notification.sendEmail(booking.getCustomer());

        return true;
    }

    public String getPaymentInfo() {
        return String.format("Payment #%d - %s - %,.0f VNĐ - %s",
                paymentId, method, amount, status.getDisplayName());
    }

    // Getters and Setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    @Override
    public String toString() {
        return "Payment #" + paymentId + " - " + status.getDisplayName();
    }
}