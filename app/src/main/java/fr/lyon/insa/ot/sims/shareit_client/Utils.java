package fr.lyon.insa.ot.sims.shareit_client;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import fr.lyon.insa.ot.sims.shareit_client.Adapters.ContactListAdapter;

/**
 * Created by Louise on 23/02/2015.
 */
public class Utils extends Activity{

    public static void openOtherActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    public static void openOtherActivity(Context context, Class<?> cls, HashMap<String, String> extras){
        Intent intent = new Intent(context, cls);
        Iterator it = extras.entrySet().iterator();
        while(it.hasNext()){
            HashMap.Entry<String, String> pair = (HashMap.Entry<String, String>) it.next();
            intent.putExtra(pair.getKey(), pair.getValue());
        }

        context.startActivity(intent);
    }

    public static void setUserId(SharedPreferences settings, long id){
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putString("userId", String.valueOf(id));
        editor.commit();
    }

    public static long getUserId(SharedPreferences settings){
        return Long.parseLong(settings.getString("userId", "-1"));
    }

    public static String getUserName(JSONObject sharer){
        String name = null;

        try{
            name = sharer.getString("firstname");
            name += " " + sharer.getString("lastname");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return name;
    }

    public static String getExchangeStatus(String status, Resources resources) {
        switch (status) {
            case "accepted": return resources.getString(R.string.status_accepted);
            case "borrowed": return resources.getString(R.string.status_borrowed);
            case "completed": return resources.getString(R.string.status_completed);
            case "issued": return resources.getString(R.string.status_issued);
            case "refused": return resources.getString(R.string.status_refused);
        }
        return null;
    }

    public static class CheckExchanges extends AsyncTask<String, Void, Integer> {

        final Menu menu;

        CheckExchanges(Menu menu){
            this.menu = menu;
        }

        @Override
        protected Integer doInBackground(String... message) {
            HttpResponse response = null;
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(Constants.uri + "exchange/" +
                    message[0] + "/awaiting");
            int result = 0;

            try {
                response = client.execute(get);
                if ( response.getStatusLine().getStatusCode()==200 ) {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(
                            response.getEntity().getContent()));
                    result = Integer.parseInt(rd.readLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        protected void onPostExecute(Integer nb) {
            RelativeLayout borrowItem = (RelativeLayout) this.menu.findItem(R.id.menu_borrow).getActionView();
            TextView count = (TextView) borrowItem.findViewById(R.id.countText);
            ImageView badge = (ImageView) borrowItem.findViewById(R.id.badge_borrow);

            if(nb != 0){
                count.setVisibility(View.VISIBLE);
                badge.setVisibility(View.VISIBLE);
                count.setText(String.valueOf(nb));
            }
        }
    }

    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
