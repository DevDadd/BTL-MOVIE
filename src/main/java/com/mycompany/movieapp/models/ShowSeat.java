package com.mycompany.movieapp.models;

public class ShowSeat {
    private String status;
    private double price;
    private Schedule schedule;
    private Seat seat;


    public ShowSeat(Schedule schedule, Seat seat, double price) {
        this.schedule = schedule;
        this.seat = seat;
        this.price = price;
        this.status = "Available";
    }

    public double getPrice() {
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
        return String.format("%s - %s - %,.2f VNĐ",
                seat.getSeatCode(), seat.getType().getDisplayName(), price);
    }


    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void setPrice(double price) { this.price = price; }

    public Schedule getSchedule() { return schedule; }
    public void setSchedule(Schedule schedule) { this.schedule = schedule; }

    public Seat getSeat() { return seat; }
    public void setSeat(Seat seat) { this.seat = seat; }

    @Override
    public String toString() {
        return seat.getSeatCode() + " (" + status + ")";
    }
}