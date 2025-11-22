package com.mycompany.movieapp.enums;

public enum AccountStatus {
    ACTIVE("Hoạt động"),
    CLOSED("Đã đóng"),
    BANNED("Bị cấm"),
    BLACKLIST("Danh sách đen");

    private final String displayName;

    AccountStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}