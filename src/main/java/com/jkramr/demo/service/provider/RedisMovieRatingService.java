package com.jkramr.demo.service.provider;

import com.jkramr.demo.model.Movie;
import com.jkramr.demo.model.Rating;
import com.jkramr.demo.repository.RatingRecordRepository;
import com.jkramr.demo.repository.redis.RatingRedisOperations;
import com.jkramr.demo.repository.MovieRepository;
import com.jkramr.demo.service.MovieRatingService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor
public class RedisMovieRatingService implements MovieRatingService {

    @NonNull
    private MovieRepository movieRepository;

    @NonNull
    private RatingRecordRepository weeklyRatingRepository;

    @NonNull
    private RatingRedisOperations ratingRedisOperations;

    @Override
    public void registerMovie(Movie movie) {
        if (movie == null) {
            return;
        }

        movie = movieRepository.save(movie);
        ratingRedisOperations.put(movie.getId(), movie.getRating());
    }

    @Override
    public Collection<Long> getAllRatings() {
        return ratingRedisOperations.findAll();
    }

    @Override
    public Movie findById(Long id) {
        return movieRepository.findOne(id);
    }

    @Override
    public Rating publishRatingDataSlice(Long rank, Movie movie) {
        Long timestamp = System.currentTimeMillis();

        return weeklyRatingRepository.save(new Rating(rank, timestamp, movie.getId(), movie.getRating(), movie.getMovieTitle()));
    }

    @Override
    public Collection<Long> getRatings(long limit) {
        return ratingRedisOperations.findFirst(limit);
    }
}
