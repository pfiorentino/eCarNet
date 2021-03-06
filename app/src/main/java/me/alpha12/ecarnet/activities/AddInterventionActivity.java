package me.alpha12.ecarnet.activities;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.alpha12.ecarnet.GlobalContext;
import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.Intervention;
import me.alpha12.ecarnet.models.Reminder;

public class AddInterventionActivity extends AppCompatActivity implements OnClickListener, OnDateSetListener {
    private Calendar selectedDate;
    private Car currentCar;
    private Intervention currentIntervention;

    private Button confirmButton;
    private Button cancelButton;
    private EditText descEditText;
    private EditText kmEditText;
    private EditText priceEditText;
    private TextView dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_intervention);

        int carId = getIntent().getExtras().getInt("carId");

        currentCar = Car.get(carId);
        confirmButton   = (Button) findViewById(R.id.confirmButton);
        cancelButton    = (Button) findViewById(R.id.cancelButton);
        descEditText    = (EditText) findViewById(R.id.descEditText);
        kmEditText      = (EditText) findViewById(R.id.kmEditText);
        priceEditText   = (EditText) findViewById(R.id.priceEditText);
        dateTextView    = (TextView) findViewById(R.id.dateTextView);
        selectedDate = Calendar.getInstance();



        if (getIntent().getExtras().containsKey("interventionId")) {
            //getSupportActionBar().setTitle(getString(R.string.edit_intervention));

            currentIntervention = Intervention.get(getIntent().getExtras().getInt("interventionId"));
            Log.d("current", currentIntervention.getDate().toString());
            currentCar = Car.get(currentIntervention.getCarId());

            selectedDate.setTime(currentIntervention.getDate());

            descEditText.setText(currentIntervention.getDescription());
            kmEditText.setText(Integer.toString(currentIntervention.getKilometers()));
            priceEditText.setText(String.format(Locale.US, "%1$.2f", (float) currentIntervention.getPrice()));
            confirmButton.setEnabled(true);
        }


        confirmButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        descEditText.addTextChangedListener(mTextWatcher);
        kmEditText.addTextChangedListener(mTextWatcher);
        priceEditText.addTextChangedListener(mTextWatcher);

        dateTextView.setText(GlobalContext.getFormattedDate(selectedDate));
        dateTextView.setOnClickListener(this);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) { }
        @Override
        public void afterTextChanged(Editable editable) {
            confirmButton.setEnabled(
                    !kmEditText.getText().toString().matches("") &&
                    !priceEditText.getText().toString().matches("") &&
                    !descEditText.getText().toString().matches("")
            );
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dateTextView:
                DialogFragment newFragment = DatePickerFragment.newInstance(this.selectedDate, this);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.cancelButton:
                onBackPressed();
                break;
            case R.id.confirmButton:
                int kilometers = Integer.parseInt(kmEditText.getText().toString());

                if(currentIntervention == null)
                currentIntervention =  new Intervention(
                        0,
                        currentCar.getId(),
                        Intervention.TYPE_OTHER,
                        descEditText.getText().toString(),
                        kilometers,
                        selectedDate.getTime(),
                        Float.parseFloat(priceEditText.getText().toString()),
                        -1
                    );
                else {
                    currentIntervention.setDescription(descEditText.getText().toString());
                    currentIntervention.setPrice(Float.valueOf(priceEditText.getText().toString()));
                    currentIntervention.setDate(selectedDate.getTime());
                    currentIntervention.setKilometers(Integer.valueOf(kmEditText.getText().toString()));
                }
                currentIntervention.persist(true);

                currentCar.setKilometers(kilometers);
                currentCar.update();

                onBackPressed();
                finish();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        selectedDate = Calendar.getInstance();
        selectedDate.set(year, monthOfYear, dayOfMonth);
        dateTextView.setText(GlobalContext.getFormattedDate(selectedDate));
    }

    public static class DatePickerFragment extends DialogFragment {
        private OnDateSetListener onDateSetListener;

        public static DatePickerFragment newInstance(Calendar date, OnDateSetListener onDateSetListener) {
            DatePickerFragment pickerFragment = new DatePickerFragment();
            pickerFragment.setOnDateSetListener(onDateSetListener);

            //Pass the date in a bundle.
            Bundle bundle = new Bundle();
            bundle.putSerializable("SELECTED_DATE", date);
            pickerFragment.setArguments(bundle);
            return pickerFragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar initialDate = (Calendar) getArguments().getSerializable("SELECTED_DATE");

            final Calendar c;
            if (initialDate != null) {
                c = initialDate;
            } else {
                c = Calendar.getInstance();
            }

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), (AddInterventionActivity) getActivity(), year, month, day);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                dialog.getDatePicker().setMaxDate(new Date().getTime());
            }
            return dialog;
        }

        private void setOnDateSetListener(OnDateSetListener listener) {
            this.onDateSetListener = listener;
        }
    }
}
