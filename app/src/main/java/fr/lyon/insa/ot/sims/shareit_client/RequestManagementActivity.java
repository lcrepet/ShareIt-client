package fr.lyon.insa.ot.sims.shareit_client;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
    private TextView owner = null;
    private TextView status = null;
    private TextView dates = null;
    private long exchangeId = 0;
    private Resources res = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_management);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0099CC")));

        res = getResources();

        exchangeId = Long.valueOf(getIntent().getExtras().getString("id"));

        product = (TextView) findViewById(R.id.Product);
        owner = (TextView) findViewById(R.id.Owner);
        status = (TextView) findViewById(R.id.Status);
        dates = (TextView) findViewById(R.id.Dates);

        new GetExchange().execute(Constants.uri + "/exchange/status", Constants.uri + "/exchange/" + exchangeId);

        ok = (Button) findViewById(R.id.OK);
        nok = (Button) findViewById(R.id.NOK);
        done = (Button) findViewById(R.id.Done);
        returned = (Button) findViewById(R.id.Returned);

        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Answer().execute(Constants.uri + "/exchange/" + exchangeId + "/accept");
                ok.setVisibility(View.INVISIBLE);
                done.setVisibility(View.VISIBLE);
                status.setText(res.getString(R.string.status_accepted));
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Answer().execute(Constants.uri + "/exchange/" + exchangeId + "/confirm");
                done.setVisibility(View.INVISIBLE);
                nok.setVisibility(View.INVISIBLE);
                returned.setVisibility(View.VISIBLE);
                status.setText(res.getString(R.string.status_borrowed));
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
                HashMap<String, String> extras = new HashMap<>();
                extras.put(Intent.EXTRA_INTENT, BorrowActivity.class.getCanonicalName());
                extras.put("id", String.valueOf(exchangeId));
                Utils.openOtherActivity(RequestManagementActivity.this, ExchangeNotation.class, extras);
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
        private JSONArray statusList = null;

        @Override
        protected JSONObject doInBackground(String... message) {
            statusList = Request.getListRequest(message[0]);
            return Request.getRequest(message[1]);
        }

        protected void onPostExecute(JSONObject reader) {
            try {
                String currentStatus = reader.getString("status");
                if(String.valueOf(Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE))).equals(reader.getJSONObject("lender").getString("id"))){
                    if(currentStatus.equals(statusList.getJSONObject(0).getString("0"))) {
                        ok.setVisibility(View.VISIBLE);
                        nok.setVisibility(View.VISIBLE);
                    } else if(currentStatus.equals(statusList.getJSONObject(1).getString("1"))) {
                        done.setVisibility(View.VISIBLE);
                    } else if(currentStatus.equals(statusList.getJSONObject(2).getString("2"))) {
                        returned.setVisibility(View.VISIBLE);
                    }
                    owner.setText("Emprunteur : " + Utils.getUserName(reader.getJSONObject("borrower")));
                } else {
                    owner.setText("Propriétaire : " + Utils.getUserName(reader.getJSONObject("lender")));
                }

                if(currentStatus.equals(statusList.getJSONObject(0).getString("0"))) {
                    status.setText(res.getString(R.string.status_issued));
                } else if(currentStatus.equals(statusList.getJSONObject(1).getString("1"))) {
                    status.setText(res.getString(R.string.status_accepted));
                } else if(currentStatus.equals(statusList.getJSONObject(2).getString("2"))) {
                    status.setText(res.getString(R.string.status_borrowed));
                } else if(currentStatus.equals(statusList.getJSONObject(3).getString("3"))) {
                    status.setText(res.getString(R.string.status_refused));
                } else if(currentStatus.equals(statusList.getJSONObject(4).getString("4"))) {
                    status.setText(res.getString(R.string.status_completed));
                }

                product.setText(res.getString(R.string.product,reader.getJSONObject("product").getString("name"),reader.getJSONObject("product").getJSONObject("category").getString("name")));
                dates.setText(res.getString(R.string.dates, reader.getString("startDate"), reader.getString("startDate")));

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
