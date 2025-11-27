package com.mycompany.movieapp.models;

import com.mycompany.movieapp.enums.SeatType;

public class Seat {
    private int seatId;
    private String row;
    private int number;
    private SeatType type;
    private Room room;

    public Seat() {}

    public Seat(int seatId, String row, int number, SeatType type, Room room) {
        this.seatId = seatId;
        this.row = row;
        this.number = number;
        this.type = type;
        this.room = room;
    }

    public String getSeatInfo() {
        return String.format("Ghế %s%d - %s", row, number, type.getDisplayName());
    }

    public String getSeatCode() {
        return row + number;
    }

    public int getSeatId() { return seatId; }
    public void setSeatId(int seatId) { this.seatId = seatId; }

    public String getRow() { return row; }
    public void setRow(String row) { this.row = row; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public SeatType getType() { return type; }
    public void setType(SeatType type) { this.type = type; }

    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }

    @Override
    public String toString() {
        return getSeatCode();
    }
}