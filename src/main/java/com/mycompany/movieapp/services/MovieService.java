package com.mycompany.movieapp.services;

import com.mycompany.movieapp.models.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MovieService {
    private static List<Movie> allMovies = new ArrayList<>();
    
    static {
        allMovies.add(new Movie(1, "Avengers: Endgame", 
            "The epic conclusion to the Infinity Saga", "181 phút", 
            "Anthony Russo, Joe Russo", "Action", "Robert Downey Jr., Chris Evans", 
            LocalDate.now().minusDays(10).toString(), "PG-13", ""));
        allMovies.add(new Movie(2, "Spider-Man: No Way Home", 
            "Peter Parker's identity is revealed", "148 phút", 
            "Jon Watts", "Action", "Tom Holland, Zendaya", 
            LocalDate.now().minusDays(5).toString(), "PG-13", ""));
        allMovies.add(new Movie(3, "Dune", 
            "A noble family becomes embroiled in a war", "155 phút", 
            "Denis Villeneuve", "Sci-Fi", "Timothée Chalamet, Rebecca Ferguson", 
            LocalDate.now().minusDays(3).toString(), "PG-13", ""));
        allMovies.add(new Movie(4, "The Matrix Resurrections", 
            "Neo returns to the Matrix", "148 phút", 
            "Lana Wachowski", "Sci-Fi", "Keanu Reeves, Carrie-Anne Moss", 
            LocalDate.now().minusDays(1).toString(), "R", ""));
        allMovies.add(new Movie(5, "No Time to Die", 
            "James Bond's final mission", "163 phút", 
            "Cary Joji Fukunaga", "Action", "Daniel Craig, Léa Seydoux", 
            LocalDate.now().plusDays(5).toString(), "PG-13", ""));
    }
    
    public static List<Movie> getAllMovies() {
        return new ArrayList<>(allMovies);
    }
    
    public static void addMovie(Movie movie) {
        if (movie != null) {
            allMovies.add(movie);
        }
    }

    public static List<Movie> searchMovies(String keyword) {
        return searchMovies(keyword, getAllMovies());
    }
    
    public static List<Movie> searchMovies(String keyword, List<Movie> allMovies) {
        if (keyword == null || keyword.isEmpty()) {
            return allMovies;
        }

        String lowerKeyword = keyword.toLowerCase().trim();

        return allMovies.stream()
                .filter(m -> m.getTitle().toLowerCase().contains(lowerKeyword) ||
                        m.getGenre().toLowerCase().contains(lowerKeyword) ||
                        m.getDirector().toLowerCase().contains(lowerKeyword) ||
                        m.getActor().toLowerCase().contains(lowerKeyword))
                .filter(Movie::isActive)
                .collect(Collectors.toList());
    }
    
    public static List<Movie> searchMoviesByDate(java.time.LocalDate date) {
        return searchMoviesByDate(date, getAllMovies());
    }
    
    public static List<Movie> searchMoviesByDate(java.time.LocalDate date, List<Movie> allMovies) {
        if (date == null) {
            return new ArrayList<>();
        }
        return allMovies.stream()
                .filter(m -> {
                    LocalDate release = m.getReleaseDateAsDate();
                    return release != null && !release.isAfter(date);
                })
                .filter(Movie::isActive)
                .collect(Collectors.toList());
    }

    public static List<Movie> getMoviesByGenre(String genre) {
        return getMoviesByGenre(genre, getAllMovies());
    }
    
    public static List<Movie> getMoviesByGenre(String genre, List<Movie> allMovies) {
        if (genre == null || genre.isEmpty()) {
            return new ArrayList<>();
        }
        return allMovies.stream()
                .filter(m -> m.getGenre().equalsIgnoreCase(genre))
                .filter(Movie::isActive)
                .collect(Collectors.toList());
    }

    public static List<Movie> getUpcomingMovies() {
        return getUpcomingMovies(getAllMovies());
    }
    
    public static List<Movie> getUpcomingMovies(List<Movie> allMovies) {
        java.time.LocalDate today = java.time.LocalDate.now();
        return allMovies.stream()
                .filter(m -> {
                    LocalDate release = m.getReleaseDateAsDate();
                    return release != null && release.isAfter(today);
                })
                .sorted(Comparator.comparing(Movie::getReleaseDateAsDate,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }
    
    public static Movie getMovieById(int movieId) {
        return allMovies.stream()
                .filter(m -> m.getMovieId() == movieId)
                .findFirst()
                .orElse(null);
    }
    
    public static boolean updateMovie(int movieId, Movie updatedMovie) {
        if (updatedMovie == null) {
            return false;
        }
        
        Movie existingMovie = getMovieById(movieId);
        if (existingMovie == null) {
            return false;
        }
        
        existingMovie.setTitle(updatedMovie.getTitle());
        existingMovie.setDescription(updatedMovie.getDescription());
        existingMovie.setDuration(updatedMovie.getDuration());
        existingMovie.setDirector(updatedMovie.getDirector());
        existingMovie.setGenre(updatedMovie.getGenre());
        existingMovie.setActor(updatedMovie.getActor());
        existingMovie.setReleaseDate(updatedMovie.getReleaseDate());
        existingMovie.setRating(updatedMovie.getRating());
        existingMovie.setPosterUrl(updatedMovie.getPosterUrl());
        existingMovie.setActive(updatedMovie.isActive());
        
        return true;
    }
    
    public static boolean deleteMovie(int movieId) {
        Movie movie = getMovieById(movieId);
        if (movie == null) {
            return false;
        }
        movie.setActive(false);
        return true;
    }

    public static List<Movie> getNowShowingMovies(List<Movie> allMovies) {
        return allMovies.stream()
                .filter(Movie::isShowingToday)
                .collect(Collectors.toList());
    }
    
    public static List<Movie> getNowShowingMovies() {
        return getNowShowingMovies(getAllMovies());
    }
}