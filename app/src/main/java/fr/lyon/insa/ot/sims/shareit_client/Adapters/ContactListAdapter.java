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
import java.util.zip.Inflater;

import fr.lyon.insa.ot.sims.shareit_client.R;
import fr.lyon.insa.ot.sims.shareit_client.Utils;

/**
 * Created by Gaetan on 02/03/2015.
 */
public class ContactListAdapter  extends BaseAdapter {

    private Activity activity;
    private List<Contact> contacts;
    private LayoutInflater inflater;

    public ContactListAdapter(Activity activity, JSONArray contacts) throws JSONException {
        this.activity = activity;
        this.contacts = new ArrayList<>();

        for(int i = 0; i < contacts.length(); i++){
            JSONObject row = contacts.getJSONObject(i);
            this.contacts.add(new Contact(row));
        }
    }

    @Override
    public int getCount() {
        return this.contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return this.contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.contacts.get(position).contactId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.contact_list_item_layout, null);

        TextView name = (TextView) convertView.findViewById(R.id.contactName);

        Contact contact = this.contacts.get(position);
        name.setText(contact.fullName);

        return convertView;
    }

    private class Contact{
        public String fullName;
        public long contactId;

        public Contact(JSONObject contact) throws JSONException {
            this.fullName = Utils.getUserName(contact);
            this.contactId = contact.getLong("id");
        }
    }
}
