/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.adobe.aem.bootstrap.components.core.models;

import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import java.util.Optional;

@Model(adaptables = Resource.class)
public class SliderModel {

    @Optional
    @Inject
    @Via("resource")
    private List<Resource> slides;

    private List<TouchMultiFieldBean> slidesList = new ArrayList<TouchMultiFieldBean>();

    @PostConstruct
    protected void init() {
        populateSlidesList(slides);
    }

    private void populateSlidesList(List<Resource> itemsList) {
        if (itemsList != null && !itemsList.isEmpty()) {
            for (Resource item : itemsList) {
                if (item == null) {
                    continue;
                }

                TouchMultiFieldBean slideItem = new TouchMultiFieldBean();
                ValueMap vm = item.getValueMap();

                String title = getPropertyValue(vm, "title");
                String link = getPropertyValue(vm, "link");
                String flag = getPropertyValue(vm, "flag");

                slideItem.setTitle(title);
                slideItem.setLink(link);
                slideItem.setFlag(flag);
                
                slidesList.add(slideItem);
            }
        }
    }

}

    private String getPropertyValue(final ValueMap properties, final String propertyName) {
        return properties.containsKey(propertyName) ? properties.get(propertyName, String.class) : StringUtils.EMPTY;
    }

    public List<TouchMultiFieldBean> getSlidesList() {
        return this.slidesList;
    }
}
