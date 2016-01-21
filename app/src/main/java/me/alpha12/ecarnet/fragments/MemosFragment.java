package me.alpha12.ecarnet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.activities.AddMemoActivity;
import me.alpha12.ecarnet.classes.MasterFragment;

public class MemosFragment extends MasterFragment {
    public static ShareFragment newInstance(int fragmentId) {
        ShareFragment fragment = new ShareFragment();
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
        return inflater.inflate(R.layout.fragment_memo, container, false);
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
        parentActivity.setTitle(R.string.title_fragment_share);
    }
}
