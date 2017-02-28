package com.jkramr.demo.service;

import com.jkramr.demo.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;

@Component
public class RatingProcessor {

    private MovieRatingService movieRatingService;

    @Value("${skipRanks:true}")
    Boolean skipRanks;

    @Autowired
    public RatingProcessor(MovieRatingService movieRatingService) {
        this.movieRatingService = movieRatingService;
    }

    public void publishMoviesRatings() {
        publishRating(movieRatingService.getAllRatings());
    }

    public void publishMoviesRatings(long limit) {
        publishRating(movieRatingService.getRatings(limit));
    }

    private void publishRating(Collection<Long> ratingDataSlice) {
        Long counter = 0L;

        Long previousRank = 0L;
        Integer previousRating = null;

        for (Long id : ratingDataSlice) {
            counter++;

            Movie movie = movieRatingService.findById(id);

            Long rank;

            Integer rating = movie.getRating();

            if (skipRanks && Objects.equals(previousRating, rating)) {
                rank = previousRank;
            } else {
                rank = counter;
            }

            movieRatingService.publishRatingDataSlice(rank, movie);

            previousRank = rank;
            previousRating = rating;
        }
    }

    public void publishApiDoc() {
        System.out.println();
        System.out.println("SUCCESS.");
        System.out.println();
        System.out.println("Restful API available at localhost:8080/ratings{?page,size,sort}");
        System.out.println();
        System.out.println("Movie list available at localhost:8080/movies{?page,size,sort}");
        System.out.println();
        System.out.println("Movie search available at localhost:8080/movies/search");
        System.out.println();
        System.out.println("Rating search available at localhost:8080/ratings/search");
        System.out.println();
    }
}
