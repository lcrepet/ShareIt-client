package fr.lyon.insa.ot.sims.shareit_client;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class ObjectActivity extends Activity {

    private long id;
    private  String TAG_ID = "id";
    private  String TAG_NAME = "name";
    private  String TAG_TYPE = "type";
    private  String TAG_STATUS = "status";
    private  String TAG_DESCRIPTION="description";
    private  String TAG_PROP="prop";
    JSONObject object = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        id = extras.getLong(Intent.EXTRA_TEXT);
        TAG_ID= String.valueOf(id);




        final Button objectButton = (Button) findViewById(R.id.bouton1);
        objectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utils.openOtherActivity(ObjectActivity.this, BorrowRequestActivity.class);
            }
        });
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

    private class DisplayObject extends AsyncTask<String, Void, JSONObject>{

        @Override
        public JSONObject doInBackground(String... message) {
            return Request.getRequest(Constants.uri + "product/" + TAG_ID);
        }
        protected void onPostExecute(JSONObject reader) {
            TextView nom = (TextView) findViewById(R.id.NomObjet);
            TextView type = (TextView) findViewById(R.id.TypeObjet);
            TextView status = (TextView)findViewById(R.id.StatusObjet);
            TextView desc = (TextView)findViewById(R.id.DescriptionObjet);
            TextView prop = (TextView) findViewById(R.id.Proprietaire);

            try {


                JSONObject c = object.getJSONObject(TAG_ID);

                String name = c.getString(TAG_NAME);
                nom.setText((CharSequence) name);

                String typ = c.getString(TAG_TYPE);
                type.setText((CharSequence) typ);

                String stat = c.getString(TAG_STATUS);
                type.setText((CharSequence) stat);

                String des = c.getString(TAG_DESCRIPTION);
                type.setText((CharSequence) des);

                String pro = c.getString(TAG_PROP);
                type.setText((CharSequence) pro);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
