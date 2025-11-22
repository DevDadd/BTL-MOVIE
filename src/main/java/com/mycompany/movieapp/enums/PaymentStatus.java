package com.mycompany.movieapp.enums;

public enum PaymentStatus {
    PENDING("Chờ thanh toán"),
    CONFIRMED("Đã thanh toán"),
    CANCELED("Đã hủy"),
    REFUNDED("Đã hoàn tiền");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}