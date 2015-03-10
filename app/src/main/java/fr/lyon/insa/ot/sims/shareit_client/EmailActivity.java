package fr.lyon.insa.ot.sims.shareit_client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

import fr.lyon.insa.ot.sims.shareit_client.Adapters.ContactListAdapter;
import fr.lyon.insa.ot.sims.shareit_client.Adapters.SearchListAdapter;

public class EmailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email);

        final ListView contactList = (ListView) findViewById(R.id.contactList);

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> extras = new HashMap<>();
                extras.put(Intent.EXTRA_INTENT, EmailActivity.class.getCanonicalName());
                extras.put("contactId", String.valueOf(contactList.getAdapter().getItemId(position)));
                Utils.openOtherActivity(EmailActivity.this, MessageActivity.class, extras);
            }
        });

        new SearchContacts().execute(Constants.uri + "contacts/" + Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.email, menu);

        final Menu m = menu;
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.menu_profile:
                HashMap<String, String> extras = new HashMap<>();
                extras.put(Intent.EXTRA_INTENT, ObjectActivity.class.getCanonicalName());
                Utils.openOtherActivity(this,
                        ProfileActivity.class, extras);
                return true;
            case R.id.menu_borrow:
                Utils.openOtherActivity(this,
                        BorrowActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
	}

    private class SearchContacts extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... message) {
            return Request.getListRequest(message[0]);
        }

        protected void onPostExecute(JSONArray reader) {
            ListView contactList = (ListView) findViewById(R.id.contactList);
            try {
                ContactListAdapter adapter = new ContactListAdapter(EmailActivity.this, reader);
                contactList.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
