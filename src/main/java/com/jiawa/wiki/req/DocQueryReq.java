package com.jiawa.wiki.req;

public class DocQueryReq extends PageReq{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DocQueryReq{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}