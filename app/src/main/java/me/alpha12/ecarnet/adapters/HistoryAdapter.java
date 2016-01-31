package me.alpha12.ecarnet.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Date;
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

            viewHolder.reminderLayout = (LinearLayout) convertView.findViewById(R.id.interventionLayout);
            viewHolder.selectedIcon = (ImageView) convertView.findViewById(R.id.selectedIcon);
            viewHolder.name = (TextView) convertView.findViewById(R.id.history_item_name);
            viewHolder.date = (TextView) convertView.findViewById(R.id.history_item_date);
            viewHolder.price = (TextView) convertView.findViewById(R.id.history_item_price);

            convertView.setTag(viewHolder);
        }

        Intervention intervention = getItem(position);

        if (intervention.isSelected()){
            viewHolder.reminderLayout.setBackgroundResource(R.color.listDividerColor);
            viewHolder.selectedIcon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.reminderLayout.setBackgroundResource(android.R.color.transparent);
            viewHolder.selectedIcon.setVisibility(View.GONE);
        }

        viewHolder.name.setText(intervention.getDescription());
        viewHolder.date.setText(DateUtils.getRelativeTimeSpanString(intervention.getDate().getTime(), new Date().getTime(), DateUtils.SECOND_IN_MILLIS));
        viewHolder.price.setText(NumberFormat.getCurrencyInstance(getContext().getResources().getConfiguration().locale).format(intervention.getPrice()));


        return convertView;
    }

    class HistoryItemViewHolder{
        public TextView name;
        public TextView date;
        public TextView price;
        public ImageView selectedIcon;
        public LinearLayout reminderLayout;

    }
}