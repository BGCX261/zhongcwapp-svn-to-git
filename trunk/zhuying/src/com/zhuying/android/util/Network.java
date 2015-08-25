package com.zhuying.android.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	private static final String HOST = "http://www.yewuben.com/openapi/MobiService";
	private HttpClient httpclient;

	private Network() {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 5000);
		httpclient = new DefaultHttpClient(params);
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
		
		Log.d(TAG, "Params = " + params.toString());
		HttpPost httppost = new HttpPost(host + url);
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
	
	/**
	 * 自动更新
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String getContent(String url) throws Exception{
	    StringBuilder sb = new StringBuilder();
	    HttpResponse response = httpclient.execute(new HttpGet(url));
	    HttpEntity entity = response.getEntity();
	    if (entity != null) {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"), 8192);
	        String line = null;
	        while ((line = reader.readLine())!= null){
	            sb.append(line + "\n");
	        }
	        reader.close();
	    }
	    return sb.toString();
	} 
}
