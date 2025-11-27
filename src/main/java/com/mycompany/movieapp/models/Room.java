package com.mycompany.movieapp.models;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private int roomId;
    private String name;
    private int capacity;
    private String roomNumber;
    private Theater theater;
    private List<Seat> seats;

    public Room() {
        this.seats = new ArrayList<>();
    }

    public Room(int roomId, String name, int capacity, String roomNumber, Theater theater) {
        this.roomId = roomId;
        this.name = name;
        this.capacity = capacity;
        this.roomNumber = roomNumber;
        this.theater = theater;
        this.seats = new ArrayList<>();
    }

    public List<Seat> getSeats() {
        return new ArrayList<>(seats);
    }

    public void addSeat(Seat seat) {
        if (seat != null && !seats.contains(seat)) {
            seats.add(seat);
        }
    }

    public int getCapacity() {
        return capacity;
    }

    public Seat getSeatByRowAndNumber(String row, int number) {
        return seats.stream()
                .filter(s -> s.getRow().equals(row) && s.getNumber() == number)
                .findFirst()
                .orElse(null);
    }

    public boolean isFull() {
        return seats.size() >= capacity;
    }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public Theater getTheater() { return theater; }
    public void setTheater(Theater theater) { this.theater = theater; }

    public void setSeats(List<Seat> seats) { this.seats = seats; }

    @Override
    public String toString() {
        return "Phòng " + roomNumber + " - " + name;
    }
}