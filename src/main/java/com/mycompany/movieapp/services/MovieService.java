package com.mycompany.movieapp.services;

import com.mycompany.movieapp.models.*;
import java.util.*;
import java.util.stream.Collectors;

public class MovieService {

    private static List<Movie> movies = new ArrayList<>();

    public static boolean addMovie(Movie movie) {
        if (movie == null) return false;
        movies.add(movie);
        return true;
    }

    public static boolean updateMovie(int movieId, Movie updatedMovie) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getMovieId() == movieId) {
                movies.set(i, updatedMovie);
                return true;
            }
        }
        return false;
    }

    public static boolean deleteMovie(int movieId) {
        return movies.removeIf(m -> m.getMovieId() == movieId);
    }

    public static List<Movie> getAllMovies() {
        return new ArrayList<>(movies);
    }

    // ✅ Khôi phục lại method bạn cần
    public static Movie getMovieById(int movieId) {
        return movies.stream()
                .filter(m -> m.getMovieId() == movieId)
                .findFirst()
                .orElse(null);
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

    public static List<Movie> getMoviesByGenre(String genre, List<Movie> allMovies) {
        return allMovies.stream()
                .filter(m -> m.getGenre().equalsIgnoreCase(genre))
                .filter(Movie::isActive)
                .collect(Collectors.toList());
    }

    public static List<Movie> getUpcomingMovies(List<Movie> allMovies) {
        java.time.LocalDate today = java.time.LocalDate.now();
        return allMovies.stream()
                .filter(m -> m.getReleaseDate().isAfter(today))
                .sorted(Comparator.comparing(Movie::getReleaseDate))
                .collect(Collectors.toList());
    }

    public static List<Movie> getNowShowingMovies(List<Movie> allMovies) {
        return allMovies.stream()
                .filter(Movie::isShowingToday)
                .collect(Collectors.toList());
    }
}
