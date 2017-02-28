package com.jkramr.demo.repository.listener;

import com.jkramr.demo.model.Movie;
import com.jkramr.demo.repository.MovieRepository;
import com.jkramr.demo.repository.RankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

@Component
public class AddMovieListener extends AbstractRepositoryEventListener<Movie> {

    private RankRepository rankRepository;
    private MovieRepository movieRepository;

    @Autowired
    public AddMovieListener(
            RankRepository rankRepository,
            MovieRepository movieRepository
    ) {
        this.rankRepository = rankRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    protected void onAfterCreate(Movie entity) {

        entity = movieRepository.save(entity);
        rankRepository.put(entity.getId(), entity.getRating());

        super.onAfterCreate(entity);
    }

    @Override
    protected void onAfterSave(Movie entity) {
        entity = movieRepository.save(entity);
        rankRepository.put(entity.getId(), entity.getRating());

        super.onAfterSave(entity);
    }

    @Override
    protected void onAfterDelete(Movie entity) {
        rankRepository.remove(entity.getId(), entity.getRating());

        super.onAfterDelete(entity);
    }
}
