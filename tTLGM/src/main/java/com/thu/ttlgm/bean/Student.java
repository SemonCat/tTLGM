package com.thu.ttlgm.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by SemonCat on 2014/1/16.
 */
@Root
public class Student {


    @Element
    private String ID;
    @Element
    private String Department;
    @Element
    private String Name;

    private int Blood;

    private int Money;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    public int getBlood() {
        return Blood;
    }

    public void setBlood(int blood) {
        Blood = blood;
    }

    public int getMoney() {
        return Money;
    }

    public void setMoney(int money) {
        Money = money;
    }

}
