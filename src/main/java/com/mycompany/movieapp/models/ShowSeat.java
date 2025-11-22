package com.mycompany.movieapp.models;

public class ShowSeat {
    private String status;
    private int price;
    private Schedule schedule;
    private Seat seat;

    public ShowSeat() {
        this.status = "Available";
    }

    public ShowSeat(String status, int price, Schedule schedule, Seat seat) {
        this.status = status;
        this.price = price;
        this.schedule = schedule;
        this.seat = seat;
    }

    public int getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return "Available".equals(status);
    }

    public void book() {
        if (isAvailable()) {
            this.status = "Booked";
        } else {
            throw new IllegalStateException("Ghế đã được đặt!");
        }
    }

    public void release() {
        this.status = "Available";
    }

    public String getSeatInfo() {
        return String.format("%s - %s - %,d VNĐ",
                seat.getSeatCode(), seat.getType().getDisplayName(), price);
    }

    // Getters and Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void setPrice(int price) { this.price = price; }

    public Schedule getSchedule() { return schedule; }
    public void setSchedule(Schedule schedule) { this.schedule = schedule; }

    public Seat getSeat() { return seat; }
    public void setSeat(Seat seat) { this.seat = seat; }

    @Override
    public String toString() {
        return seat.getSeatCode() + " (" + status + ")";
    }
}