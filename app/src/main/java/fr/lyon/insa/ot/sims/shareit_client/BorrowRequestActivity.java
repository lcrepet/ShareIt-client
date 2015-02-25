package fr.lyon.insa.ot.sims.shareit_client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BorrowRequestActivity extends Activity {

    //TODO stub object to delete

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_request);

        //TODO replace by real value passed in extras
        try {
            final JSONObject fakeId = new JSONObject("{\"id\":1,\"name\":\"Perceuse\",\"description\":\"C est une super perceuse :)\",\"sharer\":{\"id\":1,\"profilePicture\":null,\"proFilePictureType\":null,\"lastname\":\"paul\",\"firstname\":\"jean\",\"age\":25,\"sex\":\"M\",\"rating\":0.0,\"postCode\":69100,\"telephone\":null},\"status\":\"disponible\",\"category\":{\"id\":1,\"name\":\"outils\"}}");

            //TODO code to keep
            TextView name = (TextView) findViewById(R.id.objectName);
            TextView owner = (TextView) findViewById(R.id.objectOwner);

            String nameToSet = fakeId.getString("name") + " (" + fakeId.getJSONObject("category").getString("name") + ")";
            String ownerToSet = fakeId.getJSONObject("sharer").getString("firstname") + " " + fakeId.getJSONObject("sharer").getString("lastname");

            name.setText(nameToSet);
            owner.setText(ownerToSet);

            final Button objectButton = (Button) findViewById(R.id.buttonBorrow);
            objectButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    try {
                        new BorrowRequest().execute(Constants.uri + "/product/" + fakeId.getString("id") + "/borrow/", fakeId.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    private class BorrowRequest extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... message) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
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
            Toast.makeText(getApplicationContext(), "Requète envoyée !", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(BorrowRequestActivity.this, ObjectActivity.class);
            startActivity(intent);
        }
    }
}
