package fr.lyon.insa.ot.sims.shareit_client;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

import fr.lyon.insa.ot.sims.shareit_client.Adapters.ProductListAdapter;
import fr.lyon.insa.ot.sims.shareit_client.Adapters.ProductListLendAdapter;

public class BorrowActivity extends Activity {

    private String idUser;
    private ListView listExchangesBorrow = null;
    private ListView listExchangesLend = null;



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_borrow);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        listExchangesBorrow = (ListView) findViewById(R.id.listExchanges);
        listExchangesLend = (ListView) findViewById(R.id.listExchangesLend);

        listExchangesLend.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> extras = new HashMap<>();
                extras.put(Intent.EXTRA_INTENT, MainActivity.class.getCanonicalName());
                extras.put("id", String.valueOf(listExchangesLend.getAdapter().getItemId(position)));
                Utils.openOtherActivity(BorrowActivity.this, RequestManagementActivity.class, extras);
            }
        });


        try{
            idUser = getIntent().getExtras().getString("userId");
        } catch(Exception e) {
            idUser = String.valueOf(Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)));
        }

        if(idUser == null) {
            idUser = String.valueOf(Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)));
        }

       ProductListAdapter adapterBorrow = null;

        try {
            adapterBorrow = new ProductListAdapter(this, new JSONArray());
            listExchangesBorrow.setAdapter(adapterBorrow);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ProductListLendAdapter  adapterLend = null;
        try {
            adapterLend = new ProductListLendAdapter(this, new JSONArray());
            listExchangesLend.setAdapter(adapterLend);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new DisplayBorrowedObjects().execute(Constants.uri + "user/"+ idUser+"/borrowed");
        new DisplayLendedObjects().execute(Constants.uri + "user/"+ idUser+"/lended");

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


    private class DisplayBorrowedObjects extends AsyncTask<String, Void, JSONArray>{

        @Override
        protected JSONArray doInBackground(String... message) {
            return Request.getListRequest(message[0]);
        }
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        protected void onPostExecute(JSONArray reader) {


            try {
                ProductListAdapter adapterBorrow = (ProductListAdapter) listExchangesBorrow.getAdapter();
                adapterBorrow.updateProducts(reader);
                listExchangesBorrow.setAdapter(adapterBorrow);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /* affichage des objets prêtés par l'utilisateur*/


    private class DisplayLendedObjects extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... message) {
            return Request.getListRequest(message[0]);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        protected void onPostExecute(JSONArray reader) {

            try {

                ProductListLendAdapter adapterLend = (ProductListLendAdapter) listExchangesLend.getAdapter();
                adapterLend.updateProducts(reader);
                listExchangesLend.setAdapter(adapterLend);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
