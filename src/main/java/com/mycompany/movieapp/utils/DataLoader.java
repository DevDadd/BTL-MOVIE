package com.mycompany.movieapp.utils;

import com.mycompany.movieapp.models.*;
import com.mycompany.movieapp.enums.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;



public class DataLoader {

    private static List<Movie> movies = new ArrayList<>();
    private static List<Theater> theaters = new ArrayList<>();
    private static List<Schedule> schedules = new ArrayList<>();
    private static List<Customer> customers = new ArrayList<>();
    private static List<Staff> staffs = new ArrayList<>();




    public static void loadDemoData() {
        System.out.println("Đang tải dữ liệu demo...\n");

        loadMovies();
        loadTheaters();
        loadSchedules();
        loadUsers();

        System.out.println("✓ Đã tải " + movies.size() + " phim");
        System.out.println("✓ Đã tải " + theaters.size() + " rạp");
        System.out.println("✓ Đã tải " + schedules.size() + " lịch chiếu");
        System.out.println("✓ Đã tải " + (customers.size() + staffs.size()) + " users\n");
    }

    private static void loadMovies() {
        movies.add(new Movie(1, "Avengers: Endgame",
                "Cuộc chiến cuối cùng",
                "181 phút", "Anthony Russo", "Hành động",
                "Robert Downey Jr.", LocalDate.of(2024, 1, 15),
                "C13", "https://example.com/endgame.jpg"));

        movies.add(new Movie(2, "The Godfather",
                "Câu chuyện mafia",
                "175 phút", "Francis Ford Coppola", "Drama",
                "Marlon Brando", LocalDate.of(2024, 2, 1),
                "C18", "https://example.com/godfather.jpg"));
    }

    private static void loadTheaters() {
        Theater cgv = new Theater(1, "CGV Vincom", "191 Bà Triệu, HN", "1900-6017");


        Room room1 = new Room(1, "P01", 80);


        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H"};
        int seatId = 1;

        for (String row : rows) {
            for (int num = 1; num <= 10; num++) {
                SeatType type = SeatType.REGULAR;
                if (row.equals("G") || row.equals("H")) {
                    type = SeatType.VIP;
                }
                room1.addSeat(new Seat(seatId++, row, num, type, room1));
            }
        }

        cgv.addRoom(room1);
        theaters.add(cgv);
    }

    private static void loadSchedules() {
        LocalDateTime now = LocalDateTime.now();
        Movie movie = movies.get(0);
        Room room = theaters.get(0).getRooms().get(0);

        schedules.add(new Schedule(1, movie, room,
                now.plusDays(1).withHour(14).withMinute(0),
                now.plusDays(1).withHour(17).withMinute(0)));

        schedules.add(new Schedule(2, movie, room,
                now.plusDays(1).withHour(19).withMinute(30),
                now.plusDays(1).withHour(22).withMinute(30)));
    }

    private static void loadUsers() {
        Customer customer = new Customer(1, "john_doe", "123456",
                "john@example.com", "0901234567", "Hà Nội");
        customer.setLoyaltyPoints(500);
        customers.add(customer);

        staffs.add(new Staff(101, "admin", "admin123",
                "admin@cinema.com", "0909999999", "Hà Nội", "Manager"));
    }


    public static List<Movie> getMovies() { return movies; }
    public static List<Theater> getTheaters() { return theaters; }
    public static List<Schedule> getSchedules() { return schedules; }
    public static List<Customer> getCustomers() { return customers; }
    public static List<Staff> getStaffs() { return staffs; }
}