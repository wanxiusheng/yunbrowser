package com.wan.yunbrowser.Bean;

/**
 * Created by wan on 2017/4/24.
 */

public class History {
    public History() {
        // TODO Auto-generated constructor stub
    }
    private String username;
    private String title;
    private String url;


    public History(String username, String title, String url) {
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
