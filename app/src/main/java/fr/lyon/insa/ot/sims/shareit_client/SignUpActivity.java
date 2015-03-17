package fr.lyon.insa.ot.sims.shareit_client;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SignUpActivity extends Activity {

    private EditText firstName;
    private EditText lastName;
    private EditText postCode;
    private EditText age;
    private Spinner sex;
    private EditText phone;
    private EditText cheminPhoto;
    private ImageView photo;
    Bitmap bitmap;
    Uri targetUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sign_up);

        firstName = (EditText) findViewById(R.id.firstNameText);
        lastName = (EditText) findViewById(R.id.lastNameText);
        postCode = (EditText) findViewById(R.id.postCode);
        age = (EditText) findViewById(R.id.age);
        sex = (Spinner) findViewById(R.id.sex);
        phone = (EditText) findViewById(R.id.phone);
        cheminPhoto= (EditText) findViewById(R.id.PicUser);
        photo = (ImageView)findViewById(R.id.Pic);
        final Button button = (Button) findViewById(R.id.signUpButton);
        final Button downloadPicture = (Button) findViewById(R.id.FindPic);


        if(getIntent().getExtras() != null && getIntent().getExtras().getString("modification").equals("true")){
            getActionBar().setDisplayHomeAsUpEnabled(true);

            button.setText("Enregistrer");
            firstName.setHint("Prénom");
            lastName.setHint("Nom");
            postCode.setHint("Code postal");
            cheminPhoto.setHint("Photo de profil");

            downloadPicture.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 0);
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JSONObject userInfos = new JSONObject();
                        if (!firstName.getText().toString().trim().equals("")) {
                            userInfos.put("firstname", firstName.getText().toString().trim());
                        }
                        if (!lastName.getText().toString().trim().equals("")) {
                            userInfos.put("lastname", lastName.getText().toString().trim());
                        }
                        if (!postCode.getText().toString().trim().equals("")) {
                            userInfos.put("postcode", postCode.getText().toString().trim());
                        }
                        if (!phone.getText().toString().trim().equals("")) {
                            userInfos.put("phone", phone.getText().toString().trim());
                        }
                        if (sex.getSelectedItemPosition() != 0) {
                            String sexToSet;
                            if (sex.getSelectedItemPosition() == 1) {
                                sexToSet = "M";
                            } else {
                                sexToSet = "F";
                            }
                            userInfos.put("sex", sexToSet);
                        }
                        if (!age.getText().toString().trim().equals("")) {
                            userInfos.put("age", age.getText().toString().trim());
                        }
                        new UpdateProfile().execute(Constants.uri + "user/" +
                                Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE))
                                , userInfos.toString());
                        if(!cheminPhoto.getText().toString().trim().equals("")){
                            userInfos.put("profilePicture",bitmap);
                        }
                        //create a file to write bitmap data
                        File file = new File(targetUri.getPath());
                        file.createNewFile();

                        //Convert bitmap to byte array

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                        byte[] bitmapdata = bos.toByteArray();


                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();

                        new UpdatePicture(file).execute(Constants.uri + "user/" +
                                Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE))
                                , file.toString());

                    } catch(JSONException e ){

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


        }else {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    JSONObject userInfos = new JSONObject();

                    if (firstName.getText().toString().trim().equals("")) {
                        firstName.setError("Prénom requis !");
                    } else if (lastName.getText().toString().trim().equals("")) {
                        lastName.setError("Nom requis !");
                    } else if (postCode.getText().toString().trim().equals("")) {
                        postCode.setError("Code postal requis !");
                    } else {
                        try {
                            userInfos.put("firstname", firstName.getText());
                            userInfos.put("lastname", lastName.getText());
                            userInfos.put("postcode", postCode.getText());

                            if (!phone.getText().toString().trim().equals("")) {
                                userInfos.put("phone", phone.getText());
                            }
                            if (sex.getSelectedItemPosition() != 0) {
                                String sexToSet;
                                if (sex.getSelectedItemPosition() == 1) {
                                    sexToSet = "M";
                                } else {
                                    sexToSet = "F";
                                }
                                userInfos.put("sex", sexToSet);
                            }
                            if (!age.getText().toString().trim().equals("")) {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            targetUri = data.getData();
            cheminPhoto.setText(targetUri.toString());
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                //photo.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#CC0000")));
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
        String className = ProfileActivity.class.getCanonicalName();

        try {
            return new Intent(this, Class.forName(className));
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
            return null;
        }
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

                Utils.openOtherActivity(SignUpActivity.this, MainActivity.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class UpdateProfile extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... message) {
            List<NameValuePair> pairs = new ArrayList<>();
            JSONObject user;
            try {
                user = new JSONObject(message[1]);
                if(user.has("firstname")){
                    pairs.add(new BasicNameValuePair("firstname", user.getString("firstname")));
                }
                if(user.has("lastname")){
                    pairs.add(new BasicNameValuePair("lastname", user.getString("lastname")));
                }
                if(user.has("postcode")){
                    pairs.add(new BasicNameValuePair("postcode", user.getString("postcode")));
                }
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

            return Request.setRequest(message[0], pairs);
        }

        protected void onPostExecute(String reader) {
            HashMap<String, String> extras = new HashMap<>();
            extras.put(Intent.EXTRA_INTENT, MainActivity.class.getCanonicalName());
            Utils.openOtherActivity(SignUpActivity.this, ProfileActivity.class, extras);
        }
    }

    private class UpdatePicture extends AsyncTask<String, Void, String> {

        private File picture;
        public UpdatePicture (File file){
            super();
            this.picture = file;
        }

        @Override
        protected String doInBackground(String... message) {
            //List <NameValuePair> pairs = new ArrayList<>();

                    //pairs.add(new BasicNameValuePair("profilePicture",message[1]));

            return Request.putPicture(message[0], picture);
        }

        protected void onPostExecute(String reader) {
            HashMap<String, String> extras = new HashMap<>();
            extras.put(Intent.EXTRA_INTENT, MainActivity.class.getCanonicalName());
            Utils.openOtherActivity(SignUpActivity.this, ProfileActivity.class, extras);
        }
    }
}
