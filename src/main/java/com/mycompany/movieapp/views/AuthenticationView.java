package com.mycompany.movieapp.views;

import javax.swing.JFrame; 
import javax.swing.SwingUtilities;

public class AuthenticationView {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login loginFrame = new Login(); 
            
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setLocationRelativeTo(null);
            
            loginFrame.setVisible(true);
        });
    }
}