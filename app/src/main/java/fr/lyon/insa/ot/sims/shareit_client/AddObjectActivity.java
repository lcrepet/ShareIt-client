package fr.lyon.insa.ot.sims.shareit_client;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import java.util.HashMap;
import java.util.List;

import fr.lyon.insa.ot.sims.shareit_client.Adapters.ObjectsListAdapter;


public class AddObjectActivity extends Activity {

    private EditText name = null;
    private EditText description = null;
    private Button save = null;
    private Spinner categories = null;
    private Spinner preselection = null;
    private ListView list = null;
    private List<PreDefinedObjects.PredefinedObject> listObject = null;
    private List<String> objectsList = new ArrayList<String>();
    private boolean modify = false;
    private Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_object);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0099CC")));

        name = (EditText) findViewById(R.id.NameObject);
        description = (EditText) findViewById(R.id.DescObject);
        save = (Button) findViewById(R.id.SaveObject);
        categories = (Spinner) findViewById(R.id.TypeObject);
        preselection = (Spinner) findViewById(R.id.Preselection);
        Button multipleAdd = (Button) findViewById(R.id.multipleAdd);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = Integer.parseInt(extras.getString("id"));
            if(id != null) {
                modify = true;
                preselection.setVisibility(View.GONE);
                multipleAdd.setVisibility(View.GONE);
            }
        }

        multipleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openOtherActivity(AddObjectActivity.this, MultipleObjectCreateActivity.class);
            }
        });

        new GetCategories().execute(Constants.uri + "/product/category");

        categories.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                objectsList.clear();
                objectsList.add("");
                listObject = new PreDefinedObjects().getList(pos+1);

                for(PreDefinedObjects.PredefinedObject product: listObject){
                    objectsList.add(product.getName());
                }

                ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(AddObjectActivity.this,android.R.layout.simple_spinner_item, objectsList);
                nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                preselection.setAdapter(nameAdapter);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }

        });

        preselection.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                name.setText(preselection.getItemAtPosition(pos).toString());
            }

            public void onNothingSelected(AdapterView<?> parent) {
                name.setText("");
            }

        });

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(modify){
                    new UpdateObject().execute("PUT URL HERE, not working code");
                } else {
                    new SaveObject().execute(Constants.uri + "/user/" + Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)) + "/product");
                }
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

            try {
                HashMap<String, String> extras = new HashMap<>();
                extras.put(Intent.EXTRA_INTENT, ProfileActivity.class.getCanonicalName());
                extras.put("id", object.getString("id"));
                Utils.openOtherActivity(AddObjectActivity.this, ObjectActivity.class, extras);
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

    private class UpdateObject extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... message) {

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("name", name.getText().toString()));
            pairs.add(new BasicNameValuePair("category", Integer.toString(categories.getSelectedItemPosition() + 1)));
            pairs.add(new BasicNameValuePair("description", description.getText().toString()));

            return Request.newPostRequest(message[0], pairs);
        }

        protected void onPostExecute(JSONObject object) {
            Toast.makeText(AddObjectActivity.this,"Objet modifié !", Toast.LENGTH_LONG).show();

            HashMap<String, String> extras = new HashMap<>();
            extras.put(Intent.EXTRA_INTENT, ProfileActivity.class.getCanonicalName());
            extras.put("id", id.toString());
            Utils.openOtherActivity(AddObjectActivity.this, ObjectActivity.class, extras);
        }
    }

}
