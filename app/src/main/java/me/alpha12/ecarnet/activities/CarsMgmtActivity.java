package me.alpha12.ecarnet.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.adapters.NFCTagAdapter;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.NFCTag;

public class CarsMgmtActivity extends MasterListActivity<NFCTag> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResId = R.layout.activity_cars_mgmt;
        super.onCreate(savedInstanceState);
        setDefaultTitle("Mes véhicules");
    }

    @Override
    public void defineListAdapter() {
        adapter = new NFCTagAdapter(this, itemsList);
    }

    @Override
    public void populateItemsList() {
        itemsList.addAll(NFCTag.findAll(NFCTag.DBModel.C_MESSAGE));
    }
}
