package fr.lyon.insa.ot.sims.shareit_client;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class BorrowRequestActivity extends Activity {

    private TextView startDate = null;
    private TextView endDate = null;
    private Button changeStart = null;
    private Button changeEnd = null;

    private int sYear;
    private int sMonth;
    private int sDay;
    private int eYear;
    private int eMonth;
    private int eDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_request);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0099CC")));

        startDate = (TextView) findViewById(R.id.StartDate);
        endDate = (TextView) findViewById(R.id.EndDate);
        changeStart = (Button) findViewById(R.id.ChangeStartDate);
        changeEnd = (Button) findViewById(R.id.ChangeEndDate);
        setCurrentDate();

        changeStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(BorrowRequestActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                sDay = dayOfMonth;
                                sMonth = monthOfYear+1;
                                sYear = year;
                                startDate.setText("Du : " + sDay + "-"+ sMonth + "-" + sYear);

                            }
                        }, sYear, sMonth, sDay);
                dpd1.show();
            }
        });

        changeEnd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(BorrowRequestActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                eDay = dayOfMonth;
                                eMonth = monthOfYear+1;
                                eYear = year;
                                endDate.setText("Au : " + eDay + "-"+ eMonth + "-" + eYear);

                            }
                        }, eYear, eMonth, eDay);
                dpd1.show();
            }
        });

        new GetObject().execute(Constants.uri + "product/" + getIntent().getExtras().getString("id"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_borrow_request, menu);
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

    private void setCurrentDate() {
        Calendar c = Calendar.getInstance();
        sYear = c.get(Calendar.YEAR);
        sMonth = c.get(Calendar.MONTH);
        sDay = c.get(Calendar.DAY_OF_MONTH);
        eYear = c.get(Calendar.YEAR);
        eMonth = c.get(Calendar.MONTH);
        eDay = c.get(Calendar.DAY_OF_MONTH);

        startDate.setText(new StringBuilder().append("Du : ")
                .append(sDay).append("-").append(sMonth + 1).append("-")
                .append(sYear));
        endDate.setText(new StringBuilder().append("Au : ")
                .append(eDay).append("-").append(eMonth + 1).append("-")
                .append(eYear));
    }

    private class GetObject extends AsyncTask<String, Void, JSONObject> {

        @Override
        public JSONObject doInBackground(String... message) {
            return Request.getRequest(message[0]);
        }

        protected void onPostExecute(final JSONObject object){
            TextView name = (TextView) findViewById(R.id.objectName);
            TextView owner = (TextView) findViewById(R.id.objectOwner);
            try {
                String nameToSet = object.getString("name") + " (" + object.getJSONObject("category").getString("name") + ")";
                String ownerToSet = object.getJSONObject("sharer").getString("firstname") + " " + object.getJSONObject("sharer").getString("lastname");

                name.setText(nameToSet);
                owner.setText("Propriétaire : " + ownerToSet);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final Button objectButton = (Button) findViewById(R.id.buttonBorrow);
            objectButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        new BorrowRequest().execute(Constants.uri + "product/" + object.getString("id") + "/borrow/", object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    private class BorrowRequest extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... message) {
            List<NameValuePair> pairs = new ArrayList<>();
            try {
                JSONObject product = new JSONObject(message[1]);
                String start = sDay + "-" + sMonth + "-" + sYear;
                String end = eDay + "-" + eMonth + "-" + eYear;

                pairs.add(new BasicNameValuePair("lender", product.getJSONObject("sharer").getString("id")));
                pairs.add(new BasicNameValuePair("borrower",
                        String.valueOf(Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)))));
                pairs.add(new BasicNameValuePair("startdate", start));
                pairs.add(new BasicNameValuePair("enddate", end));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return Request.newPostRequest(message[0],pairs);
        }

        protected void onPostExecute(JSONObject reader){
            Toast.makeText(getApplicationContext(), "Requête envoyée !", Toast.LENGTH_LONG).show();


            Bundle extra = getIntent().getExtras();
            HashMap<String, String> extras = new HashMap<>();
            extras.put(Intent.EXTRA_INTENT, MainActivity.class.getCanonicalName());
            extras.put("id", String.valueOf(Long.valueOf(extra.getString("id"))));
            Utils.openOtherActivity(BorrowRequestActivity.this, ObjectActivity.class, extras);

        }
    }
}
