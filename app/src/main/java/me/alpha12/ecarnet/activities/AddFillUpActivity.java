package me.alpha12.ecarnet.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.alpha12.ecarnet.GlobalContext;
import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.classes.Utils;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.Intervention;

public class AddFillUpActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private boolean isCalledFromTag = false;

    private TextView dateTextView;
    private TextView kilometersTextView;
    private TextView amount;
    private TextView price;
    private Button addButton;
    private Button backButton;
    private Car currentCar;

    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fill_up);

        int carId = getIntent().getExtras().getInt("carId");
        currentCar = Car.get(carId);

        selectedDate = Calendar.getInstance();

        dateTextView = (TextView) findViewById(R.id.fillup_date_text_view);
        dateTextView.setText(GlobalContext.getFormattedDate(selectedDate));
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = DatePickerFragment.newInstance(
                        AddFillUpActivity.this.selectedDate,
                        AddFillUpActivity.this
                );
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        kilometersTextView = (TextView) findViewById(R.id.total);
        kilometersTextView.addTextChangedListener(mTextWatcher);
        amount = (TextView) findViewById(R.id.quantity);
        amount.addTextChangedListener(mTextWatcher);
        price = (TextView) findViewById(R.id.price);
        price.addTextChangedListener(mTextWatcher);
        addButton = (Button) findViewById(R.id.addFillUpButton);
        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            Intent intent = getIntent();
            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
                String message = Utils.getNFCTagMessage(intent);
                try {
                    int carId = Integer.parseInt(message);

                    currentCar = Car.get(carId);
                    if (currentCar != null) {
                        GlobalContext.setCurrentCar(carId);
                        isCalledFromTag = true;
                    } else {
                        Log.e("eCarNet", "Car not found for this profile (id:"+carId+")");
                        this.onBackPressed();
                    }
                } catch (NumberFormatException e) {
                    Log.e("eCarNet", "Invalid tag message \"" + message + "\"");
                    this.onBackPressed();
                }
            }
        } else {
            Log.e("eCarNet", "NFC Tags are not supported by this API level ("+Build.VERSION.SDK_INT+")");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                onBackPressed();
                break;
            case R.id.addFillUpButton:
                int kilometers = Integer.parseInt(kilometersTextView.getText().toString());

                Intervention inter = new Intervention(
                        0,
                        currentCar.getId(),
                        Intervention.TYPE_FILLUP,
                        "fill_up_intervention",
                        kilometers,
                        selectedDate.getTime(),
                        Float.parseFloat(price.getText().toString()),
                        Float.parseFloat(amount.getText().toString())
                );
                inter.persist();

                currentCar.setKilometers(kilometers);
                currentCar.update();

                if (isCalledFromTag){
                    GlobalContext.pushNotification("eCarNet", "Le plein a bien été ajouté à votre "+currentCar.getCarModel().toString());
                }

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
        checkForm();
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) { }
        @Override
        public void afterTextChanged(Editable editable) {
            checkForm();
        }
    };

    private void checkForm() {
        addButton.setEnabled(
                !kilometersTextView.getText().toString().matches("") &&
                        !amount.getText().toString().matches("") &&
                        !price.getText().toString().matches("") &&
                        selectedDate != null
        );
    }

    public static class DatePickerFragment extends DialogFragment {
        private DatePickerDialog.OnDateSetListener onDateSetListener;

        public static DatePickerFragment newInstance(Calendar date, DatePickerDialog.OnDateSetListener onDateSetListener) {
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
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), (AddFillUpActivity) getActivity(), year, month, day);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                dialog.getDatePicker().setMaxDate(new Date().getTime());
            }
            return dialog;
        }

        private void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
            this.onDateSetListener = listener;
        }
    }
}
