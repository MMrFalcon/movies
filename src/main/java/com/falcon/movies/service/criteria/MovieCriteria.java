package com.falcon.movies.service.criteria;

import com.falcon.movies.entity.enumeration.MovieType;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class MovieCriteria implements Serializable {

    private Long idEquals;

    private String authorNameLike;

    private String titleLike;

    private List<String> movieTypeIn;

    private List<MovieType> movieTypeEnumIn;

    public Long getIdEquals() {
        return idEquals;
    }

    public void setIdEquals(Long idEquals) {
        this.idEquals = idEquals;
    }

    public String getAuthorNameLike() {
        return authorNameLike;
    }

    public void setAuthorNameLike(String authorNameLike) {
        this.authorNameLike = authorNameLike;
    }

    public String getTitleLike() {
        return titleLike;
    }

    public void setTitleLike(String titleLike) {
        this.titleLike = titleLike;
    }

    public List<String> getMovieTypeIn() {
        return movieTypeIn;
    }

    public void setMovieTypeIn(List<String> movieTypeIn) {
        this.movieTypeIn = movieTypeIn;
    }

    public List<MovieType> getMovieTypeEnumIn() {
        return movieTypeEnumIn;
    }

    public void setMovieTypeEnumIn(List<MovieType> movieTypeEnumIn) {
        this.movieTypeEnumIn = movieTypeEnumIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieCriteria that = (MovieCriteria) o;
        return Objects.equals(idEquals, that.idEquals) && Objects.equals(authorNameLike, that.authorNameLike) && Objects.equals(titleLike, that.titleLike) && Objects.equals(movieTypeIn, that.movieTypeIn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEquals, authorNameLike, titleLike, movieTypeIn);
    }

    @Override
    public String toString() {
        return "MovieCriteria{" +
                "idEquals=" + idEquals +
                ", authorNameLike='" + authorNameLike + '\'' +
                ", titleLike='" + titleLike + '\'' +
                ", movieTypeIn=" + movieTypeIn +
                '}';
    }
}
