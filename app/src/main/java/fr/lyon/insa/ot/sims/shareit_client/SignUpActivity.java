package fr.lyon.insa.ot.sims.shareit_client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SignUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final Button button = (Button) findViewById(R.id.signUpButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText firstName = (EditText) findViewById(R.id.firstNameText);
                EditText lastName = (EditText) findViewById(R.id.lastNameText);
                EditText postCode = (EditText) findViewById(R.id.postCode);
                EditText age = (EditText) findViewById(R.id.age);
                Spinner sex = (Spinner) findViewById(R.id.sex);
                EditText phone = (EditText) findViewById(R.id.phone);

                JSONObject userInfos = new JSONObject();

                if( firstName.getText().toString().trim().equals(""))
                {
                    firstName.setError( "Prénom requis !" );
                } else if( lastName.getText().toString().trim().equals(""))
                {
                    lastName.setError( "Nom requis !" );
                } else if( postCode.getText().toString().trim().equals(""))
                {
                    postCode.setError( "Code postal requis !" );
                } else {
                    try {
                        userInfos.put("firstname", firstName.getText());
                        userInfos.put("lastname", lastName.getText());
                        userInfos.put("postcode", postCode.getText());

                        if(!phone.getText().toString().trim().equals("")){
                            userInfos.put("phone", phone.getText());
                        }
                        if(sex.getSelectedItemPosition() != 0){
                            String sexToSet;
                            if(sex.getSelectedItemPosition() == 1){
                                sexToSet = "M";
                            } else {
                                sexToSet = "F";
                            }
                            userInfos.put("sex", sexToSet);
                        }
                        if(!age.getText().toString().trim().equals("")){
                            userInfos.put("age", age.getText());
                        }
                        new SignUp().execute(Constants.uri + "user/", userInfos.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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

    private class SignUp extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... message) {
            List<NameValuePair> pairs = new ArrayList<>();
            JSONObject user;
            try {
                user = new JSONObject(message[1]);
                pairs.add(new BasicNameValuePair("firstname", user.getString("firstname")));
                pairs.add(new BasicNameValuePair("lastname", user.getString("lastname")));
                pairs.add(new BasicNameValuePair("postcode", user.getString("postcode")));
                if(user.has("sex")){
                    pairs.add(new BasicNameValuePair("sex", user.getString("sex")));
                }
                if(user.has("age")){
                    pairs.add(new BasicNameValuePair("age", user.getString("age")));
                }
                if(user.has("phone")){
                    pairs.add(new BasicNameValuePair("phone", user.getString("phone")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return Request.newPostRequest(message[0], pairs);
        }

        protected void onPostExecute(JSONObject reader) {
            try {
                long userId =  reader.getLong("id");
                Utils.setUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE), userId);

                Intent intent;
                intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
