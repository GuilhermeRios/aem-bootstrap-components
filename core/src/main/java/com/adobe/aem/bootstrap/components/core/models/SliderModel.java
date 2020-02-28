package com.adobe.aem.bootstrap.components.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import java.util.ArrayList;
import java.util.List;

import com.adobe.aem.bootstrap.components.core.bean.SliderMultiFieldBean;

@Model(adaptables = Resource.class)
public class SliderModel {

    @Inject
    @Optional
    private Resource slides;

    private ArrayList<SliderMultiFieldBean> slidesList = new ArrayList<SliderMultiFieldBean>();

    @PostConstruct
    protected void init() {
        populateSlidesList(slides);
    }

    private void populateSlidesList(Resource slides) {
        if (slides != null && slides.hasChildren()) {
            for (Resource item : slides.getChildren()) {

                SliderMultiFieldBean slideItem = new SliderMultiFieldBean();
                ValueMap vm = item.getValueMap();

                String title = getPropertyValue(vm, "title");
                String text = getPropertyValue(vm, "text");
                String button = getPropertyValue(vm, "button");
                String link = getPropertyValue(vm, "link");

                slideItem.setTitle(title);
                slideItem.setText(text);
                slideItem.setButton(button);
                slideItem.setLink(link);

                slidesList.add(slideItem);
            }
        } else{
            createPlaceholderSlider();
        }
    }

    private String getPropertyValue(final ValueMap vm, final String propertyName) {
        return vm.containsKey(propertyName) ? vm.get(propertyName, String.class) : "";
    }

    private void createPlaceholderSlider() {
        SliderMultiFieldBean placeholder = new SliderMultiFieldBean();
        placeholder.setTitle("Lorem Ipsum");
        placeholder.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        placeholder.setButton("Link");
        placeholder.setLink("#");

        slidesList.add(placeholder);
    }

    public List<SliderMultiFieldBean> getSlidesList() {
        return this.slidesList;
    }

    public Resource getSlides() {
        return this.slides;
    }
}
