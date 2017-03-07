package com.jkramr.demo.controller;

import com.jkramr.demo.service.MovieRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class RankingsController {

    private MovieRankingService movieRankingService;

    @Autowired
    public RankingsController(MovieRankingService movieRankingService) {
        this.movieRankingService = movieRankingService;
    }

    /**
     * Servlet listener to display current rankings top
     * @param top - max rank until which to display the top
     */
    @RequestMapping(value = "movies/rankings", method = RequestMethod.GET)
    public Collection<String> top(@RequestParam(required = false) Integer top) {
        return movieRankingService.publishTopRankings(movieRankingService::publishToStdin, top);
    }
}
