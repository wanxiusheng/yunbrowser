package com.wan.yunbrowser.Bean;

/**
 * Created by wan on 2017/4/24.
 */

public class User {
    public static String username="";
    public static String password="";
    public static int flag=0;//0普通用户 1：微博用户 2：微信用户
    public static String webAdd="file:///android_asset/index/index.html";
    public User(String username,String password) {
        this.username=username;
        this.password=password;
    }
    public static String getUsername() {
        return username;
    }
    public static void setUsername(String username) {
        User.username = username;
    }
    public static String getPassword() {
        return password;
    }
    public static void setPassword(String password) {
        User.password = password;
    }

}
