package com.falcon.movies.entity;

import com.falcon.movies.entity.enumeration.MovieType;

import jakarta.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Movie extends BaseEntity  {

    @NotNull
    @Column(nullable = false, unique = true)
    private String title;

    private int time;

    private LocalDate productionDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovieType movieType;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Author author;

    @Column(name = "author_id", insertable = false, updatable = false)
    private Long authorId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public MovieType getMovieType() {
        return movieType;
    }

    public void setMovieType(MovieType movieType) {
        this.movieType = movieType;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Movie movie = (Movie) o;
        return time == movie.time && Objects.equals(title, movie.title) && Objects.equals(productionDate, movie.productionDate) && movieType == movie.movieType && Objects.equals(author, movie.author) && Objects.equals(authorId, movie.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, time, productionDate, movieType, author, authorId);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", time=" + time +
                ", productionDate=" + productionDate +
                ", movieType=" + movieType +
                ", author=" + author +
                ", authorId=" + authorId +
                '}';
    }
}
