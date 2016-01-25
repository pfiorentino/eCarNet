package me.alpha12.ecarnet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.models.NFCTag;

public class NFCTagAdapter extends ArrayAdapter<NFCTag> implements View.OnTouchListener {
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
        NFCTag currentTag = objects.get(position);

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.nfc_tag_item, null);
            holder = new ViewHolder();
            holder.tagLayout        =   (LinearLayout) convertView.findViewById(R.id.tagLayout);
            holder.tagNameTextView  =       (TextView) convertView.findViewById(R.id.tagNameTextView);
            holder.tagTypeImageView =      (ImageView) convertView.findViewById(R.id.tagTypeIcon);
            holder.tagTypeTextView  =       (TextView) convertView.findViewById(R.id.tagTypeTextView);
            holder.sectionTextView  =       (TextView) convertView.findViewById(R.id.sectionTextView);
            holder.sectionTextView.setOnTouchListener(this);
            holder.sectionDivider   = convertView.findViewById(R.id.sectionDivider);
            holder.sectionDivider.setOnTouchListener(this);
            holder.bottomWhiteSpace = convertView.findViewById(R.id.bottomWhiteSpace);
            holder.bottomWhiteSpace.setOnTouchListener(this);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == objects.size()-1) {
            holder.bottomWhiteSpace.setVisibility(View.VISIBLE);
        } else {
            holder.bottomWhiteSpace.setVisibility(View.GONE);
        }

        if (position == 0 || !currentTag.getMessage().equals(objects.get(position-1).getMessage())) {
            holder.sectionTextView.setText(currentTag.getCar().toString());
            holder.sectionTextView.setVisibility(View.VISIBLE);
            holder.sectionDivider.setVisibility(View.VISIBLE);
        } else {
            holder.sectionTextView.setVisibility(View.GONE);
            holder.sectionDivider.setVisibility(View.GONE);
        }

        if (currentTag.isSelected())
            holder.tagLayout.setBackgroundResource(R.color.listDividerColor);
        else
            holder.tagLayout.setBackgroundResource(android.R.color.transparent);

        holder.tagNameTextView.setText(currentTag.getName());
        holder.tagTypeTextView.setText(currentTag.getMimeTypeDesc());
        if (currentTag.getMimeTypeIcon() != null)
            holder.tagTypeImageView.setImageResource(currentTag.getMimeTypeIcon());
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    static class ViewHolder {
        LinearLayout tagLayout;
        ImageView tagTypeImageView;
        TextView tagNameTextView;
        TextView tagTypeTextView;
        TextView sectionTextView;
        View sectionDivider;
        View bottomWhiteSpace;
    }
}