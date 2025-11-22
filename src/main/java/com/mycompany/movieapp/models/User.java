package com.mycompany.movieapp.models;

import com.mycompany.movieapp.enums.AccountStatus;

public abstract class User {
    protected int userId;
    protected String username;
    protected String password;
    protected String email;
    protected String phoneNumber;
    protected String address;
    protected AccountStatus status;

    public User() {
        this.status = AccountStatus.ACTIVE;
    }

    public User(int userId, String username, String password, String email,
                String phoneNumber, String address) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.status = AccountStatus.ACTIVE;
    }

    // Abstract method - mỗi loại user có cách hiển thị dashboard khác nhau
    public abstract String getDashboardInfo();

    // Business Methods
    public boolean login(String username, String password) {
        if (!isActive()) {
            System.out.println("Tài khoản không hoạt động!");
            return false;
        }
        return this.username.equals(username) && this.password.equals(password);
    }

    public void logout() {
        System.out.println("Đăng xuất thành công!");
    }

    public boolean updateProfile(String email, String phoneNumber, String address) {
        if (email != null && !email.isEmpty()) this.email = email;
        if (phoneNumber != null && !phoneNumber.isEmpty()) this.phoneNumber = phoneNumber;
        if (address != null && !address.isEmpty()) this.address = address;
        return true;
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (!this.password.equals(oldPassword)) {
            return false;
        }
        if (newPassword == null || newPassword.length() < 6) {
            return false;
        }
        this.password = newPassword;
        return true;
    }

    public boolean isActive() {
        return status == AccountStatus.ACTIVE;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }

    @Override
    public String toString() {
        return username + " (" + status.getDisplayName() + ")";
    }
}