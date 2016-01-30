package me.alpha12.ecarnet.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.database.DBObject;

public abstract class MasterListFragment<ItemsType extends DBObject> extends MasterFragment implements View.OnKeyListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    protected Menu menu;
    protected ArrayAdapter<ItemsType> adapter;
    protected ArrayList<ItemsType> itemsList;
    protected ArrayList<ItemsType> selectedItemsList;

    protected LinearLayout noItemTextLayout;
    protected ImageView noItemImageView;
    protected ListView listView;

    protected Integer noItemTextResId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        itemsList = new ArrayList<>();
        selectedItemsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_master_list, container, false);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(this);

        noItemTextLayout = (LinearLayout) view.findViewById(R.id.noItemTextLayout);
        noItemImageView = (ImageView) view.findViewById(R.id.noItemImageView);
        listView = (ListView) view.findViewById(R.id.listView);

        if (noItemTextResId != null)
            ((TextView) view.findViewById(R.id.noItemTextTitle)).setText(noItemTextResId);

        defineListAdapter();

        if (adapter == null) {
            Log.e("eCarNet", "You must define the list adapter with defineListAdapter method !");
        } else {
            listView.setAdapter(adapter);
        }

        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        itemsList.clear();
        selectedItemsList.clear();
        populateItemsList();

        if (itemsList.size() > 0){
            noItemTextLayout.setVisibility(View.GONE);
            noItemImageView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            noItemTextLayout.setVisibility(View.VISIBLE);
            noItemImageView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        adapter.notifyDataSetChanged();
        invalidateActionBar();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.master_list_fragment_menu, menu);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        deselectAllItems();
    }

    @Override
    public void onClick(View v) {
        deselectAllItems();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ItemsType clickedItem = itemsList.get(position);

        if (clickedItem.isSelected()) {
            selectedItemsList.remove(clickedItem);
            clickedItem.setSelected(false);
        } else {
            selectedItemsList.add(clickedItem);
            clickedItem.setSelected(true);
        }

        adapter.notifyDataSetChanged();
        invalidateActionBar();
        return true;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if (selectedItemsList.size() > 0){
                deselectAllItems();
            } else {
                parentActivity.onBackPressed();
            }
            return true;
        }

        return false;
    }

    public abstract void defineListAdapter();
    public abstract void populateItemsList();

    private void invalidateActionBar() {
        if (menu != null && menu.findItem(R.id.deleteMenuItem) != null)
            menu.findItem(R.id.deleteMenuItem).setVisible(selectedItemsList.size() > 0);

        if (selectedItemsList.size() > 0){
            if (selectedItemsList.size() > 1)
                parentActivity.setTitle(selectedItemsList.size()+" éléments");
            else
                parentActivity.setTitle(selectedItemsList.size()+" élément");
        } else {
            parentActivity.setTitle(getDefaultTitle());
        }
    }

    private void deselectAllItems() {
        selectedItemsList.clear();
        for (ItemsType item : itemsList) {
            item.setSelected(false);
        }
        adapter.notifyDataSetChanged();
        invalidateActionBar();
    }

    private void deleteAllSelectedItems() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(parentActivity);
        if (selectedItemsList.size() > 1)
            alertDialogBuilder.setMessage(getString(R.string.dialog_delete_elements, selectedItemsList.size()));
        else
            alertDialogBuilder.setMessage(getString(R.string.dialog_delete_element));

        alertDialogBuilder.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                for (ItemsType item : selectedItemsList) {
                    item.delete();
                    itemsList.remove(item);
                }

                if (itemsList.size() > 0) {
                    noItemTextLayout.setVisibility(View.GONE);
                    noItemImageView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                } else {
                    noItemTextLayout.setVisibility(View.VISIBLE);
                    noItemImageView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }

                selectedItemsList.clear();
                adapter.notifyDataSetChanged();
                invalidateActionBar();
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
