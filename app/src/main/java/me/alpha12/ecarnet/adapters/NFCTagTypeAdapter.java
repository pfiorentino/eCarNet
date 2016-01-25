package me.alpha12.ecarnet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.models.NFCTagType;

public class NFCTagTypeAdapter extends ArrayAdapter<NFCTagType> {
    private final List<NFCTagType> objects;
    private LayoutInflater mLayoutInflater;
    private Context context;

    public NFCTagTypeAdapter(Context context, List<NFCTagType> objects) {
        super(context, 0, objects);
        this.context = context;
        this.objects = objects;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.nfc_tag_type_item, null);
            holder = new ViewHolder();
            holder.imageView    = (ImageView) convertView.findViewById(R.id.tagTypeIcon);
            holder.textView     = (TextView) convertView.findViewById(R.id.tagTypeLabel);
            holder.checkmark    = (ImageView) convertView.findViewById(R.id.checkmark);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(objects.get(position).getLabel());
        if (objects.get(position).getIcon() != null)
            holder.imageView.setImageResource(objects.get(position).getIcon());
        else
            holder.imageView.setImageResource(R.drawable.ic_nfc_tblack_48dp);


        if (objects.get(position).isSelected()) {
            holder.checkmark.setVisibility(View.VISIBLE);
            holder.imageView.setBackgroundResource(R.drawable.selected_tag_type_border);
            holder.textView.setBackgroundColor(0x88F44336);
        } else {
            holder.checkmark.setVisibility(View.GONE);
            holder.imageView.setBackgroundColor(0x88FFFFFF);
            holder.textView.setBackgroundColor(0x1F000000);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public NFCTagType getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
        ImageView checkmark;
    }


}