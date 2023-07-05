package com.jiawa.wiki.req;

public class CategoryQueryReq extends PageReq{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CategoryQueryReq{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}