package com.jkramr.demo;

import com.jkramr.demo.model.Movie;
import com.jkramr.demo.service.MovieRatingService;
import com.jkramr.demo.util.SimpleFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;

@Component
class SampleInputDataCLR implements CommandLineRunner {

    private MovieRatingService movieRatingService;

    @Value("${path:null}")
    private String filePath;

    @Autowired
    SampleInputDataCLR(
            MovieRatingService movieRatingService
    ) {
        this.movieRatingService = movieRatingService;
    }

    @Override
    public void run(String... args) throws Exception {
        SimpleFileReader ratingsFileReader = readFile();

        Arrays.stream(ratingsFileReader.readFile()
                .split("\n"))
                .forEach(movieEntry -> {
                    Movie movie = parseMovieFromString(movieEntry);

                    movieRatingService.register(movie);
                });

        // publish sample data slice
        movieRatingService.publishApiDoc();

        movieRatingService.publishMoviesRatings(movieRatingService::publishToStdin);
    }

    private Movie parseMovieFromString(String movieEntry) {

        try {

            Integer year = Integer.valueOf(movieEntry.substring(0, 4));
            Float rating = Float.valueOf(movieEntry.substring(5, 8)) * 10;
            String movieTitle = movieEntry.substring(9);

            return new Movie(year, rating.intValue(), movieTitle);
        } catch (Exception ignored) {
            return null;
        }
    }

    private SimpleFileReader readFile() {

        String path = filePath;

        try {
            return new SimpleFileReader(new FileReader(new File(path)));
        } catch (FileNotFoundException ignored) {
        }

        return new SimpleFileReader(
                new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("2_ratings.txt")));
    }
}