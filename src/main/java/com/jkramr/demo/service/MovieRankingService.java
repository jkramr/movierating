package com.jkramr.demo.service;

import com.jkramr.demo.model.Movie;
import com.jkramr.demo.repository.MovieRepository;
import com.jkramr.demo.repository.RankRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Main service, knows how to process rankings
 * <p>
 * Configuration:
 * <p>
 * {@link #skipRanks}: if true, movies will be ranked with respect to rating collisions
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
 *
 * {@link #topSize}: default top size for /movies/rankings page
 *
 * {@link #bufferSize}: default size of chunks, loaded from repository
 */
@Component
public class MovieRankingService {

    private MovieRepository movieRepository;
    private RankRepository rankRepository;

    @Value("${skipRanks:true}") boolean skipRanks;
    @Value("${topSize:125}") int topSize;
    @Value("${bufferSize:100}") int bufferSize;

    @Autowired
    public MovieRankingService(
            MovieRepository movieRepository,
            RankRepository rankRepository
    ) {
        this.movieRepository = movieRepository;
        this.rankRepository = rankRepository;
    }

    public List<String> publishTopRankings(BiFunction<Movie, Long, String> recordPublisher, Integer nullableTopSize) {
        return publishTop(recordPublisher, nullableTopSize == null ? topSize : nullableTopSize);
    }

    public List<String> publishTopRankings(BiFunction<Movie, Long, String> recordPublisher) {
        return publishTop(recordPublisher, topSize);
    }

    public String publishToStdin(@NonNull Movie movie, Long rank) {
        String rankRecord = rank + " " + movie.getMovieTitle();

        System.out.println(rankRecord);

        return rankRecord;
    }

    public Movie register(Movie movie) {
        if (movie == null) {
            return null;
        }

        movie = movieRepository.save(movie);

        rankRepository.put(movie.getId(), movie.getRating());

        return movie;
    }

    private List<String> publishTop(BiFunction<Movie, Long, String> recordPublisher, int top) {
        List<String> records = new ArrayList<>();

        if (rankRepository.isEmpty()) {
            return records;
        }

        new MovieRankIterator(
                top,
                (start, end) -> rankRepository.findRange(start, end),
                (keys) -> movieRepository.findAll(keys),
                bufferSize,
                skipRanks
        ).forEachRemaining(rankRecord -> records.add(
                recordPublisher.apply(
                        rankRecord.getMovie(),
                        rankRecord.getRank()
                )
        ));

        return records;
    }

    public void publishApiDoc() {
        System.out.println();
        System.out.println("Movie API available at localhost:8080/movies{?page,size,sort}");
        System.out.println();
        System.out.println("Movie search API reference available at localhost:8080/movies/search");
        System.out.println();
    }
}
