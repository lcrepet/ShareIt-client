package fr.lyon.insa.ot.sims.shareit_client;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class BorrowActivity extends Activity {

    private String idUser;

    /* données sur l'échange (lended ou borrowed)*/

    private String TAG_LENDER= "lender";
    private String TAG_BORROWER="borrower";
    private  String TAG_PRODUCT = "product";

    /* nom du produit*/

    private String TAG_NAME="name";

    /* nom et prénom emprunteur ou prêteur*/

    private String TAG_FIRSTNAME="firstname";
    private String TAG_LASTNAME="lastname";


	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_borrow);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;

        }
        
        try{
            idUser = getIntent().getExtras().getString("userId");
        } catch(Exception e) {
            idUser = String.valueOf(Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)));
        }

        if(idUser == null) {
            idUser = String.valueOf(Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)));
        }


        new DisplayBorrowedObjects().execute();
        new DisplayLendedObjects().execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.borrow, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.menu_profile:
                HashMap<String, String> extras = new HashMap<>();
                extras.put(Intent.EXTRA_INTENT, ObjectActivity.class.getCanonicalName());
                Utils.openOtherActivity(this,
                        ProfileActivity.class, extras);
                return true;
            case R.id.menu_email:
                Utils.openOtherActivity(this,
                        EmailActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
	}

    /* affichage des objets empruntés par l'utilisateur*/

    private class DisplayBorrowedObjects extends AsyncTask<String, Void, JSONObject>{

        @Override
        public JSONObject doInBackground(String... message) {
            return Request.getRequest(Constants.uri + "user/{}/borrowed" );
        }
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        protected void onPostExecute(JSONObject reader) {
            TextView nomObjetEmprunte= (TextView) findViewById(R.id.NomObjetEmprunte);
            TextView nomPreteur = (TextView) findViewById(R.id.NomPreteur);
            TextView prenomPreteur = (TextView) findViewById(R.id.PrenomPreteur);


            try {

                String productToSet = reader.getJSONObject(TAG_PRODUCT).getString(TAG_NAME);
                nomObjetEmprunte.setText(productToSet);
                getActionBar().setTitle(productToSet);

                String lenderNameToSet = reader.getJSONObject(TAG_LENDER).getString(TAG_LASTNAME);
                nomPreteur.setText(lenderNameToSet);
                getActionBar().setTitle(lenderNameToSet);

                String lenderFirstNameToSet = reader.getJSONObject(TAG_LENDER).getString(TAG_FIRSTNAME);
                prenomPreteur.setText(lenderFirstNameToSet);
                getActionBar().setTitle(lenderFirstNameToSet);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /* affichage des objets prêtés par l'utilisateur*/

    private class DisplayLendedObjects extends AsyncTask<String, Void, JSONObject> {

        @Override
        public JSONObject doInBackground(String... message) {
            return Request.getRequest(Constants.uri + "user/{"+ idUser +"}/lended");
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        protected void onPostExecute(JSONObject reader) {
            TextView nomObjetPrete= (TextView) findViewById(R.id.NomObjetPrete);
            TextView nomEmprunteur = (TextView) findViewById(R.id.NomEmprunteur);
            TextView prenomEmprunteur = (TextView) findViewById(R.id.PrenomEmprunteur);

            try {

                String productToSet = reader.getJSONObject(TAG_PRODUCT).getString(TAG_NAME);
                nomObjetPrete.setText(productToSet);
                getActionBar().setTitle(productToSet);

                String borrowerNameToSet = reader.getJSONObject(TAG_BORROWER).getString(TAG_LASTNAME);
                nomEmprunteur.setText(borrowerNameToSet);
                getActionBar().setTitle(borrowerNameToSet);

                String borrowerFirstNameToSet = reader.getJSONObject(TAG_BORROWER).getString(TAG_FIRSTNAME);
                prenomEmprunteur.setText(borrowerFirstNameToSet);
                getActionBar().setTitle(borrowerFirstNameToSet);


            }catch(JSONException e) {
                e.printStackTrace();

            }
        }
    }
}
