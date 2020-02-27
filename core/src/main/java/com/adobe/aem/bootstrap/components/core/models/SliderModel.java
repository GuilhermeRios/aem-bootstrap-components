package com.adobe.aem.bootstrap.components.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.aem.bootstrap.components.core.bean.SliderMultiFieldBean;

import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class)
public class SliderModel {

    @Inject
    private Resource slides;

//    private List<SliderMultiFieldBean> slidesList = new ArrayList<SliderMultiFieldBean>();
//
//    @PostConstruct
//    protected void init() {
//        populateSlidesList(slides);
//    }
//
//    private void populateSlidesList(List<Resource> itemsList) {
//        if (itemsList != null && !itemsList.isEmpty()) {
//            for (Resource item : itemsList) {
//                if (item == null) {
//                    continue;
//                }
//
//                SliderMultiFieldBean slideItem = new SliderMultiFieldBean();
//                ValueMap vm = item.getValueMap();
//
//                String title = getPropertyValue(vm, "title");
//                String text = getPropertyValue(vm, "text");
//                String button = getPropertyValue(vm, "button");
//                String link = getPropertyValue(vm, "link");
//
//                slideItem.setTitle(title);
//                slideItem.setText(text);
//                slideItem.setButton(button);
//                slideItem.setLink(link);
//
//                slidesList.add(slideItem);
//            }
//        }
//    }
//
//    private String getPropertyValue(final ValueMap vm, final String propertyName) {
//        return vm.containsKey(propertyName) ? vm.get(propertyName, String.class) : "";
//    }

    public Resource getSlides() {
        return this.slides;
    }
}
