package com.mycompany.movieapp.enums;

public enum SeatType {
    REGULAR("Ghế thường", 50000),
    VIP("Ghế VIP", 80000),
    COUPLE("Ghế đôi", 150000);

    private final String displayName;
    private final int basePrice;

    SeatType(String displayName, int basePrice) {
        this.displayName = displayName;
        this.basePrice = basePrice;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getBasePrice() {
        return basePrice;
    }
}