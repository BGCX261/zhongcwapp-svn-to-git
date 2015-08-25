package com.ufida.gkb.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class Network {
	private static Network instance;
	private static final String TAG = "Network";
//	private static final String HOST = "http://10.5.31.43";
	// private static final String HOST = "http://192.168.1.101";
	private static final String HOST = "http://online.chanjet.com";
	private HttpClient httpclient;

	private Network() {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 5000);
		httpclient = new DefaultHttpClient(params);
//		httpclient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
	}

	public static Network getInstance() {
		if (instance == null)
			instance = new Network();
		return instance;
	}

	public String get(String url) throws NetworkException {
		Log.d(TAG, "URL = " + HOST + url);
		HttpGet httpget = new HttpGet(HOST + url);
		try {
			HttpResponse response = httpclient.execute(httpget);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				return EntityUtils.toString(response.getEntity());
			}else{
				return null;
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			throw new NetworkException(e.getMessage());
		}
	}

	public String post(String url, Map<String, String> params)
			throws NetworkException {
		return post(HOST, url, params);
	}

	public String post(String host, String url, Map<String, String> params)
			throws NetworkException {
		Log.d(TAG, "URL = " + host + url);

		params.put("isusememcache", "false");
		params.put("mobsrc", "android"); //统计用户使用手机客户端情况（访问链接次数）
		
		Log.d(TAG, "Params = " + params.toString());
		HttpPost httppost = new HttpPost(host + url);
//		httppost.getParams().setParameter("http.protocol.content-charset", "UTF-8");
		httppost.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			for (Entry<String, String> entry : params.entrySet())
				nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse response = httpclient.execute(httppost);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
//				Log.d(TAG, "Response = " + EntityUtils.toString(response.getEntity()) );
				return EntityUtils.toString(response.getEntity());
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
			throw new NetworkException(e.getMessage());
		}

	}

	public InputStream getStream(String host, String url)
			throws NetworkException {
		Log.d(TAG, "URL = " + host + url);
		HttpGet httpget = new HttpGet(host + url);
		try {
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			return entity.getContent();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			throw new NetworkException(e.getMessage());
		}
	}

}
