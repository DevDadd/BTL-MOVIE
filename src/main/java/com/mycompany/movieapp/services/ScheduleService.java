package com.mycompany.movieapp.services;

import com.mycompany.movieapp.models.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ScheduleService {

    public static List<Schedule> getSchedulesByMovie(Movie movie, List<Schedule> allSchedules) {
        return allSchedules.stream()
                .filter(s -> s.getMovie().equals(movie))
                .filter(Schedule::isAvailableForBooking)
                .sorted(Comparator.comparing(Schedule::getStartTime))
                .collect(Collectors.toList());
    }

    public static List<Schedule> getSchedulesByDate(java.time.LocalDate date, List<Schedule> allSchedules) {
        return allSchedules.stream()
                .filter(s -> s.getStartTime().toLocalDate().equals(date))
                .filter(Schedule::isAvailableForBooking)
                .sorted(Comparator.comparing(Schedule::getStartTime))
                .collect(Collectors.toList());
    }

    public static List<Schedule> getSchedulesByRoom(Room room, List<Schedule> allSchedules) {
        return allSchedules.stream()
                .filter(s -> s.getRoom().equals(room))
                .sorted(Comparator.comparing(Schedule::getStartTime))
                .collect(Collectors.toList());
    }

    public static boolean isRoomAvailable(Room room, LocalDateTime startTime, LocalDateTime endTime,
                                          List<Schedule> allSchedules) {
        return allSchedules.stream()
                .filter(s -> s.getRoom().equals(room))
                .noneMatch(s -> isTimeOverlap(s.getStartTime(), s.getEndTime(), startTime, endTime));
    }

    private static boolean isTimeOverlap(LocalDateTime start1, LocalDateTime end1,
                                         LocalDateTime start2, LocalDateTime end2) {
        return !end1.isBefore(start2) && !start1.isAfter(end2);
    }
}