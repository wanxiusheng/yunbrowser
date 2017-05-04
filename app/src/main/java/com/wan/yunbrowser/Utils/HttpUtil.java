package com.wan.yunbrowser.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	// 创建HttpClient对象
		public static HttpClient httpClient = new DefaultHttpClient();
		public static final String BASE_URL ="http://123.207.163.219/yunBrowser/";
//			"http://219.218.108.212:8082/yunBrowser/";
		/**
		 *
		 * @param url 发送请求的URL
		 * @return 服务器响应字符串
		 * @throws Exception
		 */
		public static String getRequest(final String url)
			throws Exception
		{
			FutureTask<String> task = new FutureTask<String>(
			new Callable<String>()
			{
				@Override
				public String call() throws Exception
				{
					// 创建HttpGet对象。
					HttpGet get = new HttpGet(url);
					// 发送GET请求
					HttpResponse httpResponse = httpClient.execute(get);
					// 如果服务器成功地返回响应
					if (httpResponse.getStatusLine()
						.getStatusCode() == 200)
					{
						// 获取服务器响应字符串
						String result = EntityUtils
							.toString(httpResponse.getEntity());
						return result;
					}
					return null;
				}
			});
			new Thread(task).start();
			return task.get();
		}


		public static String postRequest(final String url
			, final Map<String ,String> rawParams)throws Exception
		{
			FutureTask<String> task = new FutureTask<String>(
			new Callable<String>()
			{
				@Override
				public String call() throws Exception
				{
					// 创建HttpPost对象。
					HttpPost post = new HttpPost(url);
					// 如果传递参数个数比较多的话可以对传递的参数进行封装
					List<NameValuePair> params =
						new ArrayList<NameValuePair>();
					for(String key : rawParams.keySet())
					{
						//封装请求参数
						params.add(new BasicNameValuePair(key 
							, rawParams.get(key)));
					}
					// 设置请求参数
					post.setEntity(new UrlEncodedFormEntity(
						params, "utf-8"));
					// 发送POST请求
					HttpResponse httpResponse = httpClient.execute(post);
					// 如果服务器成功地返回响应
					if (httpResponse.getStatusLine()
						.getStatusCode() == 200)
					{
						// 获取服务器响应字符串
						String result = EntityUtils
							.toString(httpResponse.getEntity());
						return result;
					}
					return null;
				}
			});
			new Thread(task).start();
			return task.get();
		}

}
