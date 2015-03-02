package fr.lyon.insa.ot.sims.shareit_client;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ObjectActivity extends Activity {

    private long id;
    private  String TAG_ID = "id";
    private  String TAG_NAME = "name";
    private  String TAG_TYPE = "category";
    private  String TAG_STATUS = "status";
    private  String TAG_DESCRIPTION="description";
    private  String TAG_SHARER="sharer";

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        id = Long.valueOf(extras.getString("id"));
        TAG_ID = String.valueOf(id);

        final Button objectButton = (Button) findViewById(R.id.bouton1);
        objectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HashMap<String, String> extras = new HashMap<>();
                extras.put("id", TAG_ID);
                Utils.openOtherActivity(ObjectActivity.this, BorrowRequestActivity.class, extras);
            }
        });

        new DisplayObject().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_object, menu);
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

    @Override
    public Intent getParentActivityIntent(){
        Intent parentIntent = getIntent();
        String className = parentIntent.getStringExtra(Intent.EXTRA_INTENT);

        try {
            return new Intent(this, Class.forName(className));
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
            return null;
        }
    }

    private class DisplayObject extends AsyncTask<String, Void, JSONObject>{

        @Override
        public JSONObject doInBackground(String... message) {
            return Request.getRequest(Constants.uri + "product/" + TAG_ID);
        }
<<<<<<< HEAD
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        protected void onPostExecute(JSONObject reader) {
=======
        protected void onPostExecute(final JSONObject reader) {
>>>>>>> c192dc258a720730c12a1da11959ab205d86f74b
            TextView nom = (TextView) findViewById(R.id.NomObjet);
            TextView type = (TextView) findViewById(R.id.TypeObjet);
            TextView status = (TextView)findViewById(R.id.StatusObjet);
            TextView desc = (TextView)findViewById(R.id.DescriptionObjet);
            TextView prop = (TextView) findViewById(R.id.Proprietaire);

            try {

                String nameToSet = reader.getString(TAG_NAME);
                nom.setText(nameToSet);
                getActionBar().setTitle(nameToSet);

                String typeToSet = reader.getJSONObject(TAG_TYPE).getString(TAG_NAME);
                type.setText(typeToSet);

                String statusToSet = reader.getString(TAG_STATUS);
                status.setText(statusToSet);

                String descToSet = reader.getString(TAG_DESCRIPTION);
                desc.setText(descToSet);

                String propToSet = Utils.getUserName(reader.getJSONObject(TAG_SHARER));
                prop.setText(propToSet);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            final TextView sharer = (TextView) findViewById(R.id.Proprietaire);
            sharer.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    HashMap<String, String> extras = new HashMap<>();
                    extras.put(Intent.EXTRA_INTENT, ObjectActivity.class.getCanonicalName());
                    try {
                        extras.put("userId", reader.getJSONObject(TAG_SHARER).getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Utils.openOtherActivity(ObjectActivity.this, ProfileActivity.class, extras);
                }
            });



        }
    }


}
