package com.wan.yunbrowser.Utils;



import com.wan.yunbrowser.Bean.Bookmark;
import com.wan.yunbrowser.Bean.History;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class stringToJson {

	public stringToJson() {

	}

	public static List<Bookmark> getBookmarkList(String key, String jsonString)
			throws JSONException {
		List<Bookmark> bms = new ArrayList<Bookmark>();
		JSONObject jsonObject = new JSONObject(jsonString);
		JSONArray jsonArray;
		jsonArray = jsonObject.getJSONArray(key);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject2 = jsonArray.getJSONObject(i);
			Bookmark bm = new Bookmark();
			bm.setUsername(jsonObject2.getString("username"));
			bm.setTitle(jsonObject2.getString("title"));
			bm.setUrl(jsonObject2.getString("url"));
			bms.add(bm);
		}
		return bms;
	}
	public static List<History> getHistoryList(String key, String jsonString)
			throws JSONException {
		List<History> bms = new ArrayList<History>();
		JSONObject jsonObject = new JSONObject(jsonString);
		JSONArray jsonArray;
		jsonArray = jsonObject.getJSONArray(key);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject2 = jsonArray.getJSONObject(i);
			History bm = new History();
			bm.setUsername(jsonObject2.getString("username"));
			bm.setTitle(jsonObject2.getString("title"));
			bm.setUrl(jsonObject2.getString("url"));
			bms.add(bm);
		}
		return bms;
	}
}

