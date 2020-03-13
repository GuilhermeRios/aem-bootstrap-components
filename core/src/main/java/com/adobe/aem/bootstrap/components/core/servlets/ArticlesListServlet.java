package com.adobe.aem.bootstrap.components.core.servlets;

import com.adobe.aem.bootstrap.components.core.bean.BusinessArticleBean;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.FragmentData;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

@Component(
        service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Articles List Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/libs/api/articles/list",
        }
)
public class ArticlesListServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;

    private static final String articlesPath = "/content/dam/bootstrap-components/articles/en";

    private List<BusinessArticleBean> articlesList;

    @Override
    protected void doPost(final SlingHttpServletRequest req,
                          final SlingHttpServletResponse resp) throws ServletException, IOException {

        ResourceResolver resourceResolver = req.getResourceResolver();
//        List<RequestParameter> parameterList = req.getRequestParameterList();

//        @TODO READ JSON BODY
//        StringBuilder stringBuilder = new StringBuilder();
//        BufferedReader reqReader= req.getReader();
//        String line;
//        while((line = reqReader.readLine()) != null){
//            stringBuilder.append(line);
//        }
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject = HTTP.toJSONObject(stringBuilder.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        Map<String, String> map = new HashMap<>();
        map.put("path", articlesPath);
        map.put("type", "dam:Asset");
        if (!req.getRequestParameterList().isEmpty()) {
            String searchValue;
            if ((searchValue = req.getParameter("search")) != null) {
                map.put("fulltext", searchValue);
            }
        }
        map.put("orderby", getOrderBy(req.getParameter("orderby")));
        map.put("orderby.sort", getOrder(req.getParameter("order")));

        QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);
        Session session = resourceResolver.adaptTo(Session.class);
        Query query = builder.createQuery(PredicateGroup.create(map), session);
        SearchResult result = query.getResult();

        Iterator<Resource> resources = result.getResources();

        articlesList = new ArrayList<>();
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

        JSONObject jsonResponse = new JSONObject();
        try {
            jsonResponse.put("success", true);
            jsonResponse.put("articles", articlesList);

            resp.setContentType("application/json");
            resp.getWriter().write(jsonResponse.toString(2));
        } catch (JSONException e) {
            resp.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        }
    }

    private String getOrderBy(String orderBy) {
        if (orderBy == null) {
            return "@jcr:created";
        }

        switch (orderBy) {
            case "date":
                return "@jcr:created";
            case "title":
                return "@jcr:title";
            default:
                return "@jcr:created";
        }
    }

    private String getOrder(String order) {
        if (order == null) {
            return "asc";
        }

        switch (order) {
            case "asc":
                return "asc";
            case "desc":
                return "desc";
            default:
                return "asc";
        }
    }
}
