package com.adobe.aem.bootstrap.components.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.inject.Inject;

@Model(adaptables = Resource.class)
public class BusinessCTAModel {

    @Inject
    @Optional
    private String text = "Nunc in augue ornare, posuere libero at, condimentum magna.";

    @Inject
    @Optional
    private String button = "Placeholder";

    @Inject
    @Optional
    private String link = "#";

    public String getText() {
        return text;
    }

    public String getButton() {
        return button.toUpperCase();
    }

    public String getLink() {
        return link;
    }
}
