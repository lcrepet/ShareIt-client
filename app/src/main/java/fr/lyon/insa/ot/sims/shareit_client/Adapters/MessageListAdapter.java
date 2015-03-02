package fr.lyon.insa.ot.sims.shareit_client.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.lyon.insa.ot.sims.shareit_client.R;
import fr.lyon.insa.ot.sims.shareit_client.Utils;

/**
 * Created by Gaetan on 02/03/2015.
 */
public class MessageListAdapter extends BaseAdapter {

    private Activity activity;
    private List<Message> messages;
    private LayoutInflater inflater;

    public MessageListAdapter(Activity activity, JSONArray messages) throws JSONException {
        this.activity = activity;
        this.messages = new ArrayList<>();

        for(int i = 0; i < messages.length(); i++){
            JSONObject row = messages.getJSONObject(i);
            this.messages.add(new Message(row));
        }
    }

    @Override
    public int getCount() {
        return this.messages.size();
    }

    @Override
    public Object getItem(int position) {
        return this.messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.messages.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.message_list_item_layout, null);

        TextView name = (TextView) convertView.findViewById(R.id.contactName);
        TextView text = (TextView) convertView.findViewById(R.id.message);

        Message message = this.messages.get(position);
        name.setText(message.from);
        text.setText(message.text);

        return convertView;
    }

    private class Message{
        public String text;
        public String from;
        public long id;

        public Message(JSONObject message) throws JSONException {
            this.from = Utils.getUserName(message.getJSONObject("sender"));
            this.text = message.getString("message");
            this.id = message.getLong("id");
        }
    }
}
