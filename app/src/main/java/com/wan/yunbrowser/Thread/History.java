package com.wan.yunbrowser.Thread;

import com.wan.yunbrowser.Bean.User;
import com.wan.yunbrowser.Utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;


public class History implements Runnable{
    public String username;
    public String title;
    public String url;
	public History(String username,String title,String url) {
		this.username=username;
		this.title=title;
		this.url=url;
	}

	@Override
	public void run() {

		  Map<String,String> map= new HashMap<String,String>();
          map.put("username", User.username);
          map.put("title", title);
          map.put("url", url);
     
          String url= HttpUtil.BASE_URL+"saveHistory";
          try {
				HttpUtil.postRequest(url,map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}

}
