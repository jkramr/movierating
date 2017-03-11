package com.jkramr.demo.service;

import com.jkramr.demo.model.Movie;
import com.jkramr.demo.model.MovieRankRecord;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.internal.Function;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Custom buffered iterator, loads movies and rankings lazily,
 * takes care of collisions if applicable by {@link #skipRanks} parameter
 */
@RequiredArgsConstructor
public class MovieRankIterator
        implements Iterator<MovieRankRecord> {

  private final Integer                                    top;
  private final BiFunction<Long, Long, Map<Long, Integer>> rankingSupplier;
  private final Function<Set<Long>, List<Movie>>           movieSupplier;
  private final Integer                                    bufferSize;
  private final boolean                                    skipRanks;

  private AtomicLong counter = new AtomicLong();

  private Map<Long, Integer> rankings;
  private Iterator<Long>     idIterator;

  private Map<Long, Movie> movies;

  private Long    previousRank;
  private Integer previousRating;

  @Override
  public void forEachRemaining(@NonNull Consumer<? super MovieRankRecord> action) {
    MovieRankRecord next;
    while (hasNext() && (next = next()).getRank() <= top) {
      action.accept(next);
    }
  }

  @Override
  public boolean hasNext() {
    return rankings == null
           ? load(0, top - 1)
           : idIterator.hasNext() || load(counter.get() + 1, counter.get() + bufferSize);
  }

  @Override
  public MovieRankRecord next() {
    long currentIndex = counter.incrementAndGet(); // we start from 1, not 0

    Long id = idIterator.next();

    int rating = rankings.get(id);

    boolean shouldSkip = skipRanks && previousRating != null && rating == previousRating;

    Long rank = shouldSkip ? previousRank : currentIndex;

    previousRank = rank;
    previousRating = rating;

    Movie movie = movies.get(id);

    return movie == null
           // if true - broken state: movie rating is in ranking DB, but absent in movie DB
           ? new MovieRankRecord(Movie.EMPTY, rank)
           // degrade
           : new MovieRankRecord(movie, rank);
  }

  private boolean load(long start, long end) {
    rankings = rankingSupplier.apply(start, end);

    idIterator = rankings.keySet().iterator();

    movies = movieSupplier.apply(rankings.keySet())
                          .stream()
                          .collect(Collectors.toMap(Movie::getId, movie -> movie));

    return !rankings.isEmpty() && !movies.isEmpty();
  }

}
