package fr.lyon.insa.ot.sims.shareit_client;

import android.app.Activity;
import android.content.Context;
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

    private TextView firstName = null;
    private TextView lastName = null;
    private TextView phoneNumber = null;
    private TextView age = null;
    private TextView sexe = null;
    private TextView rating = null;
    private Button addObject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        new GetProfile().execute(Constants.uri + "user/" + Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)));


        firstName = (TextView) findViewById(R.id.FirstName);
        lastName = (TextView) findViewById(R.id.LastName);
        phoneNumber = (TextView) findViewById(R.id.PhoneNumber);
        age = (TextView) findViewById(R.id.Age);
        sexe = (TextView) findViewById(R.id.Sex);
        rating = (TextView) findViewById(R.id.Rating);
        addObject = (Button) findViewById(R.id.AddObject);
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
            case R.id.menu_borrow:
                Utils.openOtherActivity(this,
                        BorrowActivity.class);
                return true;
            case R.id.menu_email:
                Utils.openOtherActivity(this,
                        EmailActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class GetProfile extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... message) {

            return Request.getRequest(message[0]);
        }

        protected void onPostExecute(JSONObject reader) {

            try {
                firstName.setText(reader.getString("firstname"));
                lastName.setText(reader.getString("lastname"));
                phoneNumber.setText(reader.getString("telephone"));
                age.setText(reader.getString("age") + " ans");
                sexe.setText(reader.getString("sex"));
                rating.setText("Note : " + reader.getString("rating"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}