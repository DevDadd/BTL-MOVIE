package com.mycompany.movieapp.models;

import com.mycompany.movieapp.services.MovieService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Staff extends User {
    private String role;
    private LocalDate hireDate;
    private List<String> activityLog;

    public Staff(int userId, String username, String password, String email,
                 String phoneNumber, String address, String role) {
        super(userId, username, password, email, phoneNumber, address);
        this.role = role;
        this.hireDate = LocalDate.now();
        this.activityLog = new ArrayList<>();
    }

    @Override
    public String getDashboardInfo() {
        return String.format("STAFF DASHBOARD\nChức vụ: %s\nNgày vào làm: %s",
                role, hireDate);
    }


    public boolean addMovie(Movie movie) {
        if (movie == null) return false;
        boolean success = MovieService.addMovie(movie);
        if (success) {
            logActivity("Thêm phim: " + movie.getTitle());
        }
        return success;
    }

    public boolean updateMovie(int movieId, Movie updatedMovie) {
        if (updatedMovie == null) return false;
        boolean success = MovieService.updateMovie(movieId, updatedMovie);
        if (success) {
            logActivity("Cập nhật phim ID: " + movieId);
        }
        return success;
    }

    public boolean deleteMovie(int movieId) {
        boolean success = MovieService.deleteMovie(movieId);
        if (success) {
            logActivity("Xóa phim ID: " + movieId);
        }
        return success;
    }


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



    private void logActivity(String activity) {
        String log = LocalDateTime.now() + " - " + username + ": " + activity;
        activityLog.add(log);
    }

    public List<String> getActivityLog() {
        return new ArrayList<>(activityLog);
    }


    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    @Override
    public String toString() {
        return username + " - " + role;
    }
}