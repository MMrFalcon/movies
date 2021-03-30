package com.falcon.movies.web.controller;

import com.falcon.movies.dto.MovieDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

public abstract class ResponseHeaderGenerator {

    public static HttpHeaders createGetResponseHeaders(Page<?> page) {
        final int pageNumber = page.getNumber();
        final int pageSize = page.getSize();
        final int nextPage = pageNumber + 1;

        int prevPage = pageNumber - 1;
        if (prevPage < 0) prevPage = 0;

        HttpHeaders headers = new HttpHeaders();
        headers.add("M-Page-Size", String.valueOf(pageSize));
        headers.add("M-Page-Count", String.valueOf(page.getTotalPages()));
        headers.add("M-Page-Number", String.valueOf(pageNumber));
        headers.add("M-Total-Count", String.valueOf(page.getTotalElements()));
        headers.add("M-Next-Page", getUriByPage(nextPage, pageSize));
        headers.add("M-Prev-Page", getUriByPage(prevPage, pageSize));
        return headers;
    }

    public static String getUriByPage(int pageNumber, int pageSize) {
        final UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentRequest();
        return uriComponentsBuilder
                .replaceQueryParam("page", Integer.toString(pageNumber))
                .replaceQueryParam("size", Integer.toString(pageSize))
                .toUriString();
    }
}
