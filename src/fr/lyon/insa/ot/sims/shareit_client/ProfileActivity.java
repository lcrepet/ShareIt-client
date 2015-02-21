package fr.lyon.insa.ot.sims.shareit_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		new GetProfile().execute("http://178.62.199.79:8080/shareit/user/1");
		new NewUser().execute("http://178.62.199.79:8080/shareit/user");
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
			EditText firstName = (EditText) findViewById(R.id.FirstName);
			EditText lastName = (EditText) findViewById(R.id.LastName);

			try {
				JSONObject reader = new JSONObject(result);

				firstName.setHint(reader.getString("firstname"));
				lastName.setHint(reader.getString("lastname"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private class NewUser extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... message) {
			HttpResponse response = null;
			try {

				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(message[0]);
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("firstname", "henri"));
				pairs.add(new BasicNameValuePair("lastname", "durad"));
				pairs.add(new BasicNameValuePair("postcode", "69100"));
				post.setEntity(new UrlEncodedFormEntity(pairs));

				response = client.execute(post);
			
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return response.getStatusLine().toString();
		}

		protected void onPostExecute(String result) {
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
					.show();
		}
	}

}