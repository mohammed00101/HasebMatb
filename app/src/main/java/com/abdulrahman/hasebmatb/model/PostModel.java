package com.abdulrahman.hasebmatb.model;

/**
 * Created by abdulrahman on 12/28/16.
 */

public class PostModel {

    private String userName,location,text,time;
    private int postId;
    public  PostModel(){
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPostText() {
        return text;
    }

    public void setPostText(String postText) {
        this.text = postText;
    }

    public String getPostTime() {
        return time;
    }

    public void setPostTime(String postTime) {
        this.time = postTime;
    }
}
