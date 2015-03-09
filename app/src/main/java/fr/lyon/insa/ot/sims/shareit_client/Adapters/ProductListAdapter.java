package fr.lyon.insa.ot.sims.shareit_client.Adapters;

/**
 * Created by Anne on 04/03/2015.
 */
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


public class ProductListAdapter extends BaseAdapter{


    private Activity activity;
    private List<SimplifiedProduct> products;
    private LayoutInflater inflater;

    public ProductListAdapter (Activity activity, JSONArray products) throws JSONException {
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
            convertView = inflater.inflate(R.layout.exchanges_list_item_layout, null);

        TextView name = (TextView) convertView.findViewById(R.id.NomObjetEmprunte);
        TextView sharer = (TextView) convertView.findViewById(R.id.NomPreteur);

        SimplifiedProduct product = this.products.get(position);
        name.setText(product.name + " (" + product.status + ")");
        sharer.setText(product.sharer);

        return convertView;
    }

    private class SimplifiedProduct {
        public String name;
        public String sharer;
        public String status;
        public long id;

        public SimplifiedProduct(JSONObject item) throws JSONException {
            this.name = item.getJSONObject("product").getString("name");
            this.sharer = Utils.getUserName(item.getJSONObject("lender"));
            this.id = item.getInt("id");
            this.status = item.getJSONObject("product").getString("status");
        }
    }
}
