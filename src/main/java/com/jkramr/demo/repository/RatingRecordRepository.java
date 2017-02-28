package com.jkramr.demo.repository;

import com.jkramr.demo.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

/**
 * Entries:
 * http://localhost:8080/ratings{?page,size,sort}
 *
 * Search API reference:
 * http://localhost:8080/ratings/search
 */
@RepositoryRestResource
public interface RatingRecordRepository extends JpaRepository<Rating, Long> {

    Collection<Rating> findByRank(@Param("rank") Long rank);

    Collection<Rating> findByMovieId(@Param("movieId") Long movieId);

    Collection<Rating> findByRating(@Param("rating") Integer rating);

    Collection<Rating> findByMovieTitle(@Param("title") String movieTitle);
}
