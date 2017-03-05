package com.jkramr.demo.repository;

import com.jkramr.demo.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

/**
 * This RESTful repository provides access to it's entries:
 * <p>
 * <a href=http://localhost:8080/movies>Movie API</a> parameters: {?page,size,sort}
 * <p>
 * <a href=http://localhost:8080/movies/search>Movie Search API reference</a>
 * <p>
 * Spring boot allows updating/inserting entities with POST requests:
 * <p>
 * <code>$ curl -X POST -i -H "Content-Type:application/json" -d '{"year":"1989", "rating":"100", "title":"Westworld"}' http://localhost:8080/movies</code>
 */
@RepositoryRestResource
public interface MovieRepository extends JpaRepository<Movie, Long> {
    // easy switch between DBs:
//    public interface MovieRepository extends MongoRepository<Movie, Long> {
//    public interface MovieRepository extends Neo4jRepository<Movie, Long> {

    Collection<Movie> findByMovieTitle(@Param("title") String title);

    Collection<Movie> findByRating(@Param("rating") Integer rating);

    Collection<Movie> findByYear(@Param("year") Integer year);


}
