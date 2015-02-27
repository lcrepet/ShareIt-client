package fr.lyon.insa.ot.sims.shareit_client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.List;


public class AddObjectActivity extends Activity {

    private EditText name = null;
    private EditText description = null;
    private Button save = null;
    private Spinner categories = null;
    private ListView list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_object);

        name = (EditText) findViewById(R.id.NameObject);
        description = (EditText) findViewById(R.id.DescObject);
        save = (Button) findViewById(R.id.SaveObject);
        categories = (Spinner) findViewById(R.id.TypeObject);

        new GetCategories().execute(Constants.uri + "/product/category");

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new SaveObject().execute(Constants.uri + "/user/" + Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)) + "/product");
            }
        });
    }

    private class SaveObject extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... message) {

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("name", name.getText().toString()));
			pairs.add(new BasicNameValuePair("category", Integer.toString(categories.getSelectedItemPosition() + 1)));
			pairs.add(new BasicNameValuePair("description", description.getText().toString()));

            return Request.newPostRequest(message[0], pairs);
        }

        protected void onPostExecute(JSONObject object) {
            Toast.makeText(AddObjectActivity.this,"Objet créé !", Toast.LENGTH_LONG).show();

            Intent intent;
            intent = new Intent(AddObjectActivity.this, ObjectActivity.class);
            intent.putExtra(Intent.EXTRA_INTENT, ProfileActivity.class.getCanonicalName());
            try {
                intent.putExtra("id", Long.parseLong(object.getString("id")));

                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class GetCategories extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... message) {

            return Request.getListRequest(message[0]);
        }

        protected void onPostExecute(JSONArray reader) {
            List<String> list = new ArrayList<String>();

            for(int i = 0;i < reader.length() ; i++) {
                try {
                    list.add(reader.getJSONObject(i).getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddObjectActivity.this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categories.setAdapter(dataAdapter);

        }
    }

}
