package fr.lyon.insa.ot.sims.shareit_client;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.lyon.insa.ot.sims.shareit_client.Adapters.SearchListAdapter;

public class MainActivity extends Activity implements LocationListener {

    LocationManager lm;

	private TextView tv;
    private ListView listView;
    private SearchListAdapter adapter;

    public static final String SETTINGS = "SettingsFile";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        if(Utils.getUserId(getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)) == -1L){
            //user undefined, create account
            HashMap<String, String> extras = new HashMap<>();
            extras.put("modification", "false");
            Utils.openOtherActivity(MainActivity.this, SignUpActivity.class);
        }

        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> extras = new HashMap<>();
                extras.put(Intent.EXTRA_INTENT, MainActivity.class.getCanonicalName());
                extras.put("id", String.valueOf(listView.getAdapter().getItemId(position)));
                Utils.openOtherActivity(MainActivity.this, ObjectActivity.class, extras);
            }
        });
        SearchListAdapter adapter = null;
        try {
            adapter = new SearchListAdapter(this, new JSONArray(),
                    Utils.getUserId(getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)), true);
            listView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new InitCategories().execute();

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText postCode = (EditText) findViewById(R.id.textView);
                EditText objectName = (EditText) findViewById(R.id.nameEdit);
                Spinner spinner = (Spinner) findViewById((R.id.spinner));
                String arguments = "category=" + (spinner.getSelectedItemPosition() + 1);
                if(!objectName.getText().equals("")){
                    arguments = arguments + "&name=" + objectName.getText();
                }
                if(!postCode.getText().equals("")){
                    arguments = arguments + "&postcode=" + postCode.getText();
                }
                new SearchProducts().execute(Constants.uri + "product/?" + arguments);
            }
        });
	}

	

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);

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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        HashMap<String, String> extras = new HashMap<>();
		switch (item.getItemId()) {
		case R.id.menu_profile:

            extras.put(Intent.EXTRA_INTENT, ObjectActivity.class.getCanonicalName());
            Utils.openOtherActivity(this,
                    ProfileActivity.class, extras);
			return true;
		case R.id.menu_borrow:
            extras.put(Intent.EXTRA_INTENT, ObjectActivity.class.getCanonicalName());
            Utils.openOtherActivity(this,
                    BorrowActivity.class, extras);
			return true;
		case R.id.menu_email:
            Utils.openOtherActivity(this,
                    EmailActivity.class);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

    @Override
    protected void onResume() {
        super.onResume();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        String provider = lm.getBestProvider(criteria, true);

        lm.requestLocationUpdates(provider, 10000, 100, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lm.removeUpdates(this);
    }
    @Override
    public void onLocationChanged(Location location) {
        JSONObject position = new JSONObject();
        try {
            position.put("latitude", location.getLatitude());
            position.put("longitude", location.getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new UpdateLocation().execute(Constants.uri + "user/" +
                Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE))
                + "/location/", position.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private class SearchProducts extends AsyncTask<String, Void, JSONArray>{

        @Override
        protected JSONArray doInBackground(String... message) {
            return Request.getListRequest(message[0]);
        }

        protected void onPostExecute(JSONArray reader) {
            ListView listView = (ListView) findViewById(R.id.listView);
            try {
                SearchListAdapter adapter = (SearchListAdapter) listView.getAdapter();
                adapter.updateProducts(reader, true, Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)));
                listView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class InitCategories extends AsyncTask<String, Void, JSONArray>{

        @Override
        protected JSONArray doInBackground(String... params) {
            return Request.getListRequest(Constants.uri + "product/category");
        }

        protected void onPostExecute(JSONArray reader){
            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            List<String> list = new ArrayList<>();
            for(int i = 0; i < reader.length(); i++){
                JSONObject row = null;
                try {
                    row = reader.getJSONObject(i);
                    list.add(row.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(MainActivity.this ,android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
        }

    }

    private class UpdateLocation extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... message) {
            List<NameValuePair> pairs = new ArrayList<>();
            try {
                JSONObject position = new JSONObject(message[1]);
                pairs.add(new BasicNameValuePair("x", position.getString("latitude")));
                pairs.add(new BasicNameValuePair("y", position.getString("longitude")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return Request.setRequest(message[0], pairs);
        }
    }
}