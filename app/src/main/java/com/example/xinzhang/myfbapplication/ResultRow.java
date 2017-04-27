package com.example.xinzhang.myfbapplication;

/**
 * Created by xinzhang on 4/16/17.
 */

public class ResultRow {
    String id;
    String proUrl;
    String name;
    String type;


    public ResultRow(String id, String proUrl, String name,String type) {
        this.id = id;
        this.proUrl = proUrl;
        this.name = name;
        this.type = type;

    }

    public String getId(){
        return id;
    }

    public String getProUrl(){
        return proUrl;
    }

    public String getName(){
        return name;
    }

    public String getType(){
        return type;
    }
}