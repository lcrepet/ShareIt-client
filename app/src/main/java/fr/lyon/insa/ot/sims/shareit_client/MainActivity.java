package fr.lyon.insa.ot.sims.shareit_client;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.lyon.insa.ot.sims.shareit_client.searchList.SearchListAdapter;

public class MainActivity extends Activity {

	private TextView tv;
    private ListView listView;
    private SearchListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                intent = new Intent(MainActivity.this, ObjectActivity.class);
                intent.putExtra("id", listView.getAdapter().getItemId(position));
                startActivity(intent);
            }
        });
        SearchListAdapter adapter = null;
        try {
            adapter = new SearchListAdapter(this, new JSONArray());
            listView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText postCode = (EditText) findViewById(R.id.textView);
                new SearchProducts().execute(Constants.uri + "product/?postcode=" + postCode.getText());
            }
        });
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_home:
			// TODO : home
			return true;
		case R.id.menu_profile:
            Utils.openOtherActivity(this,
                    ProfileActivity.class);
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
}