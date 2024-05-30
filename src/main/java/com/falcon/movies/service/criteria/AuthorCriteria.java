package com.falcon.movies.service.criteria;

import java.time.LocalDate;
import java.util.Objects;

public class AuthorCriteria {

    private Long idEquals;

    private String nameLike;

    private LocalDate dateOfBirthGreaterThanOrEqual;

    private LocalDate dateOfBirthLessThanOrEqual;

    private Boolean countMoviesWithJoin;

    private Boolean countMoviesWithoutJoin;

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

    public Boolean getCountMoviesWithJoin() {
        return countMoviesWithJoin;
    }

    public void setCountMoviesWithJoin(Boolean countMoviesWithJoin) {
        this.countMoviesWithJoin = countMoviesWithJoin;
    }

    public Boolean getCountMoviesWithoutJoin() {
        return countMoviesWithoutJoin;
    }

    public void setCountMoviesWithoutJoin(Boolean countMoviesWithoutJoin) {
        this.countMoviesWithoutJoin = countMoviesWithoutJoin;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorCriteria that = (AuthorCriteria) o;
        return Objects.equals(idEquals, that.idEquals) && Objects.equals(nameLike, that.nameLike) && Objects.equals(dateOfBirthGreaterThanOrEqual, that.dateOfBirthGreaterThanOrEqual) && Objects.equals(dateOfBirthLessThanOrEqual, that.dateOfBirthLessThanOrEqual) && Objects.equals(countMoviesWithJoin, that.countMoviesWithJoin) && Objects.equals(countMoviesWithoutJoin, that.countMoviesWithoutJoin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEquals, nameLike, dateOfBirthGreaterThanOrEqual, dateOfBirthLessThanOrEqual, countMoviesWithJoin, countMoviesWithoutJoin);
    }

    @Override
    public String toString() {
        return "AuthorCriteria{" +
                "idEquals=" + idEquals +
                ", nameLike='" + nameLike + '\'' +
                ", dateOfBirthGreaterThanOrEqual=" + dateOfBirthGreaterThanOrEqual +
                ", dateOfBirthLessThanOrEqual=" + dateOfBirthLessThanOrEqual +
                ", countMoviesWithJoin=" + countMoviesWithJoin +
                ", countMoviesWithoutJoin=" + countMoviesWithoutJoin +
                '}';
    }
}
