package me.alpha12.ecarnet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.classes.Utils;
import me.alpha12.ecarnet.models.CarModel;


public class ModelAdapter extends ArrayAdapter<CarModel> {

    List<CarModel> modelList;

    public ModelAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ModelAdapter(Context context, int resource, List<CarModel> items) {
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

        CarModel currentModel = getItem(position);

        if (currentModel != null) {
            TextView brand = (TextView) v.findViewById(R.id.title);
            TextView year = (TextView) v.findViewById(R.id.time);
            TextView subModel = (TextView) v.findViewById(R.id.text);

            if (brand != null) {
                brand.setText(Utils.ucWords(currentModel.getBrand() + "  " + currentModel.getModel()));
            }

            if(year != null)
            {
                year.setText(Utils.ucWords(currentModel.getEnergy()));
            }

            if(subModel != null)
            {
                subModel.setText(Utils.ucWords(currentModel.getBody() + " - " + currentModel.getVersion() + " - " + currentModel.getGearboxType() + " " + currentModel.getGears()) + " rapports");
            }
        }
        return v;
    }


    public void update(List<CarModel> newModelList)
    {
        modelList = newModelList;
    }

}