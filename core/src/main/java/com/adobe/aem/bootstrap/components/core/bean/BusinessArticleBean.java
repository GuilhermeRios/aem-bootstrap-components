package com.adobe.aem.bootstrap.components.core.bean;

import com.adobe.granite.asset.api.Asset;
import com.adobe.granite.asset.api.AssetManager;
import com.day.cq.commons.date.DateUtil;
import com.day.cq.commons.date.InvalidDateException;

import java.util.Calendar;
import java.util.Date;

public class BusinessArticleBean {

    private String title;
    private String text;
    private String cover;
    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
