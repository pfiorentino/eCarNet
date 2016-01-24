package me.alpha12.ecarnet.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.models.Intervention;

/**
 * Created by guilhem on 22/01/2016.
 */
public class OperationAdapter extends ArrayAdapter<Intervention> {

    private Context ctx;
    private Intervention currentIntervention;

    public OperationAdapter(Context ctx, ArrayList<Intervention> values, FragmentActivity frag)
    {
        super(ctx, 0, values);
        this.ctx = ctx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.custom_operation_data_table, null);
        }
        currentIntervention = getItem(position);
        if (currentIntervention != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM", Locale.FRENCH);
            TextView title = (TextView) v.findViewById(R.id.operationTitle);
            TextView dateOperation = (TextView) v.findViewById(R.id.dateOperation);
            TextView price = (TextView) v.findViewById(R.id.costOperation);

            if (title != null) {
                title.setText(currentIntervention.getDescription());
            }

            if(dateOperation != null) {
                dateOperation.setText(sdf.format(currentIntervention.getDate()));
            }

            if(price != null) {
                price.setText(String.format("%.2f", currentIntervention.getPrice()));
            }
        }
        return v;
    }
}
