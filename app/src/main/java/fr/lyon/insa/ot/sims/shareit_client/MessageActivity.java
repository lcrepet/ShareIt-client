package fr.lyon.insa.ot.sims.shareit_client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.lyon.insa.ot.sims.shareit_client.Adapters.MessageListAdapter;

public class MessageActivity extends Activity {

    public String contact;
    public EditText message;
    public Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        this.handler = new Handler();

        this.handler.postDelayed(this.runnable, 5000);

        this.contact = getIntent().getExtras().getString("contactId");

        new GetMessages().execute(Constants.uri + "messages/"
            + Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)) + "/"
            + this.contact);

        message = (EditText) findViewById(R.id.messageText);
        final Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(message.getText() != null){
                    String url = Constants.uri + "message/";

                    new SendMessage().execute(url);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
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

    private final Runnable runnable = new Runnable()
    {
        public void run()

        {
            new GetMessages().execute(Constants.uri + "messages/"
                    + Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)) + "/"
                    + MessageActivity.this.contact);
            MessageActivity.this.handler.postDelayed(runnable, 5000);
        }

    };

    private class GetMessages extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... message) {
            return Request.getListRequest(message[0]);
        }

        protected void onPostExecute(JSONArray reader) {
            ListView messageList = (ListView) findViewById(R.id.messageList);
            try {
                MessageListAdapter adapter = new MessageListAdapter(MessageActivity.this, reader);
                messageList.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendMessage extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... message) {
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("sender", String.valueOf(Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)))));
            pairs.add(new BasicNameValuePair("receiver", MessageActivity.this.contact));
            pairs.add(new BasicNameValuePair("message", MessageActivity.this.message.getText().toString()));
            return Request.newPostRequest(message[0], pairs);
        }

        protected void onPostExecute(JSONObject reader){
            final EditText message = (EditText) findViewById(R.id.messageText);
            message.setText("");
            new GetMessages().execute(Constants.uri + "messages/"
                    + Utils.getUserId(getSharedPreferences(MainActivity.SETTINGS, Context.MODE_PRIVATE)) + "/" + MessageActivity.this.contact);
        }
    }
}
