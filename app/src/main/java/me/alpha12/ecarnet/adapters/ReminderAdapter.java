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
import me.alpha12.ecarnet.models.Reminder;

/**
 * Created by guilhem on 19/01/2016.
 */
public class ReminderAdapter extends ArrayAdapter<Reminder> implements View.OnTouchListener {
    private final List<Reminder> objects;
    private LayoutInflater mLayoutInflater;
    private Context context;

    public ReminderAdapter(Context context, List<Reminder> objects) {
        super(context, 0, objects);
        this.context = context;
        this.objects = objects;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Reminder currentReminder = objects.get(position);

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.reminder_item, null);
            holder = new ViewHolder();
            holder.reminderLayout = (LinearLayout) convertView.findViewById(R.id.reminderLayout);
            holder.selectedIcon = (ImageView) convertView.findViewById(R.id.selectedIcon);
            holder.reminderNameTextView = (TextView) convertView.findViewById(R.id.reminderNameTextView);
            holder.reminderLimitTextView = (TextView) convertView.findViewById(R.id.reminderLimitTextView);
            holder.reminderCreationDateTextView = (TextView) convertView.findViewById(R.id.reminderCreationDateTextView);
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

        if (currentReminder.isSelected()){
            holder.reminderLayout.setBackgroundResource(R.color.listDividerColor);
            holder.selectedIcon.setVisibility(View.VISIBLE);
        } else {
            holder.reminderLayout.setBackgroundResource(android.R.color.transparent);
            holder.selectedIcon.setVisibility(View.GONE);
        }

        holder.reminderNameTextView.setText(currentReminder.getTitle());
        holder.reminderLimitTextView.setText(currentReminder.getLimitText());
        holder.reminderCreationDateTextView.setText(currentReminder.getCreationString());

        return convertView;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Reminder getItem(int position) {
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
        LinearLayout reminderLayout;
        ImageView selectedIcon;
        TextView reminderNameTextView;
        TextView reminderLimitTextView;
        TextView reminderCreationDateTextView;
        View bottomWhiteSpace;
    }
}
