package fr.lyon.insa.ot.sims.shareit_client;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        new GetProfile().execute(Constants.uri + "user/1");

        Button addObject = (Button) findViewById(R.id.AddObject);
        addObject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utils.openOtherActivity(ProfileActivity.this, AddObjectActivity.class);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetProfile extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... message) {

			/*List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("firstname", "henri"));
			pairs.add(new BasicNameValuePair("lastname", "durad"));
			pairs.add(new BasicNameValuePair("postcode", "69100"));
			Request.NewUser("http://178.62.199.79:8080/shareit/user", pairs);*/

            return Request.getRequest(message[0]);
        }

        protected void onPostExecute(JSONObject reader) {
            TextView firstName = (TextView) findViewById(R.id.FirstName);
            TextView lastName = (TextView) findViewById(R.id.LastName);

            try {
                firstName.setText(reader.getString("firstname"));
                lastName.setText(reader.getString("lastname"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}