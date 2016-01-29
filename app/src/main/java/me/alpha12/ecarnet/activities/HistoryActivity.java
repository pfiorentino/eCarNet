package me.alpha12.ecarnet.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.adapters.HistoryAdapter;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.Intervention;

public class HistoryActivity extends AppCompatActivity {
    public static final String CAR_ID = "carid";

    private Car currentCar;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        currentCar = Car.get(getIntent().getIntExtra(CAR_ID, 0));

        if (currentCar == null)
            finish();

        listView = (ListView) findViewById(R.id.history_list_view);

        HistoryAdapter adapter = new HistoryAdapter(HistoryActivity.this, Intervention.findOtherByCar(currentCar.getId()));
        listView.setAdapter(adapter);
    }
}
