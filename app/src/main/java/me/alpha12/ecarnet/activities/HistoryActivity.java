package me.alpha12.ecarnet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.adapters.HistoryAdapter;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.Intervention;

public class HistoryActivity extends MasterListActivity<Intervention> implements View.OnClickListener {
    public static final String CAR_ID = "carid";

    private Car currentCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResId = R.layout.activity_history;
        super.onCreate(savedInstanceState);
        currentCar = Car.get(getIntent().getIntExtra(CAR_ID, 0));
        if (currentCar == null)
            finish();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);

        Intent intent = new Intent(this, AddInterventionActivity.class);
        intent.putExtra("interventionId", itemsList.get(position).getId());
        startActivity(intent);
    }


    @Override
    public void defineListAdapter() {
        adapter = new HistoryAdapter(HistoryActivity.this, itemsList);
    }

    @Override
    public void populateItemsList() {
        itemsList.addAll(Intervention.findOtherByCar(currentCar.getId()));
    }

    @Override
    public void onClick(View v) {

    }
}
