package me.alpha12.ecarnet.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.database.DBObject;

public abstract class MasterListActivity<ItemsType extends DBObject> extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    protected Menu menu;

    protected ArrayAdapter<ItemsType> adapter;
    protected ArrayList<ItemsType> itemsList;
    protected ArrayList<ItemsType> selectedItemsList;

    protected LinearLayout noItemTextLayout;
    protected ImageView noItemImageView;
    protected ListView listView;

    protected Integer noItemTextResId = null;
    protected Integer layoutResId = null;

    private String defaultTitle;
    private boolean hasFloatingActionButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemsList = new ArrayList<>();
        selectedItemsList = new ArrayList<>();

        if (layoutResId == null) {
            Log.e("eCarNet error", "You must specify layoutResId into sub-classes");
        } else {
            setContentView(layoutResId);
        }

        noItemTextLayout = (LinearLayout) findViewById(R.id.noItemTextLayout);
        noItemImageView = (ImageView) findViewById(R.id.noItemImageView);
        listView = (ListView) findViewById(R.id.listView);

        if (noItemTextResId != null)
            ((TextView) findViewById(R.id.noItemTextTitle)).setText(noItemTextResId);

        defineListAdapter();

        if (adapter == null) {
            Log.e("eCarNet", "You must define the list adapter with defineListAdapter method !");
        } else {
            listView.setAdapter(adapter);
        }

        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.master_list_fragment_menu, menu);
        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
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
    public void onBackPressed() {
        if (this.selectedItemsList.size() > 0){
            deselectAllItems();
        } else {
            super.onBackPressed();
        }
    }

    public abstract void defineListAdapter();
    public abstract void populateItemsList();

    private void invalidateActionBar() {
        if (menu != null && menu.findItem(R.id.deleteMenuItem) != null)
            menu.findItem(R.id.deleteMenuItem).setVisible(selectedItemsList.size() > 0);

        int actionBarResColor = this.getResources().getColor(R.color.colorPrimary);
        int statusBarResColor = this.getResources().getColor(R.color.colorPrimary700);

        if (selectedItemsList.size() > 0){
            actionBarResColor = this.getResources().getColor(R.color.selectionActionBarColor);
            statusBarResColor = this.getResources().getColor(R.color.selectionStatusBarColor);

            if (selectedItemsList.size() > 1)
                setTitle(selectedItemsList.size()+" éléments");
            else
                setTitle(selectedItemsList.size()+" élément");
        } else {
            setTitle(this.defaultTitle);
        }

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(actionBarResColor));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(statusBarResColor);
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

    protected void deleteAllSelectedItems() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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
                    if (hasFloatingActionButton)
                        noItemImageView.setVisibility(View.VISIBLE);
                    else
                        noItemImageView.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                }

                selectedItemsList.clear();
                adapter.notifyDataSetChanged();
                invalidateActionBar();

                afterDelete();
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.dialog_no), null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void setDefaultTitle(String value) {
        this.defaultTitle = value;
        invalidateActionBar();
    }

    public void hasFloatingActionButton(boolean hasFloatingActionButton) {
        this.hasFloatingActionButton = hasFloatingActionButton;
    }

    public void afterDelete() {

    }
}
