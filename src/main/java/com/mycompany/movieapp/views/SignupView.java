package com.mycompany.movieapp.views;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SignupView {
    public static void main(String [] args){
         SwingUtilities.invokeLater(() -> {
            Signup signupFrame = new Signup(); 
            
            signupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            signupFrame.setLocationRelativeTo(null);
            
            signupFrame.setVisible(true);
        });
    }
}
