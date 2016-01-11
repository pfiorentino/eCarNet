package me.alpha12.ecarnet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.adapters.DefaultSpinnerValueAdapter;
import me.alpha12.ecarnet.models.CarModel;

public class AddCarActivity extends AppCompatActivity {
    private Spinner brandSpinner;
    private Spinner modelSpinner;
    private EditText mineTypeEditText;

    private Button vehicleButton;
    private Button mineButton;

    private ArrayList<String> brandList;
    private ArrayList<String> modelList = new ArrayList<>();
    private ArrayAdapter<String> modelListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        brandSpinner = (Spinner) findViewById(R.id.brandSpinner);
        brandList = CarModel.findBrands();
        final ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, brandList);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(new DefaultSpinnerValueAdapter(brandAdapter, R.layout.default_spinner_value, "Sélectionnez une marque", this));
        brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modelSpinner.setEnabled(position > 0);
                if (position > 0) {
                    modelList.clear();
                    modelList.addAll(CarModel.findModelsByBrand(brandList.get(position - 1)));
                    modelListAdapter.notifyDataSetChanged();
                    modelSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                modelSpinner.setEnabled(false);
            }
        });

        modelSpinner = (Spinner) findViewById(R.id.modelSpinner);
        modelList.add("Dummy model");
        modelListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, modelList);
        modelListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(new DefaultSpinnerValueAdapter(modelListAdapter, R.layout.default_spinner_value, "Sélectionnez un modèle", this));
        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vehicleButton.setEnabled(position > 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vehicleButton.setEnabled(false);
            }
        });

        mineTypeEditText = (EditText) findViewById(R.id.mineTypeEditText);
        mineTypeEditText.addTextChangedListener(mTextWatcher);

        vehicleButton = (Button) findViewById(R.id.vehicleButton);
        vehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCarActivity.this, SearchCarActivity.class);
                intent.putExtra("brand", brandSpinner.getSelectedItem().toString());
                intent.putExtra("model", modelSpinner.getSelectedItem().toString());
                startActivityForResult(intent, 0);
            }
        });

        mineButton = (Button) findViewById(R.id.mineButton);
        mineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCarActivity.this, SearchCarActivity.class);
                intent.putExtra("mine", mineTypeEditText.getText().toString());
                startActivityForResult(intent, 0);
            }
        });
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) { }
        @Override
        public void afterTextChanged(Editable editable) {
            mineButton.setEnabled(
                    !mineTypeEditText.getText().toString().matches("")
            );
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode) {
            case MainActivity.RESULT_CLOSE_ALL:
                setResult(MainActivity.RESULT_CLOSE_ALL);
                finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
