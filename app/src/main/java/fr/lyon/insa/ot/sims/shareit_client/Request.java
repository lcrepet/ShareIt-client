package fr.lyon.insa.ot.sims.shareit_client;

import android.os.Message;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {

    public static JSONObject getRequest(String url) {
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

    public static JSONArray getListRequest(String url) {
        HttpClient httpclient;
        HttpGet request;
        HttpResponse response = null;
        String result = " ";
        JSONArray reader = null;

        try {
            httpclient = new DefaultHttpClient();
            request = new HttpGet(url);
            response = httpclient.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                result = result + line;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        try {
            reader = new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reader;
    }

    public static JSONObject newPostRequest(String url, List<NameValuePair> pairs) {
        HttpResponse response = null;
        String result = " ";
        JSONObject reader = null;
        try {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            post.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));

            response = client.execute(post);

        } catch (UnsupportedEncodingException e) {
            result += e.getMessage();
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            result += e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            result += e.getMessage();
            e.printStackTrace();
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
            result += e.getMessage();
        }

        return reader;

    }

    public static String setRequest(String url, List<NameValuePair> pairs) {
        HttpResponse response = null;
        try {

            HttpClient client = new DefaultHttpClient();
            url += "?";
            int i = 0;
            for (NameValuePair n : pairs) {
                if (i > 0) url += "&";
                url += n.getName() + "=" + n.getValue();
                i++;
            }

            HttpPut put = new HttpPut(url);
            //put.setEntity(new UrlEncodedFormEntity(pairs));

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


    public static String deleteRequest(String url) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpDelete delete = new HttpDelete(url);
        HttpResponse response = client.execute(delete);
        return response.getStatusLine().toString();
    }
}
