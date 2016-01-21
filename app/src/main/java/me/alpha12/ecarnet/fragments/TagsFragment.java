package me.alpha12.ecarnet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.activities.AddInterventionActivity;
import me.alpha12.ecarnet.activities.MainActivity;
import me.alpha12.ecarnet.classes.MasterFragment;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.NFCTag;

public class TagsFragment extends MasterFragment {
    private ArrayList<NFCTag> tagsList;

    private FloatingActionButton fab;

    public static TagsFragment newInstance(int fragmentId) {
        TagsFragment fragment = new TagsFragment();
        fragment.setFragmentId(fragmentId);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerFloatingActionButton(R.id.addTagFAB);

        setHasOptionsMenu(true);

        tagsList = NFCTag.findAll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tags, container, false);

        ListView listView = (ListView) view.findViewById(R.id.list);

        ArrayAdapter<NFCTag> adapter = new ArrayAdapter<>(
                view.getContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                tagsList);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.tags_fragment_menu, menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addTagFAB:
                Intent intent = new Intent(v.getContext(), AddInterventionActivity.class);
                intent.putExtra("carId", currentCar.getId());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void setTitle() {
        parentActivity.setTitle(R.string.title_fragment_tags);
    }
}
