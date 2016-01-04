package me.alpha12.ecarnet.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
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
        CardView card = (CardView) v.findViewById(R.id.card_view);

        Model currentModel = getItem(position);

        if (currentModel != null) {
            TextView brand = (TextView) v.findViewById(R.id.brand);
            TextView model = (TextView) v.findViewById(R.id.model);
            TextView energy = (TextView) v.findViewById(R.id.energy);
            TextView engine = (TextView) v.findViewById(R.id.engine);
            TextView year = (TextView) v.findViewById(R.id.yearText);
            TextView ratedHP = (TextView) v.findViewById(R.id.ratedHPText);


            if (brand != null) {
                brand.setText(currentModel.getBrand());
            }

            if (model != null) {
                model.setText(currentModel.getModel());
            }

            if (energy != null) {
                energy.setText(currentModel.getEnergy());
            }
            if (engine != null) {
                engine.setText(currentModel.getEngine());
            }
            if (year != null) {
                year.setText(Integer.toString(currentModel.getYear()));
            }
            if (ratedHP != null) {
                ratedHP.setText(Integer.toString(currentModel.getRatedHP()));
            }
        }
        return v;
    }


    public void update(List<Model> newModelList)
    {
        modelList = newModelList;
    }

}