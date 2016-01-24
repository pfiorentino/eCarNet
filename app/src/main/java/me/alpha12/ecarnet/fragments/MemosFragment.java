package me.alpha12.ecarnet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.activities.AddMemoActivity;
import me.alpha12.ecarnet.adapters.MemoAdapter;
import me.alpha12.ecarnet.classes.MasterListFragment;
import me.alpha12.ecarnet.models.Memo;

public class MemosFragment extends MasterListFragment<Memo> implements View.OnKeyListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    public static MemosFragment newInstance(int fragmentId) {
        MemosFragment fragment = new MemosFragment();
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
                Intent intent = new Intent(v.getContext(), AddMemoActivity.class);
                intent.putExtra("carId", currentCar.getId());
                startActivity(intent);
                break;
        }
    }


    @Override
    public void defineListAdapter() {
        adapter = new MemoAdapter(getContext(), itemsList, getActivity());
    }

    @Override
    public void populateItemsList() {
        itemsList.addAll(Memo.findAllByCar(currentCar.getId()));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
