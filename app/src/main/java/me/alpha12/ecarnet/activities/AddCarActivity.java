package me.alpha12.ecarnet.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.database.EcarnetHelper;
import me.alpha12.ecarnet.models.Model;

public class AddCarActivity extends AppCompatActivity {


    private static TextView mTimeTextView;
    private static TextView mDateTextView;
    private static Calendar mCurrentDate;
    private ArrayList<String> brands = Model.getBrands(EcarnetHelper.bdd);
    private ArrayList<String> models = new ArrayList<String>();
    private ArrayList<String> years = new ArrayList<String>();
    private Context cont = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //static set for tests

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCurrentDate = Calendar.getInstance();

        mDateTextView = (TextView) findViewById(R.id.date);
        mDateTextView.setText(getFormattedDate(this, mCurrentDate));
        mDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });


        final Spinner brandSpin = (Spinner) findViewById(R.id.brand_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, brands);
        brandSpin.setAdapter(adapter);

        brandSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                models = Model.getModelFromBrand(EcarnetHelper.bdd, brandSpin.getSelectedItem().toString());

                final Spinner modelSpin = (Spinner) findViewById(R.id.model_spinner);
                ArrayAdapter<String> modelAdapter = new ArrayAdapter<String>(cont, android.R.layout.simple_spinner_item, models);
                modelSpin.setAdapter(modelAdapter);

                modelSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        years = Model.getYearsFromBrandAndModel(EcarnetHelper.bdd, brandSpin.getSelectedItem().toString(), modelSpin.getSelectedItem().toString());
                        Spinner yearSpinner = (Spinner) findViewById(R.id.year_spinner);
                        ArrayAdapter<String> modelAdapter = new ArrayAdapter<String>(cont, android.R.layout.simple_spinner_item, years);
                        yearSpinner.setAdapter(modelAdapter);
                    }


                    public void onNothingSelected(AdapterView<?> adapterView) {
                        return;
                    }
                });
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });



 /*       modelSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (selectedItemView.toString() == "Clio") {
                    submodels.add("campus 65ch 1.0L");
                    submodels.add("sport 150ch 2.5L");
                }
                if (brandSpin.getSelectedItem().toString() == "206") {
                    submodels.add("xbox360 75ch 1.2L");
                    submodels.add("sport 180ch 2.5L");
                }
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
                submodels.add(" - ");
                return;
            }
        });

        Spinner submodelSpin = (Spinner) findViewById(R.id.submodel_spinner);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, submodels);
        brandSpin.setAdapter(adapter);
*/
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
