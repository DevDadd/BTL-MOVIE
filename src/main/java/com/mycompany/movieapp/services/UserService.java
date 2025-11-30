package com.mycompany.movieapp.services;

import com.mycompany.movieapp.models.*;
import com.mycompany.movieapp.utils.DataLoader;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private static List<User> users = new ArrayList<>();
    private static User currentUser;

    static {
        DataLoader.loadDemoData();
        users.addAll(DataLoader.getCustomers());
        users.addAll(DataLoader.getStaffs());
    }

    public static User findByUsername(String username) {
        if (username == null || username.isEmpty()) return null;
        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    public static User authenticate(String username, String password) {
        User user = findByUsername(username);
        if (user != null && user.login(username, password)) {
            currentUser = user;
            return user;
        }
        return null;
    }

    public static void addUser(User user) {
        if (user != null && findByUsername(user.getUsername()) == null) {
            users.add(user);
        }
    }

    public static List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
