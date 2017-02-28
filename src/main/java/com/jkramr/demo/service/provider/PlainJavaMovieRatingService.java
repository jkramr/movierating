package com.jkramr.demo.service.provider;

import com.jkramr.demo.model.Movie;
import com.jkramr.demo.model.Rating;
import com.jkramr.demo.repository.MovieRepository;
import com.jkramr.demo.repository.RatingRecordRepository;
import com.jkramr.demo.service.MovieRatingService;

import java.util.*;
import java.util.stream.Collectors;

public class PlainJavaMovieRatingService implements MovieRatingService {

    private MovieRepository movieRepository;
    private RatingRecordRepository ratingRepository;

    /**
     * Stub implementation using in-memory ranking storage (red-black tree, sorted by score)
     * Will die when memory runs out, not persistent, use Redis!
     */
    private TreeMap<Integer, Set<Long>> ratings;

    public PlainJavaMovieRatingService(
            MovieRepository movieRepository,
            RatingRecordRepository ratingRepository
    ) {
        this.movieRepository = movieRepository;
        this.ratingRepository = ratingRepository;
        this.ratings = new TreeMap<>();
    }

    @Override
    public void registerMovie(Movie movie) {
        if (movie == null) {
            return;
        }

        movieRepository.save(movie);

        ratings.putIfAbsent(movie.getRating(), new TreeSet<>());
        ratings.get(movie.getRating()).add(movie.getId());
    }

    @Override
    public Collection<Long> getAllRatings() {
        return ratings.keySet()
                .stream()
                .flatMap(key -> ratings.get(key).stream())
                .collect(Collectors.toList());
    }

    @Override
    public Movie findById(Long id) {
        return movieRepository.findOne(id);
    }

    @Override
    public Rating publishRatingDataSlice(Long rank, Movie movie) {
        Long timestamp = System.currentTimeMillis();

        Rating rating = new Rating(rank, timestamp, movie.getId(), movie.getRating(), movie.getMovieTitle());

        ratingRepository.save(rating);

        System.out.println(rating);

        return rating;
    }

    @Override
    public Collection<Long> getRatings(long limit) {
        return ratings.keySet()
                .stream()
                .flatMap(key -> ratings.get(key).stream())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
