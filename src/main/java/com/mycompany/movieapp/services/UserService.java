package com.mycompany.movieapp.services;

import com.mycompany.movieapp.models.User;
import com.mycompany.movieapp.models.Customer;
import com.mycompany.movieapp.models.Staff;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static List<User> users = new ArrayList<>();
    private static User currentUser;
    
    static {
        users.add(new Customer(1, "customer1", "password123", "customer1@example.com", "0123456789", "123 Main St"));
        users.add(new Customer(2, "customer2", "password123", "customer2@example.com", "0987654321", "456 Oak Ave"));
        users.add(new Staff(3, "staff1", "admin123", "staff1@example.com", "0111222333", "789 Pine Rd", "Admin"));
        users.add(new Staff(4, "admin", "admin", "admin@example.com", "0999888777", "321 Elm St", "Admin"));
    }
    
    public static User findByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
    
    public static User authenticate(String username, String password) {
        User user = findByUsername(username);
        if (user != null && user.login(username, password)) {
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

