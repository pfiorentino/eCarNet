package me.alpha12.ecarnet.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.models.Intervention;

/**
 * Created by Damien on 29/01/2016.
 */
public class FillupAdapter extends ArrayAdapter<Intervention> {

    public FillupAdapter(Context context, List<Intervention> interventionList) {
        super(context, 0, interventionList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fillup_item, parent, false);
        }

        FillupItemViewHolder viewHolder = (FillupItemViewHolder) convertView.getTag();
        if(viewHolder == null) {
            viewHolder = new FillupItemViewHolder();

            viewHolder.name = (TextView) convertView.findViewById(R.id.fillup_item_name);
            viewHolder.date = (TextView) convertView.findViewById(R.id.fillup_item_date);
            viewHolder.price = (TextView) convertView.findViewById(R.id.fillup_item_price);

            convertView.setTag(viewHolder);
        }

        Intervention intervention = getItem(position);

        DecimalFormat df = new DecimalFormat("#.#####");

        viewHolder.name.setText(df.format(intervention.getQuantity()) + " litres");
        viewHolder.date.setText(DateUtils.getRelativeTimeSpanString(intervention.getDate().getTime(), new Date().getTime(), DateUtils.SECOND_IN_MILLIS));
        viewHolder.price.setText(NumberFormat.getCurrencyInstance(getContext().getResources().getConfiguration().locale).format(intervention.getPrice()));

        return convertView;
    }

    class FillupItemViewHolder{
        public TextView name;
        public TextView date;
        public TextView price;
    }
}