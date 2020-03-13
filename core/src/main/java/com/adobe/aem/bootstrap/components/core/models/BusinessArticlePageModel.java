package com.adobe.aem.bootstrap.components.core.models;

import com.adobe.aem.bootstrap.components.core.bean.BusinessArticleBean;
import com.adobe.aem.bootstrap.components.core.helpers.BusinessArticleHelper;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.FragmentData;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.RepositoryException;
import java.util.GregorianCalendar;

@Model(adaptables = Resource.class)
public class BusinessArticlePageModel {

    @Inject
    @Optional
    private Resource fragmentPath;

    private BusinessArticleBean article;

    private BusinessArticleHelper articleHelper = new BusinessArticleHelper();

    @PostConstruct
    protected void init() throws RepositoryException {
        if (fragmentPath == null) {
            article = articleHelper.createArticlePlaceholder();
            return;
        }
        setupArticle();
    }

    private void setupArticle() throws RepositoryException {
        UserManager userManager = fragmentPath.getResourceResolver().adaptTo(UserManager.class);
        ContentFragment articleFragment = fragmentPath.adaptTo(ContentFragment.class);

        ValueMap properties = fragmentPath.adaptTo(ValueMap.class);

        article = new BusinessArticleBean();
        article.setTitle(articleFragment.getElement("article_title").getContent());
        article.setSummary(articleFragment.getElement("article_summary").getContent());
        article.setText(articleFragment.getElement("article_content").getContent());
        article.setCover(articleFragment.getElement("article_cover").getContent());

        Authorizable author = userManager.getAuthorizable(properties.get("jcr:createdBy", (String) null));
        article.setAuthor(author);

        GregorianCalendar date = properties.get("jcr:created", (GregorianCalendar) null);
        article.setDate(date);
    }

    public BusinessArticleBean getArticle() {
        return article;
    }
}
