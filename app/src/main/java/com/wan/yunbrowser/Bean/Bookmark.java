package com.wan.yunbrowser.Bean;

/**
 * Created by wan on 2017/4/24.
 */

public class Bookmark {
    private String username;
    private String title;
    private String url;
    public Bookmark() {
        // TODO Auto-generated constructor stub
    }

    public Bookmark(String username, String title, String url) {
        this.username = username;
        this.title = title;
        this.url = url;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
