package fr.lyon.insa.ot.sims.shareit_client;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ObjectActivity extends Activity {

    private String id;
    private  String TAG_ID = "id";
    private  String TAG_NAME = "name";
    private  String TAG_TYPE = "category";
    private  String TAG_STATUS = "status";
    private  String TAG_DESCRIPTION="description";
    private  String TAG_SHARER="sharer";



    private TextView nom = null;
    private TextView type = null;
    private TextView status = null;
    private TextView desc = null;
    private TextView prop = null;
    private TextView note = null;
    private Button objectButton = null;
    private Button editButton = null;
    private Button deleteButton = null;

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

        id = String.valueOf(Long.valueOf(extras.getString("id")));

        nom = (TextView) findViewById(R.id.NomObjet);
        type = (TextView) findViewById(R.id.TypeObjet);
        status = (TextView)findViewById(R.id.StatusObjet);
        desc = (TextView)findViewById(R.id.DescriptionObjet);
        prop = (TextView) findViewById(R.id.Proprietaire);
        note = (TextView) findViewById(R.id.Note);
        objectButton = (Button) findViewById(R.id.bouton1);
        editButton = (Button) findViewById(R.id.editButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
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
            return Request.getRequest(Constants.uri + "product/" + id);
        }

        protected void onPostExecute(final JSONObject reader) {

            String idJSON = "";

            try {
                idJSON = reader.getJSONObject(TAG_SHARER).getString(TAG_ID);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {

                String nameToSet = reader.getString(TAG_NAME);
                nom.setText(nameToSet);
                getActionBar().setTitle(nameToSet);

                String typeToSet = reader.getJSONObject(TAG_TYPE).getString(TAG_NAME);
                type.setText(typeToSet);

                String statusToSet = reader.getString(TAG_STATUS);
                status.setText(statusToSet);

                String descToSet = reader.getString(TAG_DESCRIPTION);
                if(descToSet != null && !descToSet.isEmpty()){
                    desc.setText(descToSet);
                } else {
                    desc.setVisibility(View.GONE);
                }

                String propToSet = Utils.getUserName(reader.getJSONObject(TAG_SHARER));
                prop.setText(propToSet);

                String noteToSet = reader.getJSONObject(TAG_SHARER).getJSONObject("userStats").getString("averageNote");
                if(!noteToSet.equals("-1.0")){
                    note.setText("Note : " + noteToSet);
                } else {
                    desc.setVisibility(View.GONE);
                }


                if(!(Long.toString(Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE))).equals(idJSON))){
                    if(status.getText().equals("disponible")){
                        objectButton.setVisibility(View.VISIBLE);
                        objectButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                HashMap<String, String> extras = new HashMap<>();
                                extras.put(TAG_ID, id);
                                Utils.openOtherActivity(ObjectActivity.this, BorrowRequestActivity.class, extras);
                            }
                        });
                    }
                } else {
                    editButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);

                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new DeleteObject().execute();
                        }
                    });

                }

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

    private class DeleteObject extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... message) {
            try {
                return Request.deleteRequest(Constants.uri + "product/" + id);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String reader) {
           Toast.makeText(ObjectActivity.this, "Objet supprimé !", Toast.LENGTH_LONG).show();
            HashMap<String, String> extras = new HashMap<>();
            extras.put(Intent.EXTRA_INTENT, MainActivity.class.getCanonicalName());
            Utils.openOtherActivity(ObjectActivity.this, ProfileActivity.class, extras);
        }
    }


}
