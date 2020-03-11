package com.adobe.aem.bootstrap.components.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class)
public class BusinessArticleSearchModel {

    @SlingObject
    private SlingHttpServletRequest request;

    private String searchValue = "";

    @PostConstruct
    protected void init() {
        setSearchValue();
    }

    private void setSearchValue() {
        if (!request.getRequestParameterList().isEmpty()) {
            String[] currentPageValue = request.getParameterValues("search");
            if (currentPageValue != null && currentPageValue[0] != null) {
                searchValue = currentPageValue[0];
            }
        }
    }

    public String getSearchValue() {
        return searchValue;
    }
}