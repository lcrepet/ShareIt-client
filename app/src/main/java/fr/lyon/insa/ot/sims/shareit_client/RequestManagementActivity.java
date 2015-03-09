package fr.lyon.insa.ot.sims.shareit_client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RequestManagementActivity extends Activity {

    private Button ok = null;
    private Button nok = null;
    private Button done = null;
    private Button returned = null;
    private TextView product = null;
    private long exchangeId = 0;
    private String profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_management);

        exchangeId = Long.valueOf(getIntent().getExtras().getString("id"));

        product = (TextView) findViewById(R.id.Product);
        new GetExchange().execute(Constants.uri + "/exchange/status", Constants.uri + "/exchange/" + exchangeId);

        ok = (Button) findViewById(R.id.OK);
        nok = (Button) findViewById(R.id.NOK);
        done = (Button) findViewById(R.id.Done);
        returned = (Button) findViewById(R.id.Returned);

        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Answer().execute(Constants.uri + "/exchange/" + exchangeId + "/accept");
                ok.setVisibility(View.INVISIBLE);
                nok.setVisibility(View.INVISIBLE);
                done.setVisibility(View.VISIBLE);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Answer().execute(Constants.uri + "/exchange/" + exchangeId + "/confirm");
                done.setVisibility(View.INVISIBLE);
                returned.setVisibility(View.VISIBLE);
            }
        });

        nok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Answer().execute(Constants.uri + "/exchange/" + exchangeId + "/reject");
                Utils.openOtherActivity(RequestManagementActivity.this, BorrowActivity.class);
            }
        });

        returned.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Answer().execute(Constants.uri + "/exchange/" + exchangeId + "/complete");
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

    private class GetExchange extends AsyncTask<String, Void, JSONObject> {
        private JSONArray status = null;

        @Override
        protected JSONObject doInBackground(String... message) {
            status = Request.getListRequest(message[0]);
            return Request.getRequest(message[1]);
        }

        protected void onPostExecute(JSONObject reader) {
            try {
                String currentStatus = reader.getString("status");
                //if(String.valueOf(Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE))).equals(reader.getJSONObject("lender").getLong("id"))){
                TextView o = (TextView) findViewById(R.id.Owner);
                o.setText(String.valueOf(reader.getJSONObject("lender").getLong("id")));
                if(currentStatus.equals(status.getJSONObject(0).getString("0"))) {
                        ok.setVisibility(View.VISIBLE);
                        nok.setVisibility(View.VISIBLE);
                        //returned.setVisibility(View.INVISIBLE);
                    } else if(currentStatus.equals(status.getJSONObject(1).getString("1"))) {
                        /*ok.setVisibility(View.INVISIBLE);
                        nok.setVisibility(View.INVISIBLE);*/
                        done.setVisibility(View.VISIBLE);
                    } else if(currentStatus.equals(status.getJSONObject(2).getString("2"))) {
                        /*ok.setVisibility(View.INVISIBLE);
                        nok.setVisibility(View.INVISIBLE);*/
                        returned.setVisibility(View.VISIBLE);
                    }
               // }


                Resources res = getResources();
                product.setText(res.getString(R.string.product,reader.getJSONObject("product").getString("name"),reader.getJSONObject("product").getJSONObject("category").getString("name")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class Answer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... message) {

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("returned", "true"));
           return Request.setRequest(message[0], pairs);
        }

        protected void onPostExecute(String message) {
        }
    }


}
