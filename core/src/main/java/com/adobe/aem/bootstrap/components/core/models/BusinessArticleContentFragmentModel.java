package com.adobe.aem.bootstrap.components.core.models;

import com.adobe.aem.bootstrap.components.core.bean.BusinessArticleBean;
import com.adobe.aem.bootstrap.components.core.helpers.BusinessArticleHelper;
import com.adobe.cq.dam.cfm.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.GregorianCalendar;
import java.util.List;

@Model(adaptables = Resource.class)
public class BusinessArticleContentFragmentModel {

    @Inject
    @Optional
    private Resource fragmentPath;

    private BusinessArticleBean article;

    private BusinessArticleHelper articleHelper = new BusinessArticleHelper();

    @PostConstruct
    protected void init() {
        if (fragmentPath == null) {
            article = articleHelper.createArticlePlaceholder();
            return;
        }
        setupArticle();
    }

    private void setupArticle() {
        ContentFragment articleFragment = fragmentPath.adaptTo(ContentFragment.class);

        article = new BusinessArticleBean();
        article.setTitle(articleFragment.getElement("article_title").getContent());
        article.setSummary(articleFragment.getElement("article_summary").getContent());
        article.setText(articleFragment.getElement("article_content").getContent());
        article.setCover(articleFragment.getElement("article_cover").getContent());

        FragmentData date = articleFragment.getElement("article_date").getValue();
        article.setDate((date.getValue(GregorianCalendar.class)));
    }

    public BusinessArticleBean getArticle() {
        return article;
    }
}
