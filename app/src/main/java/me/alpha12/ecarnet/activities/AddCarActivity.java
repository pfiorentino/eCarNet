package me.alpha12.ecarnet.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Date;

import me.alpha12.ecarnet.GlobalContext;
import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.adapters.DefaultSpinnerValueAdapter;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.CarModel;

public class AddCarActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String FROM_MAIN_ACTIVITY = "fma";

    private Spinner brandSpinner;
    private Spinner modelSpinner;
    private EditText mineTypeEditText;

    private Button vehicleButton;
    private Button mineButton;
    private Button cancelButton;
    private Button skipButton;

    private ArrayList<String> brandList;
    private ArrayList<String> modelList = new ArrayList<>();
    private ArrayAdapter<String> modelListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        brandSpinner = (Spinner) findViewById(R.id.brandSpinner);
        brandList = CarModel.findBrands(true);
        final ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, brandList);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(new DefaultSpinnerValueAdapter(brandAdapter, R.layout.default_spinner_value, "Sélectionnez une marque", this));
        brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modelSpinner.setEnabled(position > 0);
                if (position > 0) {
                    modelList.clear();
                    modelList.addAll(CarModel.findModelsByBrand(brandList.get(position - 1).toUpperCase(), true));
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
        vehicleButton.setOnClickListener(this);

        mineButton = (Button) findViewById(R.id.mineButton);
        mineButton.setOnClickListener(this);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        skipButton = (Button) findViewById(R.id.skipButton);
        if (getIntent().getBooleanExtra(FROM_MAIN_ACTIVITY, false)) {
            skipButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.VISIBLE);
            cancelButton.setOnClickListener(this);
        } else {
            cancelButton.setVisibility(View.GONE);
            skipButton.setVisibility(View.VISIBLE);
            skipButton.setOnClickListener(this);
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) { }
        @Override
        public void afterTextChanged(Editable editable) {
            mineButton.setEnabled(
                !mineTypeEditText.getText().toString().matches("") &&
                mineTypeEditText.getText().length() >= 3
            );
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode) {
            case GlobalContext.RESULT_CLOSE_ALL:
                setResult(GlobalContext.RESULT_CLOSE_ALL);
                finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.vehicleButton:
                intent = new Intent(AddCarActivity.this, SearchCarActivity.class);
                intent.putExtra("brand", brandSpinner.getSelectedItem().toString().toUpperCase());
                intent.putExtra("model", modelSpinner.getSelectedItem().toString().toUpperCase());

                if (getIntent().hasExtra("carId"))
                    intent.putExtra("carId", getIntent().getExtras().getInt("carId"));

                startActivityForResult(intent, 0);
                break;
            case R.id.mineButton:
                if (CarModel.existWithTypeMine(mineTypeEditText.getText().toString())) {
                    Intent mineIntent = new Intent(AddCarActivity.this, SearchCarActivity.class);
                    mineIntent.putExtra("mine", mineTypeEditText.getText().toString().toUpperCase());
                    if (getIntent().hasExtra("carId"))
                        mineIntent.putExtra("carId", getIntent().getExtras().getInt("carId"));
                    startActivityForResult(mineIntent, 0);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddCarActivity.this);
                    alertDialogBuilder.setMessage("Oups, aucun véhicule n'a été trouvé. Vérifiez le type mine ou cherchez par marque et modèle");
                    alertDialogBuilder.setNegativeButton("fermer", null);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                break;
            case R.id.cancelButton:
                AddCarActivity.this.setResult(GlobalContext.RESULT_CLOSE_ALL);
                AddCarActivity.this.finish();
                break;
            case R.id.skipButton:
                Car car = new Car(null, 0, null, null);
                car.persist();

                GlobalContext.setCurrentCar(car.getId());

                intent = new Intent(AddCarActivity.this, MainActivity.class);
                AddCarActivity.this.startActivity(intent);
                setResult(GlobalContext.RESULT_CLOSE_ALL);
                finish();
                break;
        }
    }
}
