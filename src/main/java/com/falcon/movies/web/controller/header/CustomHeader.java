package com.falcon.movies.web.controller.header;

import org.springframework.http.HttpHeaders;

public class CustomHeader {

    private Integer status;

    private Integer pageNumber;
    private Integer pageSize;
    private Integer nextPage;

    private Integer affectedEntitiesCount;

    private String referenceURI;

    private HttpHeaders httpHeaders;

    public CustomHeader() {
        this.httpHeaders = new HttpHeaders();
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public void setAffectedEntitiesCount(Integer affectedEntitiesCount) {
        this.affectedEntitiesCount = affectedEntitiesCount;
    }

    public void setReferenceURI(String referenceURI) {
        this.referenceURI = referenceURI;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public Integer getAffectedEntitiesCount() {
        return affectedEntitiesCount;
    }

    public String getReferenceURI() {
        return referenceURI;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public void addHttpHeader(String key, String value) {
        this.httpHeaders.add(key, value);
    }

    @Override
    public String toString() {
        return "CustomHeader{" +
                "status=" + status +
                ", pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", nextPage=" + nextPage +
                ", affectedEntitiesCount=" + affectedEntitiesCount +
                ", referenceURI='" + referenceURI + '\'' +
                '}';
    }
}
