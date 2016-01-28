package me.alpha12.ecarnet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.adapters.CarAdapter;
import me.alpha12.ecarnet.models.Car;

public class CarsMgmtActivity extends MasterListActivity<Car> implements View.OnClickListener {
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addCarFAB:
                Intent intent = new Intent(this, AddCarActivity.class);
                intent.putExtra(AddCarActivity.FROM_MAIN_ACTIVITY, true);
                startActivityForResult(intent, 0);
                break;
        }
    }
}
