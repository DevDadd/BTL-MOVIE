package com.mycompany.movieapp.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Staff extends User {
    private String role;
    private LocalDate hireDate;
    private List<String> activityLog;

    public Staff() {
        super();
        this.activityLog = new ArrayList<>();
        this.hireDate = LocalDate.now();
    }

    public Staff(int userId, String username, String password, String email,
                 String phoneNumber, String address, String role) {
        super(userId, username, password, email, phoneNumber, address);
        this.role = role;
        this.hireDate = LocalDate.now();
        this.activityLog = new ArrayList<>();
    }

    @Override
    public String getDashboardInfo() {
        return String.format("STAFF DASHBOARD\nChức vụ: %s\nNgày vào làm: %s\nQuyền hạn: Quản lý phim, lịch chiếu, phòng",
                role, hireDate);
    }

    // Movie Management
    public boolean addMovie(Movie movie) {
        if (movie == null) return false;
        logActivity("Thêm phim: " + movie.getTitle());
        return true;
    }

    public boolean updateMovie(int movieId, Movie updatedMovie) {
        if (updatedMovie == null) return false;
        logActivity("Cập nhật phim ID: " + movieId);
        return true;
    }

    public boolean deleteMovie(int movieId) {
        logActivity("Xóa phim ID: " + movieId);
        return true;
    }

    // Schedule Management
    public boolean addSchedule(Schedule schedule) {
        if (schedule == null) return false;
        if (!isValidSchedule(schedule)) {
            System.out.println("Lịch chiếu không hợp lệ!");
            return false;
        }
        logActivity("Thêm lịch chiếu: " + schedule.getScheduleInfo());
        return true;
    }

    public boolean updateSchedule(int scheduleId, Schedule updatedSchedule) {
        if (updatedSchedule == null) return false;
        logActivity("Cập nhật lịch chiếu ID: " + scheduleId);
        return true;
    }

    public boolean deleteSchedule(int scheduleId) {
        logActivity("Xóa lịch chiếu ID: " + scheduleId);
        return true;
    }

    private boolean isValidSchedule(Schedule schedule) {
        if (schedule.getStartTime().isBefore(LocalDateTime.now())) {
            return false;
        }
        if (schedule.getEndTime().isBefore(schedule.getStartTime())) {
            return false;
        }
        return true;
    }

    // Room Management
    public boolean addRoom(Room room) {
        if (room == null) return false;
        logActivity("Thêm phòng: " + room.getName());
        return true;
    }

    public boolean updateRoom(int roomId, Room updatedRoom) {
        if (updatedRoom == null) return false;
        logActivity("Cập nhật phòng ID: " + roomId);
        return true;
    }

    // Seat Management
    public boolean setSeat(Seat seat) {
        if (seat == null) return false;
        logActivity("Thêm ghế: " + seat.getSeatInfo());
        return true;
    }

    public boolean updateSeat(int seatId, Seat updatedSeat) {
        if (updatedSeat == null) return false;
        logActivity("Cập nhật ghế ID: " + seatId);
        return true;
    }

    // Utility
    private void logActivity(String activity) {
        String log = LocalDateTime.now() + " - " + username + ": " + activity;
        activityLog.add(log);
    }

    public List<String> getActivityLog() {
        return new ArrayList<>(activityLog);
    }

    // Getters and Setters
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    @Override
    public String toString() {
        return username + " - " + role;
    }
}