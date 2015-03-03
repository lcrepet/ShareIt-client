package fr.lyon.insa.ot.sims.shareit_client;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class BorrowActivity extends Activity {

    private long id;
    private  String TAG_ID = "id";
    private  String TAG_NAME = "name";

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_borrow);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        id = Long.valueOf(extras.getString("id"));
        TAG_ID = String.valueOf(id);

        new DisplayBorrowedObjects().execute();
        new DisplayLendedObjects().execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.borrow, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.menu_profile:
                HashMap<String, String> extras = new HashMap<>();
                extras.put(Intent.EXTRA_INTENT, ObjectActivity.class.getCanonicalName());
                Utils.openOtherActivity(this,
                        ProfileActivity.class, extras);
                return true;
            case R.id.menu_email:
                Utils.openOtherActivity(this,
                        EmailActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
	}

    private class DisplayBorrowedObjects extends AsyncTask<String, Void, JSONObject>{

        @Override
        public JSONObject doInBackground(String... message) {
            return Request.getRequest(Constants.uri + "user/{id}/borrowed" + TAG_ID);
        }
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        protected void onPostExecute(JSONObject reader) {
            TextView nomEmprunte = (TextView) findViewById(R.id.NomObjetEmprunte);
            try {
                String nameToSet = reader.getString(TAG_NAME);
                nomEmprunte.setText(nameToSet);
                getActionBar().setTitle(nameToSet);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class DisplayLendedObjects extends AsyncTask<String, Void, JSONObject> {

        @Override
        public JSONObject doInBackground(String... message) {
            return Request.getRequest(Constants.uri + "user/{id}/lended" + TAG_ID);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        protected void onPostExecute(JSONObject reader) {
            TextView nomPrete = (TextView) findViewById(R.id.NomObjetPrete);
            try {
                String nameToSet = reader.getString(TAG_NAME);
                nomPrete.setText(nameToSet);
                getActionBar().setTitle(nameToSet);


            }catch(JSONException e) {
                e.printStackTrace();

            }
        }
    }
}
