package com.jkramr.demo.service;

import com.jkramr.demo.model.Movie;
import com.jkramr.demo.repository.MovieRepository;
import com.jkramr.demo.repository.RankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

@Component
public class MovieRatingService {

    private MovieRepository movieRepository;
    private RankRepository rankRepository;

    @Value("${skipRanks:true}")
    boolean skipRanks;

    @Autowired
    public MovieRatingService(
            MovieRepository movieRepository,
            RankRepository rankRepository
    ) {
        this.movieRepository = movieRepository;
        this.rankRepository = rankRepository;
    }

    public List<String> publishMoviesRatings(BiFunction<Movie, Long, String> recordPublisher) {
        List<String> rankings = new ArrayList<>();
        Long counter = 0L;

        Long previousRank = 0L;
        Integer previousRating = null;

        for (Long id : rankRepository.findAll()) {
            counter++;

            Movie movie = movieRepository.findOne(id);

            Long rank;

            Integer rating = movie.getRating();

            if (skipRanks && Objects.equals(previousRating, rating)) {
                rank = previousRank;
            } else {
                rank = counter;
            }

            rankings.add(recordPublisher.apply(movie, rank));

            previousRank = rank;
            previousRating = rating;
        }

        return rankings;
    }

    public String publishToStdin(Movie movie, Long rank) {
        String rankRecord = rank + " " + movie.getMovieTitle();

        System.out.println(rankRecord);

        return rankRecord;
    }

    public void publishApiDoc() {
        System.out.println();
        System.out.println("SUCCESS.");
        System.out.println();
        System.out.println("Movie list available at localhost:8080/movies{?page,size,sort}");
        System.out.println();
        System.out.println("Movie search available at localhost:8080/movies/search");
        System.out.println();
    }

    public Movie register(Movie movie) {
        if (movie == null) {
            return null;
        }

        movie = movieRepository.save(movie);

        rankRepository.put(movie.getId(), movie.getRating());

        return movie;
    }
}
