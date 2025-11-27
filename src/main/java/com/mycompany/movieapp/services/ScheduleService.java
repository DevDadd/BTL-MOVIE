package com.mycompany.movieapp.services;

import com.mycompany.movieapp.models.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ScheduleService {
    private static List<Schedule> allSchedules = new ArrayList<>();
    private static int scheduleIdCounter = 1;
    
    static {
        initializeSampleSchedules();
    }
    
    private static void initializeSampleSchedules() {
        List<Movie> movies = MovieService.getAllMovies();
        if (movies.isEmpty()) {
            return;
        }
        
        Theater theater = new Theater(1, "CGV Vincom", "123 Nguyễn Huệ, Q1, TP.HCM", "1900 6017");
        
        Room room1 = new Room(1, "Phòng VIP", 50, "R001", theater);
        Room room2 = new Room(2, "Phòng Standard", 100, "R002", theater);
        Room room3 = new Room(3, "Phòng IMAX", 200, "R003", theater);
        
        com.mycompany.movieapp.enums.SeatType regularType = com.mycompany.movieapp.enums.SeatType.REGULAR;
        com.mycompany.movieapp.enums.SeatType vipType = com.mycompany.movieapp.enums.SeatType.VIP;
        
        for (int i = 1; i <= 10; i++) {
            room1.addSeat(new Seat(i, "A", i, vipType, room1));
            room2.addSeat(new Seat(i, "B", i, regularType, room2));
            room3.addSeat(new Seat(i, "C", i, regularType, room3));
        }
        
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        
        if (movies.size() > 0) {
            Movie movie1 = movies.get(0);
            allSchedules.add(new Schedule(generateScheduleId(), movie1, room1, 
                now.plusHours(2), now.plusHours(5)));
            allSchedules.add(new Schedule(generateScheduleId(), movie1, room2, 
                now.plusHours(5), now.plusHours(8)));
            allSchedules.add(new Schedule(generateScheduleId(), movie1, room3, 
                now.plusDays(1).withHour(14).withMinute(0), 
                now.plusDays(1).withHour(17).withMinute(0)));
        }
        
        if (movies.size() > 1) {
            Movie movie2 = movies.get(1);
            allSchedules.add(new Schedule(generateScheduleId(), movie2, room1, 
                now.plusHours(3), now.plusHours(5).plusMinutes(30)));
            allSchedules.add(new Schedule(generateScheduleId(), movie2, room2, 
                now.plusDays(1).withHour(10).withMinute(0), 
                now.plusDays(1).withHour(12).withMinute(30)));
        }
        
        if (movies.size() > 2) {
            Movie movie3 = movies.get(2);
            allSchedules.add(new Schedule(generateScheduleId(), movie3, room2, 
                now.plusHours(1), now.plusHours(3).plusMinutes(35)));
            allSchedules.add(new Schedule(generateScheduleId(), movie3, room3, 
                now.plusDays(1).withHour(18).withMinute(0), 
                now.plusDays(1).withHour(20).withMinute(35)));
        }
        
        if (movies.size() > 3) {
            Movie movie4 = movies.get(3);
            allSchedules.add(new Schedule(generateScheduleId(), movie4, room1, 
                now.plusHours(6), now.plusHours(8).plusMinutes(30)));
            allSchedules.add(new Schedule(generateScheduleId(), movie4, room2, 
                now.plusDays(1).withHour(20).withMinute(0), 
                now.plusDays(1).withHour(22).withMinute(30)));
        }
    }
    
    public static List<Schedule> getSchedulesByMovie(Movie movie) {
        return getSchedulesByMovie(movie, allSchedules);
    }
    
    public static List<Schedule> getSchedulesByMovie(Movie movie, List<Schedule> allSchedules) {
        if (movie == null) {
            return new ArrayList<>();
        }
        return allSchedules.stream()
                .filter(s -> s.getMovie().getMovieId() == movie.getMovieId())
                .filter(Schedule::isAvailableForBooking)
                .sorted(Comparator.comparing(Schedule::getStartTime))
                .collect(Collectors.toList());
    }
    
    public static List<Schedule> viewSchedules(int movieId) {
        return allSchedules.stream()
                .filter(s -> s.getMovie().getMovieId() == movieId)
                .filter(Schedule::isAvailableForBooking)
                .sorted(Comparator.comparing(Schedule::getStartTime))
                .collect(Collectors.toList());
    }

    public static List<Schedule> getSchedulesByDate(java.time.LocalDate date) {
        return getSchedulesByDate(date, allSchedules);
    }
    
    public static List<Schedule> getSchedulesByDate(java.time.LocalDate date, List<Schedule> allSchedules) {
        if (date == null) {
            return new ArrayList<>();
        }
        return allSchedules.stream()
                .filter(s -> s.getStartTime().toLocalDate().equals(date))
                .filter(Schedule::isAvailableForBooking)
                .sorted(Comparator.comparing(Schedule::getStartTime))
                .collect(Collectors.toList());
    }

    public static List<Schedule> getSchedulesByRoom(Room room) {
        return getSchedulesByRoom(room, allSchedules);
    }
    
    public static List<Schedule> getSchedulesByRoom(Room room, List<Schedule> allSchedules) {
        if (room == null) {
            return new ArrayList<>();
        }
        return allSchedules.stream()
                .filter(s -> s.getRoom().equals(room))
                .sorted(Comparator.comparing(Schedule::getStartTime))
                .collect(Collectors.toList());
    }

    public static boolean isRoomAvailable(Room room, LocalDateTime startTime, LocalDateTime endTime) {
        return isRoomAvailable(room, startTime, endTime, allSchedules);
    }
    
    public static boolean isRoomAvailable(Room room, LocalDateTime startTime, LocalDateTime endTime,
                                          List<Schedule> allSchedules) {
        if (room == null || startTime == null || endTime == null) {
            return false;
        }
        return allSchedules.stream()
                .filter(s -> s.getRoom().equals(room))
                .noneMatch(s -> isTimeOverlap(s.getStartTime(), s.getEndTime(), startTime, endTime));
    }
    
    private static boolean isValidSchedule(Schedule schedule) {
        if (schedule == null) {
            return false;
        }
        if (schedule.getStartTime().isBefore(LocalDateTime.now())) {
            return false;
        }
        if (schedule.getEndTime().isBefore(schedule.getStartTime())) {
            return false;
        }
        return true;
    }

    private static boolean isTimeOverlap(LocalDateTime start1, LocalDateTime end1,
                                         LocalDateTime start2, LocalDateTime end2) {
        return !end1.isBefore(start2) && !start1.isAfter(end2);
    }
    
    public static boolean addSchedule(Schedule schedule) {
        if (schedule == null) {
            return false;
        }
        
        if (!isValidSchedule(schedule)) {
            System.out.println("Lịch chiếu không hợp lệ!");
            return false;
        }
        
        if (!isRoomAvailable(schedule.getRoom(), schedule.getStartTime(), schedule.getEndTime())) {
            System.out.println("Phòng không khả dụng trong khoảng thời gian này!");
            return false;
        }
        
        schedule.setScheduleId(generateScheduleId());
        allSchedules.add(schedule);
        return true;
    }
    
    public static boolean updateSchedule(int scheduleId, Schedule updatedSchedule) {
        if (updatedSchedule == null) {
            return false;
        }
        
        Schedule existingSchedule = getScheduleById(scheduleId);
        if (existingSchedule == null) {
            return false;
        }
        
        if (!existingSchedule.getStartTime().equals(updatedSchedule.getStartTime()) ||
            !existingSchedule.getEndTime().equals(updatedSchedule.getEndTime()) ||
            !existingSchedule.getRoom().equals(updatedSchedule.getRoom())) {
            
            if (!isRoomAvailable(updatedSchedule.getRoom(), 
                    updatedSchedule.getStartTime(), 
                    updatedSchedule.getEndTime())) {
                System.out.println("Phòng không khả dụng trong khoảng thời gian này!");
                return false;
            }
        }
        
        if (!isValidSchedule(updatedSchedule)) {
            System.out.println("Lịch chiếu không hợp lệ!");
            return false;
        }
        
        existingSchedule.setMovie(updatedSchedule.getMovie());
        existingSchedule.setRoom(updatedSchedule.getRoom());
        existingSchedule.setStartTime(updatedSchedule.getStartTime());
        existingSchedule.setEndTime(updatedSchedule.getEndTime());
        
        return true;
    }
    
    public static boolean deleteSchedule(int scheduleId) {
        Schedule schedule = getScheduleById(scheduleId);
        if (schedule == null) {
            return false;
        }
        return allSchedules.remove(schedule);
    }
    
    public static Schedule getScheduleById(int scheduleId) {
        return allSchedules.stream()
                .filter(s -> s.getScheduleId() == scheduleId)
                .findFirst()
                .orElse(null);
    }
    
    public static List<Schedule> getAllSchedules() {
        return new ArrayList<>(allSchedules);
    }
    
    private static int generateScheduleId() {
        return scheduleIdCounter++;
    }
}