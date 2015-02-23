package fr.lyon.insa.ot.sims.shareit_client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class ProfileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		new GetProfile().execute("http://178.62.199.79:8080/shareit/user/1");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.activity_main, menu);
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

	private class GetProfile extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... message) {
			HttpClient httpclient;
			HttpGet request;
			HttpResponse response = null;
			String result = " ";

			try {
				httpclient = new DefaultHttpClient();
				request = new HttpGet(message[0]);
				response = httpclient.execute(request);
			} catch (Exception e) {
				result = "error";
			}

			try {
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
					result = result + line;
				}
			} catch (Exception e) {
				result = "error";
			}
			return result;
		}

		protected void onPostExecute(String result) {
			TextView firstName = (TextView) findViewById(R.id.FirstName);
			TextView lastName = (TextView) findViewById(R.id.LastName); 
			
			try {
				JSONObject reader = new JSONObject(result);
				
				firstName.setText(reader.getString("firstname"));
				lastName.setText(reader.getString("lastname"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}