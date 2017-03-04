package com.jkramr.demo.service;

import com.jkramr.demo.model.Movie;
import com.jkramr.demo.repository.MovieRepository;
import com.jkramr.demo.repository.RankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Main service, knows how to process rankings
 * <p>
 * skipRanks: if true, movies will be ranked with respect to rating collisions
 * <p>
 * e.g if {@link #skipRanks}==true if two movies have same rating, they will have same ranking
 * <p>
 * So for the ratings:
 * <p>
 * 1965 8.5 Sound of Music
 * <p>
 * 1994 9.8 Shawshank Redemption
 * <p>
 * 2005 8.5 The Incredibles
 * <p>
 * 1997 7.6 Home Alone 2
 * <p>
 * The end result will yield the following output to standard out:
 * <p>
 * 1 Shawshank Redemption
 * <p>
 * 2 Sound of Music
 * <p>
 * 2 The Incredibles
 * <p>
 * 4 Home Alone 2
 */
@Component
public class MovieRankingService {

    private MovieRepository movieRepository;
    private RankRepository rankRepository;

    @Value("${skipRanks:true}")
    boolean skipRanks;

    @Autowired
    public MovieRankingService(
            MovieRepository movieRepository,
            RankRepository rankRepository
    ) {
        this.movieRepository = movieRepository;
        this.rankRepository = rankRepository;
    }

    public List<String> publishMoviesRatings(BiFunction<Long, Long, String> recordPublisher, Integer nullableLimit) {
        return Optional.ofNullable(nullableLimit)
                .map(limit -> publish(recordPublisher, rankRepository.findRange(0, limit - 1)))
                .orElseGet(() -> publishMoviesRatings(recordPublisher));
    }

    public List<String> publishMoviesRatings(BiFunction<Long, Long, String> recordPublisher) {
        return publish(recordPublisher, rankRepository.findAll());
    }

    public List<String> publish(BiFunction<Long, Long, String> recordPublisher, Map<Long, Integer> ratings) {
        List<String> records = new ArrayList<>();

        long counter = 0;

        long previousRank = 0;
        Integer previousRating = null;

        for (Long id : ratings.keySet()) {
            counter++;

            Long rank;

            Integer rating = ratings.get(id);

            if (skipRanks && Objects.equals(previousRating, rating)) {
                rank = previousRank;
            } else {
                rank = counter;
            }

            records.add(recordPublisher.apply(id, rank));

            previousRank = rank;
            previousRating = rating;
        }

        return records;
    }

    public String publishToStdin(Long id, Long rank) {
        String rankRecord = Optional.ofNullable(movieRepository.findOne(id))
                .map(movie -> rank + " " + movie.getMovieTitle())
                .orElse(rank + " Degraded gracefully");

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
