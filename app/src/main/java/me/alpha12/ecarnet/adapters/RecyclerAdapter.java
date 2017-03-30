package me.alpha12.ecarnet.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.alpha12.ecarnet.R;

/**
 * Created by paul on 26/10/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    public final static String EXTRA_CLICKED_ITEM = "clickedItem";

    private final Context ctx;
    private final String[] values;

    public RecyclerAdapter(Context ctx, String[] data) {
        this.ctx = ctx;
        this.values = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(v);*/

        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.text.setText(values[position]);
        holder.itemView.setClickable(true);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(ctx, DetailsActivity.class);
                intent.putExtra(EXTRA_CLICKED_ITEM, values[position]);
                ctx.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView;
        }
    }
}
