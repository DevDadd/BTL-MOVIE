package com.mycompany.movieapp.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Schedule {
    private int scheduleId;
    private Movie movie;
    private Room room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<ShowSeat> showSeats;

    public Schedule() {
        this.showSeats = new ArrayList<>();
    }

    public Schedule(int scheduleId, Movie movie, Room room,
                    LocalDateTime startTime, LocalDateTime endTime) {
        this.scheduleId = scheduleId;
        this.movie = movie;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.showSeats = new ArrayList<>();
        initializeShowSeats();
    }

    private void initializeShowSeats() {
        if (room != null) {
            for (Seat seat : room.getSeats()) {
                ShowSeat showSeat = new ShowSeat();
                showSeat.setSeat(seat);
                showSeat.setSchedule(this);
                showSeat.setStatus("Available");
                showSeat.setPrice(seat.getType().getBasePrice());
                showSeats.add(showSeat);
            }
        }
    }

    public List<ShowSeat> getAvailableSeats() {
        return showSeats.stream()
                .filter(ss -> "Available".equals(ss.getStatus()))
                .collect(Collectors.toList());
    }

    public List<ShowSeat> getShowSeatInfo() {
        return new ArrayList<>(showSeats);
    }

    public Room getRoom() {
        return room;
    }

    public boolean isUpcoming() {
        return startTime.isAfter(LocalDateTime.now());
    }

    public boolean isAvailableForBooking() {
        return startTime.isAfter(LocalDateTime.now().plusMinutes(30));
    }

    public String getScheduleInfo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("%s - %s\nPhòng: %s\nThời gian: %s",
                movie.getTitle(), room.getName(), room.getRoomNumber(),
                startTime.format(formatter));
    }

    public int getAvailableSeatsCount() {
        return (int) showSeats.stream()
                .filter(ss -> "Available".equals(ss.getStatus()))
                .count();
    }

    // Getters and Setters
    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }

    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }

    public void setRoom(Room room) { this.room = room; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public List<ShowSeat> getShowSeats() { return showSeats; }
    public void setShowSeats(List<ShowSeat> showSeats) { this.showSeats = showSeats; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
        return movie.getTitle() + " - " + startTime.format(formatter);
    }
}