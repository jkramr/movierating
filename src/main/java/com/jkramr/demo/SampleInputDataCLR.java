package com.jkramr.demo;

import com.jkramr.demo.model.Movie;
import com.jkramr.demo.service.MovieRankingService;
import com.jkramr.demo.util.SimpleFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;

@Component
class SampleInputDataCLR
        implements CommandLineRunner {

  private MovieRankingService movieRankingService;

  @Value("${path:null}")
  private String filePath;

  @Autowired
  SampleInputDataCLR(
          MovieRankingService movieRankingService
  ) {
    this.movieRankingService = movieRankingService;
  }

  @Override
  public void run(String... args)
          throws Exception {
    SimpleFileReader ratingsFileReader = readFile();

    Arrays.stream(ratingsFileReader.readFile()
                                   .split("\n"))
          .forEach(movieEntry -> {
            Movie movie = parseMovieFromString(movieEntry);

            movieRankingService.register(movie);
          });

    movieRankingService.publishApiDoc();

    movieRankingService.publishTopRankings(movieRankingService::publishToStdin);
  }

  private Movie parseMovieFromString(String movieEntry) {

    try {

      Integer year       = Integer.valueOf(movieEntry.substring(0, 4));
      Float   rating     = Float.valueOf(movieEntry.substring(5, 8)) * 10;
      String  movieTitle = movieEntry.substring(9);

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
            new InputStreamReader(this.getClass()
                                      .getClassLoader()
                                      .getResourceAsStream("2_ratings.txt")));
  }
}
