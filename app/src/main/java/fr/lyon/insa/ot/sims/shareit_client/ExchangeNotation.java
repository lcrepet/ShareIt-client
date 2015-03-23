package fr.lyon.insa.ot.sims.shareit_client;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExchangeNotation extends Activity {

    private TextView product = null;
    private TextView lender = null;
    private TextView borrower = null;

    private long exchangeId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_notation);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#CC0000")));
        bar.setDisplayHomeAsUpEnabled(true);

        this.product = (TextView) findViewById(R.id.objectInfo);
        this.lender = (TextView) findViewById(R.id.lenderInfo);
        this.borrower = (TextView) findViewById(R.id.borrowerInfo);

        this.exchangeId = Long.parseLong(getIntent().getExtras().getString("id"));

        new GetExchange().execute(Constants.uri + "/exchange/" + exchangeId);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exchange_notation, menu);
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

    @Override
    public Intent getParentActivityIntent(){
        Intent parentIntent = getIntent();
        String className = parentIntent.getStringExtra(Intent.EXTRA_INTENT);

        try {
            return new Intent(this, Class.forName(className));
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
            return null;
        }
    }

    private class GetExchange extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... message) {
            return Request.getRequest(message[0]);
        }

        protected void onPostExecute(JSONObject reader) {
            try {
                product.setText(reader.getJSONObject("product").getString("name"));
                if(String.valueOf(Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE))).equals(reader.getJSONObject("lender").getString("id"))) {
                    borrower.setText("Emprunteur: " + Utils.getUserName(reader.getJSONObject("borrower")));
                    lender.setVisibility(View.GONE);
                } else {
                    lender.setText("Propriétaire: " + Utils.getUserName(reader.getJSONObject("lender")));
                    borrower.setVisibility(View.GONE);
                }


                Button rateButton = (Button) findViewById(R.id.validateButton);
                final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

                rateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject pairs = new JSONObject();
                        try {
                            pairs.put("user", Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)));
                            pairs.put("note", ratingBar.getRating());
                            new PostNote().execute(Constants.uri + "/exchange/" + exchangeId + "/rate/", pairs.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class PostNote extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... message) {
            List<NameValuePair> pairs = new ArrayList<>();
            try {
                JSONObject args = new JSONObject(message[1]);
                pairs.add(new BasicNameValuePair("user", args.getString("user")));
                pairs.add(new BasicNameValuePair("note", args.getString("note")));


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return Request.newPostRequest(message[0], pairs);
        }

        protected void onPostExecute(JSONObject reader) {
            Utils.openOtherActivity(ExchangeNotation.this, BorrowActivity.class);
        }

    }
}
