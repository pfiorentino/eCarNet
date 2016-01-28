package me.alpha12.ecarnet.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.models.Intervention;

public class HistoryAdapter extends ArrayAdapter<Intervention> {
    public HistoryAdapter(Context context, List<Intervention> interventionList) {
        super(context, 0, interventionList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_item, parent, false);
        }

        HistoryItemViewHolder viewHolder = (HistoryItemViewHolder) convertView.getTag();
        if(viewHolder == null) {
            viewHolder = new HistoryItemViewHolder();

            viewHolder.name = (TextView) convertView.findViewById(R.id.history_item_name);
            viewHolder.date = (TextView) convertView.findViewById(R.id.history_item_date);
            viewHolder.price = (TextView) convertView.findViewById(R.id.history_item_price);

            convertView.setTag(viewHolder);
        }

        Intervention intervention = getItem(position);

        viewHolder.name.setText(intervention.getDescription());
        viewHolder.date.setText(DateUtils.getRelativeDateTimeString(getContext(), intervention.getDate().getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.DAY_IN_MILLIS * 2, 0));
        viewHolder.price.setText(NumberFormat.getCurrencyInstance(getContext().getResources().getConfiguration().locale).format(intervention.getPrice()));

        return convertView;
    }

    class HistoryItemViewHolder{
        public TextView name;
        public TextView date;
        public TextView price;
    }
}