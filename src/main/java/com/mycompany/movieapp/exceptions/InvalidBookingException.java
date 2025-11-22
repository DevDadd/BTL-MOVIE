package com.mycompany.movieapp.exceptions;

public class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}