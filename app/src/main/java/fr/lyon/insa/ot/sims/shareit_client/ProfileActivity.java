package fr.lyon.insa.ot.sims.shareit_client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.lyon.insa.ot.sims.shareit_client.Adapters.SearchListAdapter;

public class ProfileActivity extends Activity {

    private TextView firstName = null;
    private TextView lastName = null;
    private TextView phoneNumber = null;
    private TextView age = null;
    private TextView sexe = null;
    private TextView rating = null;
    private ListView listProducts = null;
    private Button addObject = null;
    private Button changeProfile = null;
    private String profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        firstName = (TextView) findViewById(R.id.FirstName);
        lastName = (TextView) findViewById(R.id.LastName);
        phoneNumber = (TextView) findViewById(R.id.PhoneNumber);
        age = (TextView) findViewById(R.id.Age);
        sexe = (TextView) findViewById(R.id.Sex);
        rating = (TextView) findViewById(R.id.Rating);
        listProducts = (ListView) findViewById(R.id.ListProducts);
        addObject = (Button) findViewById(R.id.AddObject);
        changeProfile = (Button) findViewById(R.id.EditProfile);

        try{
            profileId = getIntent().getExtras().getString("userId");
        } catch(Exception e) {
            profileId = String.valueOf(Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)));
        }

        if(profileId == null) {
            profileId = String.valueOf(Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)));
        }

        new GetProfile().execute(Constants.uri + "user/" + profileId);

        SearchListAdapter adapter = null;
        try {
            adapter = new SearchListAdapter(this, new JSONArray());
            listProducts.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new GetProducts().execute(Constants.uri + "user/" + profileId + "/product");

        addObject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utils.openOtherActivity(ProfileActivity.this, AddObjectActivity.class);
            }
        });

        changeProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HashMap<String, String> extras = new HashMap<>();
                extras.put("modification", "true");
                Utils.openOtherActivity(ProfileActivity.this, SignUpActivity.class, extras);
            }
        });

        listProducts.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> extras = new HashMap<>();
                extras.put(Intent.EXTRA_INTENT, ProfileActivity.class.getCanonicalName());
                extras.put("id", String.valueOf(listProducts.getAdapter().getItemId(position)));
                Utils.openOtherActivity(ProfileActivity.this, ObjectActivity.class, extras);
            }
        });

        ImageButton messageButton = (ImageButton) findViewById(R.id.SendMail);
        if(String.valueOf(Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE))).equals(profileId)){
            messageButton.setVisibility(View.INVISIBLE);
        } else {
            addObject.setVisibility(View.INVISIBLE);
            changeProfile.setVisibility(View.INVISIBLE);
        }

        messageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                HashMap<String, String> extras = new HashMap<>();
                extras.put(Intent.EXTRA_INTENT, ProfileActivity.class.getCanonicalName());
                extras.put("contactId", profileId);
                Utils.openOtherActivity(ProfileActivity.this, MessageActivity.class, extras);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.profile, menu);
        final Menu m = menu;

        Utils.CheckExchanges badge = new Utils.CheckExchanges(menu);
        badge.execute(String.valueOf(Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE))));

        final MenuItem borrowItem = menu.findItem(R.id.menu_borrow);
        borrowItem.getActionView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                m.performIdentifierAction(borrowItem.getItemId(), 0);
            }
        });
        return super.onCreateOptionsMenu(menu);
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

    private class GetProducts extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... message) {

            return Request.getListRequest(message[0]);
        }

        protected void onPostExecute(JSONArray reader) {
            try {
                SearchListAdapter adapter = (SearchListAdapter) listProducts.getAdapter();
                adapter.updateProducts(reader);
                listProducts.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}