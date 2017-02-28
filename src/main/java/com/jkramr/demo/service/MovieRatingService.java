package com.jkramr.demo.service;

import com.jkramr.demo.model.Movie;
import com.jkramr.demo.model.Rating;

import java.util.Collection;

public interface MovieRatingService {

    void registerMovie(Movie movie);

    Collection<Long> getAllRatings();

    Movie findById(Long id);

    Rating publishRatingDataSlice(Long rank, Movie movie);

    Collection<Long> getRatings(long limit);
}
