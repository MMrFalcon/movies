package com.falcon.movies.service.criteria;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class AuthorCriteria implements Serializable {

    private Long idEquals;

    private String nameLike;

    private LocalDate dateOfBirthGreaterThanOrEqual;

    private LocalDate dateOfBirthLessThanOrEqual;

    private Boolean countMovies;

    public Long getIdEquals() {
        return idEquals;
    }

    public void setIdEquals(Long idEquals) {
        this.idEquals = idEquals;
    }

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    public LocalDate getDateOfBirthGreaterThanOrEqual() {
        return dateOfBirthGreaterThanOrEqual;
    }

    public void setDateOfBirthGreaterThanOrEqual(LocalDate dateOfBirthGreaterThanOrEqual) {
        this.dateOfBirthGreaterThanOrEqual = dateOfBirthGreaterThanOrEqual;
    }

    public LocalDate getDateOfBirthLessThanOrEqual() {
        return dateOfBirthLessThanOrEqual;
    }

    public void setDateOfBirthLessThanOrEqual(LocalDate dateOfBirthLessThanOrEqual) {
        this.dateOfBirthLessThanOrEqual = dateOfBirthLessThanOrEqual;
    }

    public Boolean getCountMovies() {
        return countMovies;
    }

    public void setCountMovies(Boolean countMovies) {
        this.countMovies = countMovies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorCriteria that = (AuthorCriteria) o;
        return Objects.equals(idEquals, that.idEquals) && Objects.equals(nameLike, that.nameLike) && Objects.equals(dateOfBirthGreaterThanOrEqual, that.dateOfBirthGreaterThanOrEqual) && Objects.equals(dateOfBirthLessThanOrEqual, that.dateOfBirthLessThanOrEqual) && Objects.equals(countMovies, that.countMovies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEquals, nameLike, dateOfBirthGreaterThanOrEqual, dateOfBirthLessThanOrEqual, countMovies);
    }

    @Override
    public String toString() {
        return "AuthorCriteria{" +
                "idEquals=" + idEquals +
                ", nameLike='" + nameLike + '\'' +
                ", dateOfBirthGreaterThanOrEqual=" + dateOfBirthGreaterThanOrEqual +
                ", dateOfBirthLessThanOrEqual=" + dateOfBirthLessThanOrEqual +
                ", countMovies=" + countMovies +
                '}';
    }
}
