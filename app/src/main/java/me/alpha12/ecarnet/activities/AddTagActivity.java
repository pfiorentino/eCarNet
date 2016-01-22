package me.alpha12.ecarnet.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.adapters.NFCTagTypeAdapter;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.NFCTag;
import me.alpha12.ecarnet.models.NFCTagType;

public class AddTagActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<NFCTagType> tagTypes;
    private NFCTagType selectedType;
    private int currentCarId;

    private EditText tagNameEditText;
    private Button confirmButton;
    private GridView actionGrid;
    private NFCTagTypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        currentCarId = getIntent().getExtras().getInt("carId");

        tagTypes = new ArrayList<>();
        tagTypes.add(new NFCTagType("Nouveau plein", NFCTag.MIME_ADD_FILLUP, R.drawable.ic_local_gas_station_tblack_48dp));
        tagTypes.add(new NFCTagType("Nouvelle intervention", NFCTag.MIME_ADD_OPERATION, R.drawable.ic_local_car_wash_tblack_48dp));
        tagTypes.add(new NFCTagType("Nouveau memo", NFCTag.MIME_ADD_MEMO, R.drawable.ic_notifications_tblack_48dp));
        tagTypes.add(new NFCTagType("Infos du véhicule", NFCTag.MIME_CAR_INFO, R.drawable.ic_info_outline_tblack_48dp));

        tagNameEditText = (EditText) findViewById(R.id.tagNameEditText);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(this);

        actionGrid = (GridView) findViewById(R.id.action_grid);
        adapter = new NFCTagTypeAdapter(this, tagTypes);
        actionGrid.setAdapter(adapter);
        actionGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                selectedType = tagTypes.get(position);
                confirmButton.setEnabled(true);

                for (int i = 0; i < tagTypes.size(); i++){
                    if (i == position){
                        tagTypes.get(i).setSelected(true);
                    } else {
                        tagTypes.get(i).setSelected(false);
                    }
                }

                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmButton:
                String name = tagNameEditText.getText().toString();
                if (tagNameEditText.getText().toString().equals(""))
                    name = "Tag sans nom";

                NFCTag tag = new NFCTag(name, selectedType.getMimeType(), String.valueOf(currentCarId));
                tag.persist();
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
