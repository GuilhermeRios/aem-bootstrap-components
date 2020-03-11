package com.adobe.aem.bootstrap.components.core.helpers;

import com.adobe.aem.bootstrap.components.core.bean.BusinessArticleBean;

public class BusinessArticleHelper {
    public BusinessArticleBean createArticlePlaceholder() {
        BusinessArticleBean article = new BusinessArticleBean();
        article.setTitle("Placeholder");
        article.setText("");
        article.setCover("https://via.placeholder.com/300");

        return article;
    }

}
