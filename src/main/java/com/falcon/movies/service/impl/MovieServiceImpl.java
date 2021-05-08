package com.falcon.movies.service.impl;

import com.falcon.movies.dto.MovieDto;
import com.falcon.movies.entity.Movie;
import com.falcon.movies.repository.MovieRepository;
import com.falcon.movies.service.MovieService;
import com.falcon.movies.service.mapper.MovieMapper;
import com.falcon.movies.service.observers.notifications.EmailNotificationObserverImpl;
import com.falcon.movies.service.observers.notifications.NewMoviesListener;
import com.falcon.movies.service.observers.notifications.SmsNotificationObserverImpl;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl extends BaseServiceImpl<Movie, MovieRepository, MovieDto, MovieMapper>
    implements MovieService {

    private final NewMoviesListener newMoviesListener;

    public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper, NewMoviesListener newMoviesListener) {
        super(movieRepository, movieMapper);

        this.newMoviesListener = newMoviesListener;
        this.newMoviesListener.addObserver(new EmailNotificationObserverImpl());
        this.newMoviesListener.addObserver(new SmsNotificationObserverImpl());
    }

    @Override
    public MovieDto save(MovieDto item) {
        MovieDto savedMovie = super.save(item);
        newMoviesListener.setNotificationText("New movie was added with title: " + savedMovie.getTitle());
        newMoviesListener.notifyObservers();
        return savedMovie;
    }
}
