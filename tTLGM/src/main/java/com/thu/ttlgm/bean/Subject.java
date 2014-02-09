package com.thu.ttlgm.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by SemonCat on 2014/1/13.
 */
@Root
public class Subject implements Serializable{

    @Element
    private String Title;

    @ElementList
    private List<Class> ClassList;

    @ElementList
    private List<Student> Students;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public List<Class> getClassList() {
        return ClassList;
    }

    public void setClassList(List<Class> classList) {
        ClassList = classList;
    }

    public List<Student> getStudents() {
        return Students;
    }

    public void setStudents(List<Student> students) {
        Students = students;
    }
}

