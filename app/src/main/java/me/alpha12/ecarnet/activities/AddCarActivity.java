package me.alpha12.ecarnet.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.database.EcarnetHelper;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.Model;
import me.alpha12.ecarnet.models.User;

public class AddCarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    //values of spinners
    private ArrayList<String> brands = Model.getBrands(EcarnetHelper.bdd);
    private ArrayList<String> models = new ArrayList<String>();
    private ArrayList<Model> subModel = new ArrayList<Model>();

    //value of selected item;
    private String currentBrand;
    private String currentModel;

    private ArrayAdapter brandAdapter;
    private ArrayAdapter modelAdapter;
    private ArrayAdapter subModelAdapter;

    //spinners

    private Spinner subModelSpinner;
    private AutoCompleteTextView brandText;
    private AutoCompleteTextView modelText;


    private Button addCarButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentBrand = "";
        currentModel = "";

        //findView of spinners
        subModelSpinner = (Spinner)findViewById(R.id.sub_model_spinner);
        brandText = (AutoCompleteTextView)findViewById(R.id.brand);
        modelText = (AutoCompleteTextView)findViewById(R.id.model);
        modelText.setEnabled(false);
        subModelSpinner.setEnabled(false);


        addCarButton = (Button)findViewById(R.id.btn_addUser);
        addCarButton.setEnabled(false);

        //initialize default value
        brandAdapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, brands);
        brandText.setAdapter(brandAdapter);
        brandText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentBrand = parent.getItemAtPosition(position).toString();
                models = Model.getModelFromBrand(EcarnetHelper.bdd, currentBrand);
                modelAdapter = new ArrayAdapter(parent.getContext(), android.R.layout.simple_dropdown_item_1line, models);
                modelText.setAdapter(modelAdapter);
                modelText.setEnabled(true);
                subModel = new ArrayList<Model>();
                subModelAdapter = new ArrayAdapter(parent.getContext(), android.R.layout.simple_dropdown_item_1line, subModel);
                subModelSpinner.setAdapter(subModelAdapter);
            }
        });


        modelText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentModel = parent.getItemAtPosition(position).toString();
                subModel = Model.getModelFromBrandModel(EcarnetHelper.bdd, currentBrand, currentModel);
                subModelSpinner.setEnabled(true);
                subModelAdapter = new ArrayAdapter(parent.getContext(), android.R.layout.simple_dropdown_item_1line, subModel);
                subModelSpinner.setAdapter(subModelAdapter);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subModelSpinner.setOnItemSelectedListener(this);
        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Car.addCar(new Car(0, "CZ-123-PB", (Model)subModelSpinner.getSelectedItem()), EcarnetHelper.bdd);
                User.activateUser(EcarnetHelper.bdd);
                Toast.makeText(getBaseContext(), "added car : " + currentBrand + " - " + currentModel, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddCarActivity.this, MainActivity.class);
                AddCarActivity.this.startActivity(intent);
            }
        });

    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if(subModelSpinner.getSelectedItem() != null)
            addCarButton.setEnabled(true);
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
        return;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    }
