package fr.lyon.insa.ot.sims.shareit_client.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.lyon.insa.ot.sims.shareit_client.PreDefinedObjects;
import fr.lyon.insa.ot.sims.shareit_client.R;

/**
 * Created by Gaetan on 10/03/2015.
 */
public class ObjectsListAdapter extends BaseAdapter {

    private List<Boolean> listCheck;
    private List<PreDefinedObjects.PredefinedObject> listObject;
    private Activity activity;
    private LayoutInflater inflater;

    public ObjectsListAdapter(Activity activity){
        listCheck = new ArrayList<>();
        this.activity = activity;
        listObject = new PreDefinedObjects().getList();
        for(int i = 0; i < listObject.size(); i++){
            listCheck.add(false);
        }
    }

    @Override
    public int getCount() {
        return listObject.size();
    }

    @Override
    public Object getItem(int position) {
        return listObject.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public List<PreDefinedObjects.PredefinedObject> getSelectedObjects(){
        List<PreDefinedObjects.PredefinedObject> result = new ArrayList<>();

        for(int i = 0; i < listObject.size(); i++){
            if(listCheck.get(i)){
                result.add(listObject.get(i));
            }
        }

        return result;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.objects_multiple_add_list_layout, null);

        TextView name = (TextView) convertView.findViewById(R.id.objectName);

        PreDefinedObjects.PredefinedObject object = this.listObject.get(position);
        name.setText(object.getName());

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkObject);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listCheck.set(position, isChecked);
            }
        });

        checkBox.setChecked(listCheck.get(position));

        return convertView;
    }
}
