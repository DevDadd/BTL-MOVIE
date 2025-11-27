package com.mycompany.movieapp.models;

import java.util.ArrayList;
import java.util.List;

public class Theater {
    private int theaterId;
    private String name;
    private String location;
    private String contactNumber;
    private List<Room> rooms;

    public Theater() {
        this.rooms = new ArrayList<>();
    }

    public Theater(int theaterId, String name, String location, String contactNumber) {
        this.theaterId = theaterId;
        this.name = name;
        this.location = location;
        this.contactNumber = contactNumber;
        this.rooms = new ArrayList<>();
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    public void addRoom(Room room) {
        if (room != null && !rooms.contains(room)) {
            rooms.add(room);
        }
    }

    public Room getRoomById(int roomId) {
        return rooms.stream()
                .filter(r -> r.getRoomId() == roomId)
                .findFirst()
                .orElse(null);
    }

    public int getTotalCapacity() {
        return rooms.stream()
                .mapToInt(Room::getCapacity)
                .sum();
    }

    public int getTheaterId() { return theaterId; }
    public void setTheaterId(int theaterId) { this.theaterId = theaterId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public void setRooms(List<Room> rooms) { this.rooms = rooms; }

    @Override
    public String toString() {
        return name + " - " + location;
    }
}