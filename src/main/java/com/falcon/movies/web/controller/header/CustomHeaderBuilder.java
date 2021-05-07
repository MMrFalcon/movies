package com.falcon.movies.web.controller.header;

public interface CustomHeaderBuilder {
    CustomHeaderBuilder setResponseStatus(int status);
    CustomHeaderBuilder setGetMultipleHeader(int pageNumber, int pageSize);
    CustomHeaderBuilder setGetOneHeader();
    CustomHeaderBuilder setCreateOrUpdateHeader(int affectedEntityCount);
    CustomHeaderBuilder setDeleteHeader(int affectedEntitiesCount);
    CustomHeader build();
}
