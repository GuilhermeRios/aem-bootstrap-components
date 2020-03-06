package com.adobe.aem.bootstrap.components.core.models;

import com.adobe.aem.bootstrap.components.core.bean.BusinessArticleBean;
import com.adobe.cq.dam.cfm.ContentFragment;
import jdk.vm.ci.meta.Value;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class)
public class BusinessArticleListModel {

    @SlingObject
    private ResourceResolver resourceResolver;

    private List<BusinessArticleBean> articlesList = new ArrayList<>();

    private Resource resourceTemp;
    @Inject
    @Optional
    private String listPath = "#";

    @PostConstruct
    protected void init() {
        if (listPath != "#") {
            Resource resource = resourceResolver.getResource(listPath);
            resourceTemp = resource;
            if (resource.hasChildren()) {
                for (Resource item : resource.getChildren()) {
                    if (item.getResourceType().equals("nt:unstructured")) {
                        continue;
                    }

                    Resource articleResource = resourceResolver.getResource(item.getPath());
                    ContentFragment articleFragment = articleResource.adaptTo(ContentFragment.class);
                    BusinessArticleBean article = new BusinessArticleBean();
                    article.setTitle(articleFragment.getElement("article_title").getContent());
                    article.setText(articleFragment.getElement("article_content").getContent());
                    article.setDate(articleFragment.getElement("article_date").getContent());
                    article.setCover(articleFragment.getElement("article_cover").getContent());

                    articlesList.add(article);
                }
            }
        }
    }

    public String getListPath() {
        return listPath;
    }

    public List<BusinessArticleBean> getArticlesList() {
        return articlesList;
    }
}
