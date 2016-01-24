package me.alpha12.ecarnet.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.activities.AddInterventionActivity;
import me.alpha12.ecarnet.activities.AddTagActivity;
import me.alpha12.ecarnet.activities.MainActivity;
import me.alpha12.ecarnet.activities.WriteTagActivity;
import me.alpha12.ecarnet.adapters.NFCTagAdapter;
import me.alpha12.ecarnet.classes.MasterFragment;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.NFCTag;

public class TagsFragment extends MasterFragment implements View.OnKeyListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private Menu menu;
    private ArrayAdapter<NFCTag> adapter;
    private ArrayList<NFCTag> tagsList;
    private ArrayList<NFCTag> selectedTagsList;

    private LinearLayout noTagTextLayout;
    private ImageView noTagImageView;
    private ListView tagsListView;

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

        tagsList = new ArrayList<>();
        selectedTagsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tags, container, false);
        view.setOnKeyListener(this);

        noTagTextLayout = (LinearLayout) view.findViewById(R.id.noTagTextLayout);
        noTagImageView = (ImageView) view.findViewById(R.id.noTagImageView);
        tagsListView = (ListView) view.findViewById(R.id.list);

        adapter = new NFCTagAdapter(view.getContext(), tagsList);

        tagsListView.setAdapter(adapter);
        tagsListView.setOnItemLongClickListener(this);
        tagsListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.tags_fragment_menu, menu);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteMenuItem:
                deleteAllSelectedItems();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        tagsList.clear();
        selectedTagsList.clear();
        tagsList.addAll(NFCTag.findAll(NFCTag.DBModel.C_MESSAGE));

        if (tagsList.size() > 0){
            noTagTextLayout.setVisibility(View.GONE);
            noTagImageView.setVisibility(View.GONE);
            tagsListView.setVisibility(View.VISIBLE);
        } else {
            noTagTextLayout.setVisibility(View.VISIBLE);
            noTagImageView.setVisibility(View.VISIBLE);
            tagsListView.setVisibility(View.GONE);
        }

        adapter.notifyDataSetChanged();
        refreshActionBar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addTagFAB:
                deselectAllItems();
                Intent intent = new Intent(v.getContext(), AddTagActivity.class);
                intent.putExtra("carId", currentCar.getId());
                startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if (selectedTagsList.size() > 0){
                deselectAllItems();
            } else {
                parentActivity.onBackPressed();
            }
            return true;
        }

        return false;
    }

    @Override
    public void setTitle() {
        if (selectedTagsList.size() > 0){
            if (selectedTagsList.size() > 1)
                parentActivity.setTitle(selectedTagsList.size()+" éléments");
            else
                parentActivity.setTitle(selectedTagsList.size()+" élément");
        } else {
            parentActivity.setTitle(R.string.title_fragment_tags);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        deselectAllItems();
        Intent intent = new Intent(parentActivity, WriteTagActivity.class);
        intent.putExtra("nfcTag", tagsList.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        NFCTag clickedTag = tagsList.get(position);

        if (clickedTag.isSelected()) {
            selectedTagsList.remove(clickedTag);
            clickedTag.setSelected(false);
        } else {
            selectedTagsList.add(clickedTag);
            clickedTag.setSelected(true);
        }

        adapter.notifyDataSetChanged();
        refreshActionBar();
        return true;
    }

    private void refreshActionBar() {
        if (menu != null)
            menu.findItem(R.id.deleteMenuItem).setVisible(selectedTagsList.size() > 0);
        setTitle();
    }

    private void deselectAllItems() {
        selectedTagsList.clear();
        for (NFCTag tag: tagsList) {
            tag.setSelected(false);
        }
        adapter.notifyDataSetChanged();
        refreshActionBar();
    }

    private void deleteAllSelectedItems() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(parentActivity);
        if (selectedTagsList.size() > 1)
            alertDialogBuilder.setMessage(getString(R.string.dialog_delete_elements, selectedTagsList.size()));
        else
            alertDialogBuilder.setMessage(getString(R.string.dialog_delete_element));

        alertDialogBuilder.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                for (NFCTag tag : selectedTagsList) {
                    tag.delete();
                    tagsList.remove(tag);
                }

                if (tagsList.size() > 0){
                    noTagTextLayout.setVisibility(View.GONE);
                    noTagImageView.setVisibility(View.GONE);
                    tagsListView.setVisibility(View.VISIBLE);
                } else {
                    noTagTextLayout.setVisibility(View.VISIBLE);
                    noTagImageView.setVisibility(View.VISIBLE);
                    tagsListView.setVisibility(View.GONE);
                }

                selectedTagsList.clear();
                adapter.notifyDataSetChanged();
                refreshActionBar();
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) { }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
