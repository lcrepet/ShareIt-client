package fr.lyon.insa.ot.sims.shareit_client;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.lyon.insa.ot.sims.shareit_client.Adapters.ObjectsListAdapter;


public class MultipleObjectCreateActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_object_create);

        final ListView objectList = (ListView) findViewById(R.id.objectList);
        final ObjectsListAdapter adapter = new ObjectsListAdapter(this);
        objectList.setAdapter(adapter);

        Button validate = (Button) findViewById(R.id.addObjects);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(PreDefinedObjects.PredefinedObject product: adapter.getSelectedObjects()){
                    JSONObject args = new JSONObject();
                    try {
                        args.put("name", product.getName());
                        args.put("category", product.getCategory());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new SaveObject().execute(Constants.uri + "/user/" + Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)) + "/product", args.toString());
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_multiple_object_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class SaveObject extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... message) {
            List<NameValuePair> pairs = new ArrayList<>();
            try {
                JSONObject obj = new JSONObject(message[1]);

                pairs.add(new BasicNameValuePair("name", obj.getString("name")));
                pairs.add(new BasicNameValuePair("category", obj.getString("category")));
            } catch (JSONException e) {
                e.printStackTrace();
            }



            return Request.newPostRequest(message[0], pairs);
        }

        protected void onPostExecute(JSONObject object) {
            Toast.makeText(MultipleObjectCreateActivity.this, "Objets créé !", Toast.LENGTH_LONG).show();

            HashMap<String, String> extras = new HashMap<>();
            extras.put(Intent.EXTRA_INTENT, MainActivity.class.getCanonicalName());
            Utils.openOtherActivity(MultipleObjectCreateActivity.this, ProfileActivity.class, extras);

        }
    }
}
