package fr.lyon.insa.ot.sims.shareit_client.searchList;

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

import fr.lyon.insa.ot.sims.shareit_client.MainActivity;
import fr.lyon.insa.ot.sims.shareit_client.R;

/**
 * Created by Gaetan on 24/02/2015.
 */
public class SearchListAdapter extends BaseAdapter {
    private Activity activity;
    private List<SimplifiedProduct> products;
    private LayoutInflater inflater;

    public SearchListAdapter(Activity activity, JSONArray products) throws JSONException {
        this.activity = activity;
        this.products = new ArrayList<>();

        for(int i = 0; i < products.length(); i++){
            JSONObject row = products.getJSONObject(i);
            this.products.add(new SimplifiedProduct(row));
        }

    }

    public void updateProducts(JSONArray products) throws JSONException {
        this.products = new ArrayList<>();
        for(int i = 0; i < products.length(); i++){
            JSONObject row = products.getJSONObject(i);
            this.products.add(new SimplifiedProduct(row));
        }
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return products.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.search_list_item_layout, null);

        TextView name = (TextView) convertView.findViewById(R.id.ObjectName);
        TextView status = (TextView) convertView.findViewById(R.id.ObjectStatus);
        TextView category = (TextView) convertView.findViewById(R.id.ObjectCategory);

        SimplifiedProduct product = this.products.get(position);
        name.setText(product.name);
        status.setText(product.disponibility);
        category.setText(product.category);

        return convertView;
    }

    private class SimplifiedProduct {
        public String name;
        public String disponibility;
        public String category;
        public long id;

        public SimplifiedProduct(JSONObject item) throws JSONException {
            this.name = item.getString("nom");
            this.disponibility = item.getString("status");
            this.id = item.getInt("id");
            this.category = item.getJSONObject("category").getString("name");
        }
    }
}
