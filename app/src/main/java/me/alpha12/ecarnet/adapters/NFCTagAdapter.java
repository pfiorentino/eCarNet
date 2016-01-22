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
import me.alpha12.ecarnet.models.NFCTag;

public class NFCTagAdapter extends ArrayAdapter<NFCTag> {
    private final List<NFCTag> objects;
    private LayoutInflater mLayoutInflater;
    private Context context;

    public NFCTagAdapter(Context context, List<NFCTag> objects) {
        super(context, 0, objects);
        this.context = context;
        this.objects = objects;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.nfc_tag_item, null);
            holder = new ViewHolder();
            holder.tagNameTextView  = (TextView) convertView.findViewById(R.id.tagNameTextView);
            holder.tagTypeImageView = (ImageView) convertView.findViewById(R.id.tagTypeIcon);
            holder.tagTypeTextView  = (TextView) convertView.findViewById(R.id.tagTypeTextView);
            holder.sectionTextView  = (TextView) convertView.findViewById(R.id.sectionTextView);
            holder.sectionDivider   = convertView.findViewById(R.id.sectionDivider);
            holder.bottomWhiteSpace = convertView.findViewById(R.id.bottomWhiteSpace);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == objects.size()-1) {
            holder.bottomWhiteSpace.setVisibility(View.VISIBLE);
        } else {
            holder.bottomWhiteSpace.setVisibility(View.GONE);
        }

        if (position == 0 || !objects.get(position).getMessage().equals(objects.get(position-1).getMessage())) {
            holder.sectionTextView.setText(objects.get(position).getCar().toString());
            holder.sectionTextView.setVisibility(View.VISIBLE);
            holder.sectionDivider.setVisibility(View.VISIBLE);

        } else {
            holder.sectionTextView.setVisibility(View.GONE);
            holder.sectionDivider.setVisibility(View.GONE);
        }

        holder.tagNameTextView.setText(objects.get(position).getName());
        holder.tagTypeTextView.setText(objects.get(position).getMimeTypeString());
        if (objects.get(position).getMimeTypeIcon() != null)
            holder.tagTypeImageView.setImageResource(objects.get(position).getMimeTypeIcon());
        else
            holder.tagTypeImageView.setImageResource(R.drawable.ic_nfc_tblack_24dp);

        return convertView;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public NFCTag getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView tagTypeImageView;
        TextView tagNameTextView;
        TextView tagTypeTextView;
        TextView sectionTextView;
        View sectionDivider;
        View bottomWhiteSpace;
    }
}