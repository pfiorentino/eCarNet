package me.alpha12.ecarnet.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.adapters.CarAdapter;
import me.alpha12.ecarnet.models.Car;

public class CarsMgmtActivity extends MasterListActivity<Car> implements View.OnClickListener {
    public final static int CARS_DELETED_RESULT = 35564;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResId = R.layout.activity_cars_mgmt;
        super.onCreate(savedInstanceState);
        setDefaultTitle("Mes véhicules");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addCarFAB);
        fab.setOnClickListener(this);
    }

    @Override
    public void defineListAdapter() {
        adapter = new CarAdapter(this, itemsList);
    }

    @Override
    public void populateItemsList() {
        itemsList.addAll(Car.findAll());
    }

    @Override
    protected void deleteAllSelectedItems() {
        if (selectedItemsList.size() == itemsList.size()){
            String message;
            if (itemsList.size() > 1) {
                message = "Vous ne pouvez pas supprimer tous vos véhicules.\nL'utilisation d'eCarNet nécessite que vous en gardiez au moins un.";
            } else {
                message = "Impossible de supprimer votre dernier véhicule.";
            }

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setPositiveButton(getString(R.string.dialog_ok), null);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            super.deleteAllSelectedItems();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addCarFAB:
                Intent intent = new Intent(this, AddCarActivity.class);
                intent.putExtra(AddCarActivity.FROM_MAIN_ACTIVITY, true);
                startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    public void afterDelete() {
        super.afterDelete();
        setResult(CARS_DELETED_RESULT);
    }
}
