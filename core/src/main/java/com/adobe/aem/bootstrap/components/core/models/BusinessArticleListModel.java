package com.adobe.aem.bootstrap.components.core.models;

import com.adobe.aem.bootstrap.components.core.bean.BusinessArticleBean;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.FragmentData;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Session;

import java.util.*;

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
    private int itemsPerPage = 5;

    private List<BusinessArticleBean> articlesList = new ArrayList<>();

    private int currentPage = 1;
    private String[] currentPageValue;

    @PostConstruct
    protected void init() {
        setCurrentPage();

        if (listPath == "#") {
            createArticlePlaceholder();
            return;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("path", listPath);
        map.put("type", "dam:Asset");
        QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);
        Session session = resourceResolver.adaptTo(Session.class);
        Query query = builder.createQuery(PredicateGroup.create(map), session);
        query.setStart(getQueryStart());
        query.setHitsPerPage(itemsPerPage);
        SearchResult result = query.getResult();

        Iterator<Resource> resources = result.getResources();

        while (resources.hasNext()) {
            ContentFragment articleFragment = resourceResolver.getResource(resources.next().getPath()).adaptTo(ContentFragment.class);

            BusinessArticleBean article = new BusinessArticleBean();
            article.setTitle(articleFragment.getElement("article_title").getContent());
            article.setSummary(articleFragment.getElement("article_summary").getContent());
            article.setText(articleFragment.getElement("article_content").getContent());
            article.setCover(articleFragment.getElement("article_cover").getContent());

            FragmentData date = articleFragment.getElement("article_date").getValue();
            article.setDate(((GregorianCalendar) date.getValue()));

            articlesList.add(article);
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

    private void setCurrentPage() {
        if (!request.getRequestParameterList().isEmpty()) {
            currentPageValue = request.getParameterValues("page");
            if (currentPageValue != null && currentPageValue[0] != null) {
                currentPage = Integer.parseInt(currentPageValue[0]);
                return;
            }
        }
        currentPage = 1;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getNextPage() {
        return currentPage + 1;
    }

    private int getQueryStart() {
        return (currentPage * itemsPerPage) - 1;
    }
}
