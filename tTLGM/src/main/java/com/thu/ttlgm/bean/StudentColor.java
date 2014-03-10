package com.thu.ttlgm.bean;

/**
 * Created by SemonCat on 2014/3/7.
 */
public class StudentColor extends Student{
    private int color;

    public StudentColor(Student mStudent) {
        setName(mStudent.getName());
        setImageUrl(mStudent.getImageUrl());
        setLogin(mStudent.IsLogin());
        setBlood(mStudent.getBlood());
        setDepartment(mStudent.getDepartment());
        setID(mStudent.getID());
        setMoney(mStudent.getMoney());
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
