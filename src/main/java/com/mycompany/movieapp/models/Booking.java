package com.mycompany.movieapp.models;

import com.mycompany.movieapp.enums.BookingStatus;
import com.mycompany.movieapp.exceptions.InvalidBookingException;
import com.mycompany.movieapp.exceptions.SeatNotAvailableException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Booking {
    private int bookingId;
    private Customer customer;
    private Schedule schedule;
    private LocalDateTime bookingTime;
    private double totalPrice;
    private BookingStatus status;
    private List<ShowSeat> seatList;
    private Payment payment;

    public Booking() {
        this.bookingTime = LocalDateTime.now();
        this.status = BookingStatus.PENDING;
        this.seatList = new ArrayList<>();
    }

    public Booking(int bookingId, Customer customer, Schedule schedule) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.schedule = schedule;
        this.bookingTime = LocalDateTime.now();
        this.status = BookingStatus.PENDING;
        this.seatList = new ArrayList<>();
    }

    public boolean addSeat(ShowSeat showSeat) throws SeatNotAvailableException {
        if (showSeat == null) {
            return false;
        }

        if (!showSeat.isAvailable()) {
            throw new SeatNotAvailableException("Ghế " + showSeat.getSeat().getSeatCode() + " đã được đặt!");
        }

        if (seatList.contains(showSeat)) {
            return false;
        }

        seatList.add(showSeat);
        calculateTotalPrice();
        return true;
    }

    public boolean removeSeat(ShowSeat showSeat) {
        if (seatList.remove(showSeat)) {
            calculateTotalPrice();
            return true;
        }
        return false;
    }

    private void calculateTotalPrice() {
        totalPrice = seatList.stream()
                .mapToDouble(ShowSeat::getPrice)
                .sum();

        if (customer != null) {
            int discount = customer.getLoyaltyDiscount();
            totalPrice = totalPrice * (100 - discount) / 100;
        }
    }

    public boolean confirmBooking() throws InvalidBookingException {
        if (seatList.isEmpty()) {
            throw new InvalidBookingException("Chưa chọn ghế nào!");
        }

        if (!schedule.isAvailableForBooking()) {
            throw new InvalidBookingException("Suất chiếu không còn khả dụng!");
        }

        for (ShowSeat seat : seatList) {
            if (!seat.isAvailable()) {
                throw new InvalidBookingException("Ghế " + seat.getSeat().getSeatCode() + " đã được đặt!");
            }
        }

        for (ShowSeat seat : seatList) {
            seat.book();
        }

        this.status = BookingStatus.CONFIRMED;

        if (customer != null) {
            customer.addBooking(this);
        }

        return true;
    }

    public boolean cancelBooking() {
        if (!isCancelable()) {
            return false;
        }

        for (ShowSeat seat : seatList) {
            seat.release();
        }

        this.status = BookingStatus.CANCELED;

        if (payment != null && payment.getStatus() == com.mycompany.movieapp.enums.PaymentStatus.CONFIRMED) {
            payment.refund();
        }

        return true;
    }

    public boolean isCancelable() {
        if (status != BookingStatus.CONFIRMED && status != BookingStatus.PENDING) {
            return false;
        }

        long hoursUntilShow = ChronoUnit.HOURS.between(LocalDateTime.now(), schedule.getStartTime());
        return hoursUntilShow >= 2;
    }

    public List<ShowSeat> getBookedSeats() {
        return new ArrayList<>(seatList);
    }

    public String getBookingInfo() {
        StringBuilder info = new StringBuilder();
        info.append("========== THÔNG TIN BOOKING ==========\n");
        info.append("Mã booking: ").append(bookingId).append("\n");
        info.append("Khách hàng: ").append(customer.getUsername()).append("\n");
        info.append("Phim: ").append(schedule.getMovie().getTitle()).append("\n");
        info.append("Suất chiếu: ").append(schedule.getStartTime()).append("\n");
        info.append("Ghế đã chọn: ");
        for (ShowSeat seat : seatList) {
            info.append(seat.getSeat().getSeatCode()).append(" ");
        }
        info.append("\n");
        info.append("Tổng tiền: ").append(String.format("%,.0f VNĐ", totalPrice)).append("\n");
        info.append("Trạng thái: ").append(status.getDisplayName()).append("\n");
        info.append("=======================================\n");
        return info.toString();
    }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Schedule getSchedule() { return schedule; }
    public void setSchedule(Schedule schedule) { this.schedule = schedule; }

    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public List<ShowSeat> getSeatList() { return seatList; }
    public void setSeatList(List<ShowSeat> seatList) {
        this.seatList = seatList;
        calculateTotalPrice();
    }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }

    @Override
    public String toString() {
        return "Booking #" + bookingId + " - " + status.getDisplayName();
    }
}