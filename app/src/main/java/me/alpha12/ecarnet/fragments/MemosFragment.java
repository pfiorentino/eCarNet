package me.alpha12.ecarnet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.activities.AddMemoActivity;
import me.alpha12.ecarnet.adapters.MemoAdapter;
import me.alpha12.ecarnet.classes.MasterFragment;
import me.alpha12.ecarnet.models.Memo;

public class MemosFragment extends MasterFragment {
    private ListView myMemos;

    public static MemosFragment newInstance(int fragmentId) {
        MemosFragment fragment = new MemosFragment();
        fragment.setFragmentId(fragmentId);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerFloatingActionButton(R.id.addMemoFAB);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo, container, false);

        ArrayList<Memo> memoList = Memo.findAllByCar(currentCar.getId());

        if(!memoList.isEmpty()) {
            myMemos = (ListView) view.findViewById(R.id.memoList);
            myMemos.setAdapter(new MemoAdapter(getContext(), memoList, getActivity()));
        }
        return view;
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
    public void setTitle() {
        parentActivity.setTitle(R.string.title_fragment_memos);
    }
}
