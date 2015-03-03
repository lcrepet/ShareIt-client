package fr.lyon.insa.ot.sims.shareit_client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BorrowRequestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_request);

        new GetObject().execute(Constants.uri + "product/" + getIntent().getExtras().getString("id"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_borrow_request, menu);
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

    private class GetObject extends AsyncTask<String, Void, JSONObject> {

        @Override
        public JSONObject doInBackground(String... message) {
            return Request.getRequest(message[0]);
        }

        protected void onPostExecute(final JSONObject object){
            TextView name = (TextView) findViewById(R.id.objectName);
            TextView owner = (TextView) findViewById(R.id.objectOwner);
            try {
                String nameToSet = object.getString("name") + " (" + object.getJSONObject("category").getString("name") + ")";
                String ownerToSet = object.getJSONObject("sharer").getString("firstname") + " " + object.getJSONObject("sharer").getString("lastname");

                name.setText(nameToSet);
                owner.setText(ownerToSet);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final Button objectButton = (Button) findViewById(R.id.buttonBorrow);
            objectButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        new BorrowRequest().execute(Constants.uri + "/product/" + object.getString("id") + "/borrow/", object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    private class BorrowRequest extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... message) {
            List<NameValuePair> pairs = new ArrayList<>();
            try {
                JSONObject product = new JSONObject(message[1]);
                pairs.add(new BasicNameValuePair("lender", product.getJSONObject("sharer").getString("id")));
                pairs.add(new BasicNameValuePair("borrower",
                        String.valueOf(Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)))));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return Request.newPostRequest(message[0],pairs);
        }

        protected void onPostExecute(JSONObject reader){
            Toast.makeText(getApplicationContext(), "Requête envoyée !", Toast.LENGTH_LONG).show();

            Bundle extra = getIntent().getExtras();
            HashMap<String, String> extras = new HashMap<>();
            extras.put(Intent.EXTRA_INTENT, MainActivity.class.getCanonicalName());
            extras.put("id", String.valueOf(Long.valueOf(extra.getString("id"))));
            Utils.openOtherActivity(BorrowRequestActivity.this, ObjectActivity.class, extras);
        }
    }
}
