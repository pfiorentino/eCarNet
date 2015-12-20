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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class AddCarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static TextView mTimeTextView;
    private static TextView mDateTextView;
    private static Calendar mCurrentDate;


    //values of spinners
    private ArrayList<String> brands = Model.getBrands(EcarnetHelper.bdd);
    private ArrayList<String> models = new ArrayList<String>();
    private ArrayList<String> years = new ArrayList<String>();
    private ArrayList<Model> subModel = new ArrayList<Model>();

    //value of selected item;
    private String currentBrand;
    private String currentModel;
    private String currentYear;
    private Model currentSubModel;


    //spinners
    private Spinner brandSpinner;
    private Spinner modelSpinner;
    private Spinner yearSpinner;
    private Spinner subModelSpinner;


    private Button addCarButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTimeTextView = (TextView)findViewById(R.id.kilometers);

        currentBrand = "";
        currentModel = "";

        //findView of spinners
        brandSpinner = (Spinner)findViewById(R.id.brand_spinner);
        modelSpinner = (Spinner)findViewById(R.id.model_spinner);
        yearSpinner = (Spinner)findViewById(R.id.year_spinner);
        subModelSpinner = (Spinner)findViewById(R.id.sub_model_spinner);

        addCarButton = (Button)findViewById(R.id.btn_addUser);
        addCarButton.setEnabled(false);

        //initialize default value
        brands.add(0, " Select Brand ");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, brands);
        brandSpinner.setAdapter(adapter);

        brandSpinner.setOnItemSelectedListener(this);
        modelSpinner.setOnItemSelectedListener(this);
        yearSpinner.setOnItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Car.addCar(new Car(0, "CZ-123-PB", (Model)subModelSpinner.getSelectedItem()), EcarnetHelper.bdd);
                Toast.makeText(getBaseContext(), "added car : " + brandSpinner.getSelectedItem().toString() + " - " + modelSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddCarActivity.this, MainActivity.class);
                AddCarActivity.this.startActivity(intent);
            }
        });

    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if(brandSpinner.getSelectedItem().toString() != " Select Brand ")
        {
            if(models.size()==0 || (brandSpinner.getSelectedItem().toString() != currentBrand))
            {
                models = Model.getModelFromBrand(EcarnetHelper.bdd, brandSpinner.getSelectedItem().toString());
                models.add(0, " Select Model ");
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, models);
                modelSpinner.setAdapter(adapter);
                years = new ArrayList<String>();
                adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, subModel);
                yearSpinner.setAdapter(adapter);
                subModel = new ArrayList<Model>();
                adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, subModel);
                subModelSpinner.setAdapter(adapter);
            }
            else if(modelSpinner.getSelectedItem().toString() != " Select Model ")
            {
                if(years.size()==0 || modelSpinner.getSelectedItem().toString() != currentModel) {
                    years = Model.getYearsFromBrandAndModel(EcarnetHelper.bdd, brandSpinner.getSelectedItem().toString(), modelSpinner.getSelectedItem().toString());
                    years.add(0, " Select Year ");
                    ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, years);
                    yearSpinner.setAdapter(adapter);
                    subModel = new ArrayList<Model>();
                    adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, subModel);
                    subModelSpinner.setAdapter(adapter);
                }
                else
                {
                    ArrayAdapter subModelAdapter;
                    if(yearSpinner.getSelectedItem().toString() == " Select Year ")
                    {
                        subModel = Model.getModelFromBrandModelYearAndRatedHP(EcarnetHelper.bdd, brandSpinner.getSelectedItem().toString(), modelSpinner.getSelectedItem().toString(), null);
                    }
                    else
                    {
                        if(yearSpinner.getSelectedItem().toString() != currentYear)
                        {
                            System.out.println("aqui");
                            subModel = Model.getModelFromBrandModelYearAndRatedHP(EcarnetHelper.bdd, brandSpinner.getSelectedItem().toString(), modelSpinner.getSelectedItem().toString(), yearSpinner.getSelectedItem().toString());
                        }
                    }
                    currentYear = yearSpinner.getSelectedItem().toString();
                    subModelAdapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, subModel);
                    subModelSpinner.setAdapter(subModelAdapter);
                }
                currentModel = modelSpinner.getSelectedItem().toString();
            }
            currentBrand = brandSpinner.getSelectedItem().toString();
        }
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


        private static String getFormattedDate(Context ctx, Calendar c) {
            return DateUtils.formatDateTime(ctx, c.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_ABBREV_WEEKDAY | DateUtils.FORMAT_ABBREV_MONTH);
        }

        private static String getFormattedTime(Context ctx, Calendar c) {
            return DateUtils.formatDateTime(ctx, c.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME);
        }

        public static class DatePickerFragment extends DialogFragment
                implements DatePickerDialog.OnDateSetListener {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Use the current date as the default date in the picker
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);

                // Create a new instance of DatePickerDialog and return it
                return new DatePickerDialog(getActivity(), this, year, month, 0);
            }

            public void onDateSet(DatePicker view, int year, int month, int day) {
                final Calendar c = Calendar.getInstance();
                c.set(year, month, 0);
                mDateTextView.setText(getFormattedDate(this.getContext(), c));
            }
        }

    }
