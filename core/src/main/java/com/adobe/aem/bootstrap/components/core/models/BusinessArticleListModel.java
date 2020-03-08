package com.adobe.aem.bootstrap.components.core.models;

import com.adobe.aem.bootstrap.components.core.bean.BusinessArticleBean;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.FragmentData;
import jdk.jfr.Name;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BusinessArticleListModel {

    @SlingObject
    private SlingHttpServletRequest request;

    @SlingObject
    private ResourceResolver resourceResolver;

    @Inject
    @Via("resource")
    private String listPath = "#";

    @Inject
    @Via("resource")
    private int itemsPerPage;

    private List<BusinessArticleBean> articlesList = new ArrayList<>();
    private List<RequestParameter> requestParameterList;

    private int page = 1;

    @PostConstruct
    protected void init() {
        requestParameterList = request.getRequestParameterList();
        if (listPath == "#") {
            createArticlePlaceholder();
            return;
        }
        Resource resource = resourceResolver.getResource(listPath);
        if (resource.hasChildren()) {
            for (Resource item : resource.getChildren()) {
                if (item.getResourceType().equals("nt:unstructured")) {
                    continue;
                }

                ContentFragment articleFragment = resourceResolver.getResource(item.getPath()).adaptTo(ContentFragment.class);

                BusinessArticleBean article = new BusinessArticleBean();
                article.setTitle(articleFragment.getElement("article_title").getContent());
                article.setText(articleFragment.getElement("article_content").getContent());
                article.setCover(articleFragment.getElement("article_cover").getContent());

                FragmentData date = articleFragment.getElement("article_date").getValue();
                article.setDate(((GregorianCalendar) date.getValue()));

                articlesList.add(article);
            }
        }
    }

    private void createArticlePlaceholder() {
        BusinessArticleBean article = new BusinessArticleBean();
        article.setTitle("Placeholder");
        article.setText("");
        article.setCover("https://via.placeholder.com/300");

        articlesList.add(article);
    }

    public List<BusinessArticleBean> getArticlesList() {
        return articlesList;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
