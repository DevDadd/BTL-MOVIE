package com.mycompany.movieapp.models;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class Movie {
    private int movieId;
    private String title;
    private String description;
    private String duration;
    private String director;
    private String genre;
    private String actor;
    private String releaseDate;
    private String rating;
    private String posterUrl;
    private boolean isActive;

    public Movie() {
        this.isActive = true;
    }

    public Movie(int movieId, String title, String description, String duration,
                 String director, String genre, String actor, String releaseDate,
                 String rating, String posterUrl) {
        this.movieId = movieId;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.director = director;
        this.genre = genre;
        this.actor = actor;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.posterUrl = posterUrl;
        this.isActive = true;
    }

    public String getDetails() {
        return String.format(
                "Phim: %s\nĐạo diễn: %s\nThể loại: %s\nDiễn viên: %s\n" +
                        "Thời lượng: %s\nRating: %s\nKhởi chiếu: %s",
                title, director, genre, actor, duration, rating, releaseDate
        );
    }

    public List<Schedule> getSchedules(List<Schedule> allSchedules) {
        return allSchedules.stream()
                .filter(s -> s.getMovie().getMovieId() == this.movieId)
                .filter(s -> s.getStartTime().isAfter(java.time.LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    public boolean isShowingToday() {
        LocalDate release = parseReleaseDate();
        return release != null && this.isActive && !release.isAfter(LocalDate.now());
    }

    public LocalDate getReleaseDateAsDate() {
        return parseReleaseDate();
    }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getActor() { return actor; }
    public void setActor(String actor) { this.actor = actor; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString() {
        return title + " (" + rating + ")";
    }

    private LocalDate parseReleaseDate() {
        if (releaseDate == null || releaseDate.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(releaseDate.trim());
        } catch (DateTimeParseException ex) {
            return null;
        }
    }
}