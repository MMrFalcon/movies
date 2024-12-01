package com.falcon.movies.config;

import com.falcon.movies.entity.Author;
import com.falcon.movies.entity.Movie;
import com.falcon.movies.entity.enumeration.MovieType;
import com.falcon.movies.repository.AuthorRepository;
import com.falcon.movies.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Load data on application startup if database is empty.
 * Skip when tests profile is active.
 */
@Component
@Profile({"!test"})
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final AuthorRepository authorRepository;
    private final MovieRepository movieRepository;

    public DataInitializer(AuthorRepository authorRepository, MovieRepository movieRepository) {
        this.authorRepository = authorRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("Load data on startup...");
        loadAuthors();
        loadMovies();
    }

    private void loadMovies() {
        long moviesCount = movieRepository.count();
        log.info("Persisted movies: {}", moviesCount);
        if (moviesCount <= 0) {
            log.info("No persisted movies found - adding new data");
            Movie movie = new Movie();
            movie.setTitle("Example title 1");
            movie.setAuthor(authorRepository.findAll().get(0));
            movie.setMovieType(MovieType.COMEDY);
            movie.setProductionDate(LocalDate.now().minusDays(123));
            movie.setTime(120);

            Movie movie2 = new Movie();
            movie2.setTitle("Example title 2");
            movie2.setAuthor(authorRepository.findAll().get(1));
            movie2.setMovieType(MovieType.HORROR);
            movie2.setProductionDate(LocalDate.now().minusDays(33));
            movie2.setTime(120);

            movie = movieRepository.save(movie);
            movie2 = movieRepository.save(movie2);

            log.info("Saved movie 1 : {} , saved movie 2 : {}", movie, movie2);
        }
    }

    private void loadAuthors() {
        long authorsCount = authorRepository.count();
        log.info("Persisted authors: {}", authorsCount);
        if (authorsCount <= 0) {
            log.info("No persisted authors found - adding new data");
            Author author = new Author();
            author.setName("Falcon");
            author.setDateOfBirth(LocalDate.of(1990, 2, 23));

            Author author2 = new Author();
            author2.setName("Jack Porter");
            author2.setDateOfBirth(LocalDate.of(1890, 2, 23));

            author = authorRepository.save(author);
            author2 = authorRepository.save(author2);

            log.info("Saved author 1 : {} , saved author 2 : {}", author, author2);
        }
    }
}
