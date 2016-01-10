package me.alpha12.ecarnet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.models.Model;


public class ModelAdapter extends ArrayAdapter<Model> {

    List<Model> modelList;

    public ModelAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ModelAdapter(Context context, int resource, List<Model> items) {
        super(context, resource, items);
        modelList = items;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;



        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.custom_model_listview, null);
        }

        Model currentModel = getItem(position);

        if (currentModel != null) {
            TextView brand = (TextView) v.findViewById(R.id.title);
            TextView year = (TextView) v.findViewById(R.id.time);
            TextView subModel = (TextView) v.findViewById(R.id.text);

            if (brand != null) {
                brand.setText(currentModel.getBrand() + "  " + currentModel.getModel());
            }

            if(year != null)
            {
                year.setText(Integer.toString(currentModel.getYear()));
            }

            if(subModel != null)
            {
                subModel.setText(currentModel.getSubModel() + " - " + currentModel.getEngine() + " - " + currentModel.getRatedHP() + "CV");
            }
        }
        return v;
    }


    public void update(List<Model> newModelList)
    {
        modelList = newModelList;
    }

}