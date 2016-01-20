package me.alpha12.ecarnet.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import me.alpha12.ecarnet.models.Memo;

/**
 * Created by guilhem on 19/01/2016.
 */
public class MemoRecyclerAdapter extends RecyclerView.Adapter<MemoRecyclerAdapter.ViewHolder> {

        List<Memo> memoL;

        public final static String EXTRA_CLICKED_ITEM = "clickedItem";

        private Context ctx;
        private ArrayList<Memo> values = new ArrayList<>();

        public MemoRecyclerAdapter(Context ctx, ArrayList<Memo> data) {
            this.ctx = ctx;
            this.values = data;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_memo_listview, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final Memo currentMemo = values.get(position);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy", Locale.FRENCH);

            if (currentMemo != null) {
                if (holder.title != null) {
                    holder.title.setText(Utils.ucWords(currentMemo.getTitle()));
                }
                if(holder.date != null) {
                    holder.date.setText(Utils.ucWords(sdf.format(currentMemo.getDateNote())));
                }
                if(holder.limit != null) {
                    holder.limit.setText(Utils.ucWords(sdf.format(currentMemo.getLimitDate()) + " - " + currentMemo.getKilometers()+" km"));
                }
            }
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, AddMemoActivity.class);
                    intent.putExtra("idMemo", currentMemo.getId());
                    ctx.startActivity(intent);                }
            });

        }

        @Override
        public int getItemCount() {
            return values.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public TextView date;
            public TextView limit;
            public LinearLayout item;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                date = (TextView) itemView.findViewById(R.id.date);
                limit = (TextView) itemView.findViewById(R.id.limit);
                item = (LinearLayout) itemView.findViewById(R.id.memoSelection);
            }
        }
}
