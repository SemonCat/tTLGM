package com.thu.ttlgm.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SemonCat on 2014/1/13.
 */
@Root
public class Class implements Serializable{

    @Element
    private String Title;
    @Element
    private Date Date;
    @Element
    private int Week;
    @Element
    private String Content;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        Date = date;
    }

    public int getWeek() {
        return Week;
    }

    public void setWeek(int week) {
        Week = week;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    @Override
    public String toString(){
        return "Class Title:"+getTitle()+" Class Date:"+
                new SimpleDateFormat("yyyy/MM/dd").format(getDate())+" Class Week:"+getWeek()+
                " Class Content:"+getContent();
    }

    @Override
    public int hashCode() {
        return Title.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return this.getWeek()==(((Class)o).getWeek());
    }
}
