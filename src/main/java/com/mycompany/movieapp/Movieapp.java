package com.mycompany.movieapp;

import com.mycompany.movieapp.utils.DataLoader;
import com.mycompany.movieapp.models.*;
import com.mycompany.movieapp.services.*;
import java.util.*;

public class Movieapp {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   HỆ THỐNG ĐẶT VÉ XEM PHIM");
        System.out.println("========================================\n");


        DataLoader.loadDemoData();


        demoCustomerBooking();

        System.out.println("\n========================================");
        System.out.println("   DEMO HOÀN TẤT");
        System.out.println("========================================");
    }

    private static void demoCustomerBooking() {
        System.out.println("========== DEMO ĐẶT VÉ ==========\n");

        List<Movie> movies = DataLoader.getMovies();
        List<Schedule> schedules = DataLoader.getSchedules();
        Customer customer = DataLoader.getCustomers().get(0);


        List<Movie> results = MovieService.searchMovies("Avengers", movies);
        System.out.println("Tìm thấy: " + results.get(0).getTitle());


        Movie movie = results.get(0);
        List<Schedule> movieSchedules = customer.viewSchedules(movie.getMovieId(), schedules);
        System.out.println("Có " + movieSchedules.size() + " suất chiếu");


        Schedule schedule = movieSchedules.get(0);
        List<ShowSeat> seats = schedule.getAvailableSeats();
        List<ShowSeat> selected = Arrays.asList(seats.get(0), seats.get(1));

        try {
            Booking booking = BookingService.createBooking(customer, schedule, selected);
            System.out.println("✓ Đặt vé thành công!");
            System.out.println("Tổng tiền: " + String.format("%,.2f VNĐ", booking.getTotalPrice()));


            boolean paid = BookingService.confirmAndPayBooking(booking, "MOMO");
            if (paid) {
                System.out.println("✓ Thanh toán thành công!");
            }
        } catch (Exception e) {
            System.out.println("✗ Lỗi: " + e.getMessage());
        }
    }


    public static List<Movie> getMovies() { return DataLoader.getMovies(); }
    public static List<Schedule> getSchedules() { return DataLoader.getSchedules(); }
    public static List<Theater> getTheaters() { return DataLoader.getTheaters(); }
}