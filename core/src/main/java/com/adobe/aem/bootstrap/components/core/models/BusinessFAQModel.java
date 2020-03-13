package com.adobe.aem.bootstrap.components.core.models;

import com.adobe.aem.bootstrap.components.core.bean.FAQBusinessBean;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;

@Model(adaptables = Resource.class)
public class BusinessFAQModel {

    @Inject
    @Optional
    private Resource questions;

    private ArrayList<FAQBusinessBean> faqList = new ArrayList<>();

    @PostConstruct
    protected void init() {
        populateFAQList(questions);
    }

    private void populateFAQList(Resource questions) {
        if (questions != null && questions.hasChildren()) {
            for (Resource item : questions.getChildren()) {
                ValueMap map = item.getValueMap();

                FAQBusinessBean faq = new FAQBusinessBean();
                faq.setQuestion(getPropertyValue(map, "question"));
                faq.setAnswer(getPropertyValue(map, "answer"));

                faqList.add(faq);
            }
        }
    }

    private String getPropertyValue(final ValueMap vm, final String propertyName) {
        return vm.containsKey(propertyName) ? vm.get(propertyName, String.class) : "";
    }

    public ArrayList<FAQBusinessBean> getFaqList() {
        return faqList;
    }
}
