package fr.lyon.insa.ot.sims.shareit_client;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AddObjectActivity extends Activity {

    private EditText name = null;
    private EditText description = null;
    private Button save = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_object);

        name = (EditText) findViewById(R.id.NameObject);
        description = (EditText) findViewById(R.id.DescObject);
        save = (Button) findViewById(R.id.SaveObject);

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new SaveObject().execute(Constants.uri + "/user/1/product");
            }
        });
    }

    private class SaveObject extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... message) {

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("name", name.getText().toString()));
			pairs.add(new BasicNameValuePair("category", "3"));
			pairs.add(new BasicNameValuePair("description", description.getText().toString()));
			Request.newPostRequest(Constants.uri, pairs);

            return Request.newPostRequest(message[0], pairs);
        }

        protected void onPostExecute() {
            Toast.makeText(getApplicationContext(),"Objet créé !", Toast.LENGTH_LONG).show();

        }
    }

    private class GetCategories extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... message) {

            return Request.getListRequest(message[0]);
        }

        protected void onPostExecute() {
            Toast.makeText(getApplicationContext(),"Objet créé !", Toast.LENGTH_LONG).show();

        }
    }

}
