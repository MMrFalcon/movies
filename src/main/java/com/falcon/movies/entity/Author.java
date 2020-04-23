package com.falcon.movies.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
public class Author extends BaseEntity implements Serializable {

    @Column(nullable = false, unique = true)
    private String name;

    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Movie> movies;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Author author = (Author) o;
        return name.equals(author.name) &&
                Objects.equals(dateOfBirth, author.dateOfBirth) &&
                Objects.equals(movies, author.movies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, dateOfBirth, movies);
    }
}
