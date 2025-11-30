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
        if (!movies.isEmpty() || !theaters.isEmpty() || !customers.isEmpty() || !staffs.isEmpty())
            return;

        loadMovies();
        loadTheaters();
        loadSchedules();
        loadUsers();

        System.out.println("✓ Đã tải " + movies.size() + " phim");
        System.out.println("✓ Đã tải " + theaters.size() + " rạp");
        System.out.println("✓ Đã tải " + schedules.size() + " lịch chiếu");
        System.out.println("✓ Đã tải " + (customers.size() + staffs.size()) + " users");
    }

    private static void loadMovies() {
        movies.add(new Movie(1, "Avengers: Endgame",
                "Cuộc chiến cuối cùng của các siêu anh hùng",
                "181 phút", "Anthony Russo", "Hành động",
                "Robert Downey Jr.", LocalDate.now().minusDays(7),
                "C13", "https://example.com/endgame.jpg"));

        movies.add(new Movie(2, "The Godfather",
                "Câu chuyện về gia đình mafia",
                "175 phút", "Francis Ford Coppola", "Drama",
                "Marlon Brando", LocalDate.now().minusDays(30),
                "C18", "https://example.com/godfather.jpg"));

        movies.add(new Movie(3, "Inception",
                "Bộ phim về giấc mơ",
                "148 phút", "Christopher Nolan", "Sci-Fi",
                "Leonardo DiCaprio", LocalDate.now().minusDays(14),
                "C16", "https://example.com/inception.jpg"));

        movies.add(new Movie(4, "Dune: Part Two",
                "Hành trình trả thù",
                "166 phút", "Denis Villeneuve", "Sci-Fi",
                "Timothée Chalamet", LocalDate.now().plusDays(7),
                "C13", "https://example.com/dune2.jpg"));
    }

    private static void loadTheaters() {
        Theater cgv = new Theater(1, "CGV Vincom", "191 Bà Triệu, HN", "1900-6017");
        Room room1 = new Room(1, "P01", 80);

        String[] rows = {"A", "B", "C", "D", "E", "F"};
        int seatId = 1;
        for (String row : rows) {
            for (int num = 1; num <= 10; num++) {
                SeatType type = (row.equals("E") || row.equals("F")) ? SeatType.VIP : SeatType.REGULAR;
                room1.addSeat(new Seat(seatId++, row, num, type, room1));
            }
        }
        cgv.addRoom(room1);
        theaters.add(cgv);
    }

    private static void loadSchedules() {
        if (movies.isEmpty() || theaters.isEmpty()) return;

        LocalDateTime now = LocalDateTime.now();
        Room room = theaters.get(0).getRooms().get(0);
        int scheduleId = 1;

        for (Movie movie : movies) {
            schedules.add(new Schedule(scheduleId++, movie, room,
                    now.plusDays(1).withHour(14).withMinute(0),
                    now.plusDays(1).withHour(17).withMinute(0)));

            schedules.add(new Schedule(scheduleId++, movie, room,
                    now.plusDays(1).withHour(19).withMinute(30),
                    now.plusDays(1).withHour(22).withMinute(30)));

            schedules.add(new Schedule(scheduleId++, movie, room,
                    now.plusDays(2).withHour(15).withMinute(0),
                    now.plusDays(2).withHour(18).withMinute(0)));
        }
    }

    private static void loadUsers() {
        // Customers
        Customer c1 = new Customer(1, "john_doe", "123456",
                "john@example.com", "0901234567", "Hà Nội");
        c1.setLoyaltyPoints(500);
        customers.add(c1);

        customers.add(new Customer(2, "jane_smith", "123456",
                "jane@example.com", "0907654321", "TP.HCM"));

        // Staffs
        staffs.add(new Staff(101, "admin", "admin123",
                "admin@cinema.com", "0909999999", "Hà Nội", "Admin"));
    }

    public static List<Movie> getMovies() { return movies; }
    public static List<Theater> getTheaters() { return theaters; }
    public static List<Schedule> getSchedules() { return schedules; }
    public static List<Customer> getCustomers() { return customers; }
    public static List<Staff> getStaffs() { return staffs; }
}
