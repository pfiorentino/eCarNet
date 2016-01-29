package me.alpha12.ecarnet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.activities.AddTagActivity;
import me.alpha12.ecarnet.activities.WriteTagActivity;
import me.alpha12.ecarnet.adapters.NFCTagAdapter;
import me.alpha12.ecarnet.models.NFCTag;

public class TagsFragment extends MasterListFragment<NFCTag> {
    public static TagsFragment newInstance(int fragmentId) {
        TagsFragment fragment = new TagsFragment();
        fragment.setFragmentId(fragmentId);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        noItemTextResId = R.string.no_tag_found;
        registerFloatingActionButton(R.id.addTagFAB);

        setDefaultTitle(getString(R.string.title_fragment_tags));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.addTagFAB:
                Intent intent = new Intent(v.getContext(), AddTagActivity.class);
                intent.putExtra("carId", currentCar.getId());
                startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    public void defineListAdapter() {
        adapter = new NFCTagAdapter(getContext(), itemsList);
    }

    @Override
    public void populateItemsList() {
        itemsList.addAll(NFCTag.findAll(NFCTag.DBModel.C_CAR_ID));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);

        Intent intent = new Intent(parentActivity, WriteTagActivity.class);
        intent.putExtra("nfcTag", itemsList.get(position));
        startActivity(intent);
    }
}
