package com.mycompany.movieapp.exceptions;

public class SeatNotAvailableException extends Exception {
    public SeatNotAvailableException(String message) {
        super(message);
    }
}