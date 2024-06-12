package com.falcon.movies.entity.enumeration;

public enum MovieType {

    COMEDY("COMEDY"),
    HORROR("HORROR"),
    THRILLER("THRILLER"),
    DRAMA("DRAMA"),
    NONE("NONE");

    private final String value;

    MovieType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }
}
