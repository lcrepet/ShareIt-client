package fr.lyon.insa.ot.sims.shareit_client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.widget.TextView;

public class GetData extends AsyncTask<String, Void, String> {
	private TextView display;

	GetData(TextView view) {
		this.display = view;
	}

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
		this.display.setText(result);
	}
}
