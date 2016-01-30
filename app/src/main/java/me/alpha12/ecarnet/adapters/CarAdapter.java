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
import me.alpha12.ecarnet.classes.Utils;
import me.alpha12.ecarnet.models.Car;

public class CarAdapter extends ArrayAdapter<Car> implements View.OnTouchListener {
    private final List<Car> objects;
    private LayoutInflater mLayoutInflater;
    private Context context;

    public CarAdapter(Context context, List<Car> objects) {
        super(context, 0, objects);
        this.context = context;
        this.objects = objects;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Car currentCar = objects.get(position);

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.car_item, null);
            holder = new ViewHolder();
            holder.tagLayout        =   (LinearLayout) convertView.findViewById(R.id.tagLayout);
            holder.tagNameTextView  =       (TextView) convertView.findViewById(R.id.tagNameTextView);
            holder.tagTypeImageView =      (ImageView) convertView.findViewById(R.id.tagTypeIcon);
            holder.tagDetailsTextView =     (TextView) convertView.findViewById(R.id.tagDetailsTextView);
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

        if (position == 0 || !(currentCar.getType() == objects.get(position-1).getType())) {
            holder.sectionTextView.setText(currentCar.getType() == Car.OWNED_CAR ? "Mes voitures" : "Voitures partagées");
            holder.sectionTextView.setVisibility(View.VISIBLE);
            holder.sectionDivider.setVisibility(View.VISIBLE);
        } else {
            holder.sectionTextView.setVisibility(View.GONE);
            holder.sectionDivider.setVisibility(View.GONE);
        }

        if (currentCar.isSelected()) {
            holder.tagLayout.setBackgroundResource(R.color.listDividerColor);
            holder.tagTypeImageView.setImageResource(R.drawable.ic_check_circle_black_54pc_40dp);
        } else {
            holder.tagLayout.setBackgroundResource(android.R.color.transparent);
            holder.tagTypeImageView.setImageDrawable(currentCar.getCarPicture(context));
        }

        holder.tagNameTextView.setText(currentCar.getModelString());
        if (currentCar.getCarModel() != null) {
            holder.tagTypeTextView.setVisibility(View.VISIBLE);
            holder.tagTypeTextView.setText(Utils.ucWords(currentCar.getCarModel().getDetails()));
        } else {
            holder.tagTypeTextView.setVisibility(View.GONE);
        }
        holder.tagDetailsTextView.setText(currentCar.getDetails());

        return convertView;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Car getItem(int position) {
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
        TextView tagDetailsTextView;
        TextView tagTypeTextView;
        TextView sectionTextView;
        View sectionDivider;
        View bottomWhiteSpace;
    }
}