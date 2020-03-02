package com.adobe.aem.bootstrap.components.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import com.adobe.aem.bootstrap.components.core.bean.SliderBusinessBean;

import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class)
public class BusinessSliderModel {

    @Inject
    @Optional
    private Resource slides;

    private ArrayList<SliderBusinessBean> slidesList = new ArrayList<SliderBusinessBean>();

    @PostConstruct
    protected void init() {
        populateSlidesList(this.slides);
    }

    private void populateSlidesList(Resource slides) {
        if (slides != null && slides.hasChildren()) {
            for (Resource item : slides.getChildren()) {

                SliderBusinessBean slideItem = new SliderBusinessBean();
                ValueMap vm = item.getValueMap();

                String title = getPropertyValue(vm, "title");
                String text = getPropertyValue(vm, "text");
                String background = getPropertyValue(vm, "backgroundRef");

                slideItem.setTitle(title);
                slideItem.setText(text);
                slideItem.setBackground(background);

                slidesList.add(slideItem);
            }
        } else {
            createPlaceholderSlider();
        }
    }

    private String getPropertyValue(final ValueMap vm, final String propertyName) {
        return vm.containsKey(propertyName) ? vm.get(propertyName, String.class) : "";
    }

    private void createPlaceholderSlider() {
        SliderBusinessBean placeholder = new SliderBusinessBean();
        placeholder.setTitle("Lorem Ipsum");
        placeholder.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        placeholder.setBackground("http://placehold.it/1900x1080");

        slidesList.add(placeholder);
    }

    public List<SliderBusinessBean> getSlidesList() {
        return this.slidesList;
    }

    public Resource getSlides() {
        return this.slides;
    }
}
