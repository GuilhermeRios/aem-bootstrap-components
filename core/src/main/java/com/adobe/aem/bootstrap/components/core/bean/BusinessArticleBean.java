package com.adobe.aem.bootstrap.components.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

//@JsonIgnoreProperties({"date"})
public class BusinessArticleBean {

    private String title;
    private String summary;
    private String text;
    private String cover;
    private GregorianCalendar date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

//    @JsonIgnore
    public String getDateFormated() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.YYYY");
        dateFormatter.setCalendar(this.date);
        return dateFormatter.format(this.date.getTime());
    }
}
