package fr.lyon.insa.ot.sims.shareit_client;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Louise on 23/02/2015.
 */
public class Utils extends Activity{

    public static void openOtherActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
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
            name += sharer.getString("lastname");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return name;
    }
}
