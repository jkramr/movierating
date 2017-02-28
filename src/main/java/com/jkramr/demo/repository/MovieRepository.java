package com.jkramr.demo.repository;

import com.jkramr.demo.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

/**
 * Entries:
 * http://localhost:8080/movies{?page,size,sort}
 *
 * Search API reference:
 * http://localhost:8080/movies/search
 */
@RepositoryRestResource
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Collection<Movie> findByMovieTitle(@Param("title") String title);

    // easy switch between DBs:
//    public interface MovieRepository extends MongoRepository<Movie, Long> {
//    public interface MovieRepository extends Neo4jRepository<Movie, Long> {
}
