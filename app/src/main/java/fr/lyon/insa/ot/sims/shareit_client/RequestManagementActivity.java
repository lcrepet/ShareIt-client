package fr.lyon.insa.ot.sims.shareit_client;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RequestManagementActivity extends ActionBarActivity {

    private Button ok = null;
    private Button nok = null;
    private Button returned = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_management);

        ok = (Button) findViewById(R.id.OK);
        nok = (Button) findViewById(R.id.NOK);
        returned = (Button) findViewById(R.id.Returned);

        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Answer().execute(Constants.uri + "/user/" + Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)) + "/accept");
                ok.setVisibility(View.INVISIBLE);
                returned.setVisibility(View.VISIBLE);
            }
        });

        nok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Answer().execute(Constants.uri + "/user/" + Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)) + "/reject");
                Utils.openOtherActivity(RequestManagementActivity.this, BorrowActivity.class);
            }
        });

        returned.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Answer().execute(Constants.uri + "/user/" + Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)) + "/complete");
                Utils.openOtherActivity(RequestManagementActivity.this, BorrowActivity.class);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_request_management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class Answer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... message) {

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("id", Integer.toString(1)));
           return Request.setRequest(message[0], pairs);
        }

        protected void onPostExecute() {

        }
    }


}
