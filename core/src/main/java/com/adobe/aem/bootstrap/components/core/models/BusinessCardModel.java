package com.adobe.aem.bootstrap.components.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import javax.inject.Inject;

@Model(adaptables = Resource.class)
public class BusinessCardModel {

    @Inject
    @Optional
    private String title = "Placeholder";

    @Inject
    @Optional
    private String text = "Mauris ut cursus odio, eu.";

    @Inject
    @Optional
    private String button = "Placeholder";

    @Inject
    @Optional
    private String link = "#";

    public String getTitle() {
        return title;
    }

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
