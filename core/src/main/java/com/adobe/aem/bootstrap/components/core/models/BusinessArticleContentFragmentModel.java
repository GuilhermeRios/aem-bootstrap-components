package com.adobe.aem.bootstrap.components.core.models;

import com.adobe.aem.bootstrap.components.core.bean.BusinessArticleBean;
import com.adobe.cq.dam.cfm.*;
import com.adobe.granite.asset.api.AssetManager;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class)
public class BusinessArticleContentFragmentModel {

    @Inject
    @Optional
    private Resource fragmentPath;

    private BusinessArticleBean article;

    private AssetManager assetManager;

    @PostConstruct
    protected void init() {
        setupArticle();
    }

    private void setupArticle() {
        ContentFragment articleFragment = fragmentPath.adaptTo(ContentFragment.class);
        article = new BusinessArticleBean();
        article.setTitle(articleFragment.getElement("article_title").getContent());
        article.setText(articleFragment.getElement("article_content").getContent());
        article.setDate(articleFragment.getElement("article_date").getContent());
        article.setCover(articleFragment.getElement("article_cover").getContent());
    }

    public BusinessArticleBean getArticle() {
        return article;
    }
}
