package fr.lyon.insa.ot.sims.shareit_client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.lyon.insa.ot.sims.shareit_client.Adapters.SearchListAdapter;

public class MainActivity extends Activity {

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
            adapter = new SearchListAdapter(this, new JSONArray());
            listView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new InitCategories().execute();

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText postCode = (EditText) findViewById(R.id.textView);
                Spinner spinner = (Spinner) findViewById((R.id.spinner));
                String arguments = "category=" + (spinner.getSelectedItemPosition() + 1);
                if(!postCode.getText().equals("")){
                    arguments = arguments + "&postcode=" + postCode.getText();
                }
                new SearchProducts().execute(Constants.uri + "product/?" + arguments);
            }
        });
	}

	

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

    private class SearchProducts extends AsyncTask<String, Void, JSONArray>{

        @Override
        protected JSONArray doInBackground(String... message) {
            return Request.getListRequest(message[0]);
        }

        protected void onPostExecute(JSONArray reader) {
            ListView listView = (ListView) findViewById(R.id.listView);
            try {
                SearchListAdapter adapter = (SearchListAdapter) listView.getAdapter();
                adapter.updateProducts(reader);
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
}