package com.falcon.movies.dto;

import com.falcon.movies.entity.enumeration.MovieType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents movie data transfer object.
 * When we use builder pattern, with final fields, we need to inform jackson library how to deserialize object
 * passed in JSON body. In this class we provide:
 * - @JsonDeserialize to inform jackson how to construct object.
 * - @JsonProperty to inform jackson how to match fields with not matching setters.
 */
@JsonDeserialize(builder = MovieDto.Builder.class)
public class MovieDto {

    private final Long id;

    private final String title;

    private final int time;

    private final LocalDate productionDate;

    private final MovieType movieType;

    private final Long authorId;

    private final String authorName;

    private final LocalDate creationDate;

    private final LocalDate updateDate;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getTime() {
        return time;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public MovieType getMovieType() {
        return movieType;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(String title) {
        return new Builder(title);
    }

    public static class Builder {
        private Long id;

        private String title;

        private int time;

        private LocalDate productionDate;

        private MovieType movieType;

        private Long authorId;

        private String authorName;

        private LocalDate creationDate;

        private LocalDate updateDate;

        public Builder(String title) {
            this.title = title;
        }

        public Builder() {
        }

        @JsonProperty("id")
        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        @JsonProperty("title")
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        @JsonProperty("time")
        public Builder setTime(int time) {
            this.time = time;
            return this;
        }

        @JsonProperty("productionDate")
        public Builder setProductionDate(LocalDate productionDate) {
            this.productionDate = productionDate;
            return this;
        }

        @JsonProperty("movieType")
        public Builder setMovieType(MovieType movieType) {
            this.movieType = movieType;
            return this;
        }

        @JsonProperty("authorId")
        public Builder setAuthorId(Long authorId) {
            this.authorId = authorId;
            return this;
        }

        @JsonProperty("authorName")
        public Builder setAuthorName(String authorName) {
            this.authorName = authorName;
            return this;
        }

        @JsonProperty("creationDate")
        public Builder setCreationDate(LocalDate creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        @JsonProperty("updateDate")
        public Builder setUpdateDate(LocalDate updateDate) {
            this.updateDate = updateDate;
            return this;
        }

        public MovieDto build() {
            return new MovieDto(this);
        }
    }

    private MovieDto(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.time = builder.time;
        this.productionDate = builder.productionDate;
        this.movieType = builder.movieType;
        this.authorId = builder.authorId;
        this.authorName = builder.authorName;
        this.creationDate = builder.creationDate;
        this.updateDate = builder.updateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieDto movieDto = (MovieDto) o;
        return time == movieDto.time && Objects.equals(id, movieDto.id) && Objects.equals(title, movieDto.title) && Objects.equals(productionDate, movieDto.productionDate) && movieType == movieDto.movieType && Objects.equals(authorId, movieDto.authorId) && Objects.equals(authorName, movieDto.authorName) && Objects.equals(creationDate, movieDto.creationDate) && Objects.equals(updateDate, movieDto.updateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, time, productionDate, movieType, authorId, authorName, creationDate, updateDate);
    }

    @Override
    public String toString() {
        return "MovieDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", time=" + time +
                ", productionDate=" + productionDate +
                ", movieType=" + movieType +
                ", authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
