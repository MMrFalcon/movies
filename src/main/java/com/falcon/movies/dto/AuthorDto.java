package com.falcon.movies.dto;

import java.time.LocalDate;
import java.util.Objects;

public class AuthorDto {

    private final Long id;

    private final LocalDate creationDate;

    private final LocalDate updateDate;

    private final String name;

    private final LocalDate dateOfBirth;


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

    public static class Builder {
        private Long id;

        private LocalDate creationDate;

        private LocalDate updateDate;

        private String name;

        private LocalDate dateOfBirth;

        public Builder(String name) {
            this.name = name;
        }

        public Builder() {
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Builder setCreationDate(LocalDate creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public Builder setUpdateDate(LocalDate updateDate) {
            this.updateDate = updateDate;
            return this;
        }

        public Builder setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
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
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorDto authorDto = (AuthorDto) o;
        return Objects.equals(id, authorDto.id) && Objects.equals(creationDate, authorDto.creationDate) && Objects.equals(updateDate, authorDto.updateDate) && Objects.equals(name, authorDto.name) && Objects.equals(dateOfBirth, authorDto.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, updateDate, name, dateOfBirth);
    }

    @Override
    public String toString() {
        return "AuthorDto{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
