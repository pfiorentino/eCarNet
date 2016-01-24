package me.alpha12.ecarnet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.activities.AddReminderActivity;
import me.alpha12.ecarnet.adapters.ReminderAdapter;
import me.alpha12.ecarnet.classes.MasterListFragment;
import me.alpha12.ecarnet.models.Reminder;

public class ReminderFragment extends MasterListFragment<Reminder> {
    public static ReminderFragment newInstance(int fragmentId) {
        ReminderFragment fragment = new ReminderFragment();
        fragment.setFragmentId(fragmentId);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleResourceId = R.string.title_fragment_memos;
        noItemTextResId = R.string.no_memo_found;
        registerFloatingActionButton(R.id.addMemoFAB);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addMemoFAB:
                Intent intent = new Intent(v.getContext(), AddReminderActivity.class);
                intent.putExtra("carId", currentCar.getId());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void defineListAdapter() {
        adapter = new ReminderAdapter(getContext(), itemsList);
    }

    @Override
    public void populateItemsList() {
        itemsList.addAll(Reminder.findAllByCar(currentCar.getId()));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(parent.getContext(), AddReminderActivity.class);
        intent.putExtra("memoId", itemsList.get(position).getId());
        parentActivity.startActivity(intent);
    }
}
