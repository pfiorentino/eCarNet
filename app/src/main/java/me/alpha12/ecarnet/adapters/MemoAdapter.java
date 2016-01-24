package me.alpha12.ecarnet.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.Utils;
import me.alpha12.ecarnet.activities.AddMemoActivity;
import me.alpha12.ecarnet.activities.CustomizeCarActivity;
import me.alpha12.ecarnet.activities.MainActivity;
import me.alpha12.ecarnet.models.CarModel;
import me.alpha12.ecarnet.models.Memo;

/**
 * Created by guilhem on 19/01/2016.
 */
public class MemoAdapter extends ArrayAdapter<Memo> {

    private List<Memo> memoL;
    private Context ctx;
    private Memo currentMemo;
    private FragmentActivity contextFragment;
    public MemoAdapter(Context ctx, ArrayList<Memo> values, FragmentActivity frag)
    {
        super(ctx, 0, values);
        contextFragment = frag;
        memoL = values;
        this.ctx = ctx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.custom_memo_listview, null);
        }
        currentMemo = getItem(position);
        if (currentMemo != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy", Locale.FRENCH);
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView dateSet = (TextView) v.findViewById(R.id.date);
            TextView limit = (TextView) v.findViewById(R.id.limit);

            if (title != null) {
                title.setText(Utils.ucWords(currentMemo.getTitle()));
            }

            if(dateSet != null) {
                dateSet.setText(Utils.ucWords(sdf.format(currentMemo.getDateNote())));
            }

            if(limit != null) {
                limit.setText(Utils.ucWords(currentMemo.getKilometers() + " km - " + sdf.format(currentMemo.getLimitDate())));
            }
            LinearLayout item = (LinearLayout) v.findViewById(R.id.memoSelection);
        }
        return v;
    }
}
