package fr.lyon.insa.ot.sims.shareit_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class Request {

	public static JSONObject GetUser(String url) {
		HttpClient httpclient;
		HttpGet request;
		HttpResponse response = null;
		String result = " ";
		JSONObject reader = null;

		try {
			httpclient = new DefaultHttpClient();
			request = new HttpGet(url);
			response = httpclient.execute(request);
		} catch (Exception e) {
			result = "error";
		}

		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				result = result + line;
			}

			reader = new JSONObject(result);
		} catch (Exception e) {
			result = "error";
		}

		return reader;
	}

	public static String NewUser(String url, List<NameValuePair> pairs) {
		HttpResponse response = null;
		try {

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			post.setEntity(new UrlEncodedFormEntity(pairs));

			response = client.execute(post);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response.getStatusLine().toString();

	}
	
	public static String SetUser(String url, List<NameValuePair> pairs) {
		HttpResponse response = null;
		try {

			HttpClient client = new DefaultHttpClient();
			HttpPut put = new HttpPut(url);
			put.setEntity(new UrlEncodedFormEntity(pairs));

			response = client.execute(put);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response.getStatusLine().toString();
	}
	

}
