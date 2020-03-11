package com.adobe.aem.bootstrap.components.core.models;

import com.adobe.aem.bootstrap.components.core.bean.BusinessArticleBean;

import com.adobe.aem.bootstrap.components.core.helpers.BusinessArticleHelper;
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

    private BusinessArticleHelper articleHelper = new BusinessArticleHelper();
    private List<BusinessArticleBean> articlesList = new ArrayList<>();
    private String[] currentPageValue;
    private int currentPage = 1;
    private String searchValue;
    private long totalMatches;

    @PostConstruct
    protected void init() {
        setCurrentPage();
        setSearchValue();

        if (listPath == "#") {
            articlesList.add(articleHelper.createArticlePlaceholder());
            return;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("path", listPath);
        map.put("type", "dam:Asset");

        if (searchValue != null && !searchValue.isEmpty()) {
            map.put("fulltext", searchValue);
//            map.put("1_property", "@jcr:content/jcr:title");
//            map.put("1_property.value", "%"+searchValue+"%");
//            map.put("1_property.operation", "like");
        }

        map.put("orderby", "@jcr:created");
        map.put("orderby.sort", "desc");
        QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);
        Session session = resourceResolver.adaptTo(Session.class);
        Query query = builder.createQuery(PredicateGroup.create(map), session);
        query.setStart(getQueryStart());
        query.setHitsPerPage(itemsPerPage);
        SearchResult result = query.getResult();

        totalMatches = result.getTotalMatches();

        Iterator<Resource> resources = result.getResources();

        while (resources.hasNext()) {
            ContentFragment articleFragment = resources.next().adaptTo(ContentFragment.class);

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

    private void setSearchValue() {
        if (!request.getRequestParameterList().isEmpty()) {
            currentPageValue = request.getParameterValues("search");
            if (currentPageValue != null && currentPageValue[0] != null) {
                searchValue = currentPageValue[0];
            }
        }
    }

    public List<BusinessArticleBean> getArticlesList() {
        return articlesList;
    }

    public int getNextPage() {
        return (currentPage + 1);
    }

    public String getNextPageQuery() {
        String search = "";
        if (searchValue != null && !searchValue.isEmpty()) {
            search = "&search=" + searchValue;
        }

        return "?page=" + (currentPage + 1) + search;
    }

    public int getPreviousPage() {
        return (currentPage - 1);
    }

    public String getPreviousPageQuery() {
        String search = "";
        if (searchValue != null && !searchValue.isEmpty()) {
            search = "&search=" + searchValue;
        }

        return "?page=" + (currentPage - 1) + search;
    }

    public int getQueryStart() {
        return (currentPage - 1) * itemsPerPage;
    }

    public boolean isLastPage() {
        return totalMatches <= currentPage * itemsPerPage;
    }
}
