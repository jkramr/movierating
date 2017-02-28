package com.jkramr.demo.controller;

import com.jkramr.demo.service.MovieRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class RankingsController {

    private MovieRatingService movieRatingService;

    @Autowired
    public RankingsController(MovieRatingService movieRatingService) {
        this.movieRatingService = movieRatingService;
    }

    @RequestMapping(value = "movies/rankings", method = RequestMethod.GET)
    public Collection<String> rankings() {
        return movieRatingService.publishMoviesRatings(movieRatingService::publishToStdin);
    }
}
