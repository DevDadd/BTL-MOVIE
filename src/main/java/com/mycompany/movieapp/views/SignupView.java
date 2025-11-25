/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.movieapp.views;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author taova
 */
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
