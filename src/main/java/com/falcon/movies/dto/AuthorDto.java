package com.falcon.movies.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents author data transfer object.
 * When we use builder pattern, with final fields, we need to inform jackson library how to deserialize object
 * passed in JSON body. In this class we provide:
 * - @JsonDeserialize to inform jackson how to construct object.
 * - @JsonProperty to inform jackson how to match fields with not matching setters.
 */
@JsonDeserialize(builder = AuthorDto.Builder.class)
public class AuthorDto {

    private final Long id;

    private final LocalDate creationDate;

    private final LocalDate updateDate;

    private final String name;

    private final LocalDate dateOfBirth;

    /**
     * Variable used only in report feature.
     * Provided by {@link com.falcon.movies.service.projection.GroupedMoviesProjection}.
     */
    private final Long moviesCount;


    public static Builder builder(String name) {
        return new Builder(name);
    }

    /**
     * Empty object is created mostly for mapstruct library.
     * @return Builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static Builder copy(AuthorDto authorDto) {
        return new Builder()
                .setId(authorDto.getId())
                .setCreationDate(authorDto.getCreationDate())
                .setUpdateDate(authorDto.getUpdateDate())
                .setDateOfBirth(authorDto.getDateOfBirth())
                .setMoviesCount(authorDto.getMoviesCount())
                .setName(authorDto.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public Long getMoviesCount() {
        return moviesCount;
    }

    public static class Builder {
        private Long id;

        private LocalDate creationDate;

        private LocalDate updateDate;

        private String name;

        private LocalDate dateOfBirth;

        private Long moviesCount;

        public Builder(String name) {
            this.name = name;
        }

        public Builder() {
        }

        @JsonProperty("id")
        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        @JsonProperty("name")
        public Builder setName(String name) {
            this.name = name;
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

        @JsonProperty("dateOfBirth")
        public Builder setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public Builder setMoviesCount(Long moviesCount) {
            this.moviesCount = moviesCount;
            return this;
        }

        public AuthorDto build() {
            return new AuthorDto(this);
        }
    }

    private AuthorDto(AuthorDto.Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.dateOfBirth = builder.dateOfBirth;
        this.creationDate = builder.creationDate;
        this.updateDate = builder.updateDate;
        this.moviesCount = builder.moviesCount;;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorDto authorDto = (AuthorDto) o;
        return Objects.equals(id, authorDto.id) && Objects.equals(creationDate, authorDto.creationDate) && Objects.equals(updateDate, authorDto.updateDate) && Objects.equals(name, authorDto.name) && Objects.equals(dateOfBirth, authorDto.dateOfBirth) && Objects.equals(moviesCount, authorDto.moviesCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, updateDate, name, dateOfBirth, moviesCount);
    }

    @Override
    public String toString() {
        return "AuthorDto{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", moviesCount=" + moviesCount +
                '}';
    }
}
