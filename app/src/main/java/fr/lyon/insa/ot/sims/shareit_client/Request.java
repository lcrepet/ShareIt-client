package fr.lyon.insa.ot.sims.shareit_client;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
            post.setEntity(new UrlEncodedFormEntity(pairs));

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


    public static String putPicture(String url, List<NameValuePair> nameValuePairs, File picture) {
        String responseBody = "failure";
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);


        Map<String, String> map = new HashMap<String, String>();
        HttpPut put = new HttpPut(url);
        put.addHeader("Accept", "application/json");

        //MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        FileEntity req=new FileEntity(picture, "binary/octet-stream");
        //builder.setCharset(MIME.UTF8_CHARSET);


        /*
        if (picture!= null)
            builder.addBinaryBody("Filedata", picture, ContentType.MULTIPART_FORM_DATA, picture.getName());

        */
        put.setEntity(req);

        try {
            responseBody = EntityUtils.toString(client.execute(put).getEntity(), "UTF-8");


            JSONObject object = new JSONObject(responseBody);
            Boolean success = object.optBoolean("success");
            String message = object.optString("error");

            if (!success) {
                responseBody = message;
            } else {
                responseBody = "success";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }

        return"hola";

    }


}
