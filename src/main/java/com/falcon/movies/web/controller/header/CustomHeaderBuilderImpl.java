package com.falcon.movies.web.controller.header;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

public class CustomHeaderBuilderImpl implements CustomHeaderBuilder {

    private CustomHeader customHeader;

    public CustomHeaderBuilderImpl() {
        this.reset();
    }

    private void reset() {
        this.customHeader = new CustomHeader();
    }

    @Override
    public CustomHeaderBuilderImpl setResponseStatus(int status) {
        this.customHeader.setStatus(status);
        return this;
    }

    @Override
    public CustomHeaderBuilderImpl setGetMultipleHeader(int pageNumber, int pageSize) {
        this.customHeader.setPageNumber(pageNumber);
        this.customHeader.setPageSize(pageSize);
        this.customHeader.setNextPage(pageNumber + 1);
        this.customHeader.setReferenceURI(getUriByPage(pageNumber + 1, pageSize));
        return this;
    }

    @Override
    public CustomHeaderBuilderImpl setGetOneHeader() {
        this.customHeader.setReferenceURI(getCurrentUri());
        return this;
    }

    @Override
    public CustomHeaderBuilderImpl setCreateOrUpdateHeader(int affectedEntityCount) {
        this.customHeader.setAffectedEntitiesCount(affectedEntityCount);
        this.customHeader.setReferenceURI(getCurrentUri());
        return this;
    }

    @Override
    public CustomHeaderBuilderImpl setDeleteHeader(int affectedEntitiesCount) {
        final String[] uriParts = getCurrentUri().split("/");
        StringBuilder deleteResponseUrl = new StringBuilder();
        for (int i = 0; i < uriParts.length - 1; i++) {
            deleteResponseUrl.append(uriParts[i]).append("/");
        }
        this.customHeader.setAffectedEntitiesCount(affectedEntitiesCount);
        this.customHeader.setReferenceURI(deleteResponseUrl.toString());
        return this;
    }

    @Override
    public CustomHeader build() {
        setUpHttpHeaders();
        return this.customHeader;
    }

    protected void setUpHttpHeaders() {
        if (this.customHeader.getStatus() != null) {
            this.customHeader.addHttpHeader("M-RESPONSE-STATUS", this.customHeader.getStatus().toString());
        }

        if (this.customHeader.getPageSize() != null) {
            this.customHeader.addHttpHeader("M-PAGE-SIZE", this.customHeader.getPageSize().toString());
        }

        if (this.customHeader.getPageNumber() != null) {
            this.customHeader.addHttpHeader("M-PAGE-NUMBER", this.customHeader.getPageNumber().toString());
        }

        if (this.customHeader.getNextPage() != null) {
            this.customHeader.addHttpHeader("M-NEXT-PAGE-NUMBER", this.customHeader.getNextPage().toString());
        }

        if (this.customHeader.getAffectedEntitiesCount() != null) {
            this.customHeader.addHttpHeader("M-AFFECTED-ENTITIES-COUNT",  this.customHeader.getAffectedEntitiesCount().toString());
        }

        if (this.customHeader.getReferenceURI() != null) {
            this.customHeader.addHttpHeader("M-REFERENCE-URL", this.customHeader.getReferenceURI());
        }
    }

    protected String getUriByPage(int pageNumber, int pageSize) {
        final UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentRequest();
        return uriComponentsBuilder
                .replaceQueryParam("page", Integer.toString(pageNumber))
                .replaceQueryParam("size", Integer.toString(pageSize))
                .toUriString();
    }

    protected String getCurrentUri() {
        return ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
    }
}
