package me.alpha12.ecarnet.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.Intervention;

public class HistoryActivity extends AppCompatActivity {
    public static final String CAR_ID = "carid";

    private Car currentCar;
    //private List<Intervention> interventions;

    private TableLayout historyTable;
    private Button moreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        currentCar = Car.get(getIntent().getIntExtra(CAR_ID, 0));

        if (currentCar == null)
            finish();

/*
        // TODO remove
        new Intervention(0, currentCar.getId(), 0, "vidange et filtre a gazoil", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "courroie", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange et filtre a gazoil", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "courroie", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange et filtre a gazoil", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "courroie", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "courroie", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange et filtre a gazoil", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "courroie", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange et filtre a gazoil", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "courroie", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "courroie", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange et filtre a gazoil", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "courroie", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange et filtre a gazoil", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "courroie", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "courroie", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange et filtre a gazoil", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "courroie", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange et filtre a gazoil", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "vidange", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 0, "courroie", 15000, new Date(), 80.4, 45.3).persist();
        new Intervention(0, currentCar.getId(), 1, "(FILL UP)this not supposed to be displayed", 15000, new Date(), 80.4, 45.3).persist();
*/



        //interventions = ;

        historyTable = (TableLayout) findViewById(R.id.history_list);
        moreButton = (Button) findViewById(R.id.history_btn_more);

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillTable(Intervention.findOtherByCar(currentCar.getId()));
                moreButton.setVisibility(View.GONE);
            }
        });

        fillTable(Intervention.find10OtherByCar(currentCar.getId()));
    }

    private void fillTable(List<Intervention> interventions) {
        historyTable.removeAllViews();
        for (Intervention intervention : interventions) {
            TableRow row = new TableRow(getApplicationContext());
            TextView textView = new TextView(getApplicationContext());
            textView.setText(intervention.getDescription() + String.valueOf(intervention.getId()));
            row.addView(textView);
            historyTable.addView(row);
        }
    }
}
