package com.example.xinzhang.myfbapplication;

/**
 * Created by xinzhang on 4/17/17.
 */

public class PostItem {
    String proUrl;
    String posterName;
    String postTime;
    String postContent;

    public PostItem(String proUrl, String posterName, String postTime, String postContent){
        this.proUrl = proUrl;
        this.posterName = posterName;
        this.postTime = postTime;
        this.postContent = postContent;
    }

    public String getProUrl(){
        return proUrl;
    }

    public String getPosterName(){
        return posterName;
    }

    public String getPostTime(){
        return postTime;
    }

    public String getPostContent(){
        return postContent;
    }

}
