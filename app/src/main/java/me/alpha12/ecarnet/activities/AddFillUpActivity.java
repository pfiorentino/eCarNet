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
import me.alpha12.ecarnet.Utils;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.Intervention;

public class AddFillUpActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean isCalledFromTag = false;

    private static TextView mDateTextView;
    private Calendar mCurrentDate;
    private TextView kilometersTextView;
    private TextView amount;
    private TextView price;
    private Button addButton;
    private Button backButton;
    private Car currentCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fill_up);

        int carId = getIntent().getExtras().getInt("carId");
        currentCar = Car.findCarById(carId);

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
        mDateTextView.addTextChangedListener(mTextWatcher);

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

                    currentCar = Car.findCarById(carId);
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

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) { }
        @Override
        public void afterTextChanged(Editable editable) {
            addButton.setEnabled(
                    !kilometersTextView.getText().toString().matches("") &&
                    !amount.getText().toString().matches("") &&
                    !price.getText().toString().matches("") &&
                    !mDateTextView.getText().toString().matches("")
            );
        }
    };

    private static String getFormattedDate(Context ctx, Calendar c) {
        return DateUtils.formatDateTime(ctx, c.getTimeInMillis(),
                                        DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_ABBREV_WEEKDAY | DateUtils.FORMAT_ABBREV_MONTH);
    }

    private static String getFormattedTime(Context ctx, Calendar c) {
        return DateUtils.formatDateTime(ctx, c.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                onBackPressed();
                break;
            case R.id.addFillUpButton:
                SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM yyyy", Locale.FRENCH);
                try {
                    Date d = sdf.parse(mDateTextView.getText().toString());
                    int kilometers = Integer.parseInt(kilometersTextView.getText().toString());

                    Intervention inter = new Intervention(
                            0,
                            currentCar.getId(),
                            Intervention.TYPE_FILLUP,
                            "fill_up_intervention",
                            kilometers,
                            new Date(d.getTime()),
                            (double) Float.parseFloat(price.getText().toString()),
                            (double) Float.parseFloat(amount.getText().toString())
                        );
                    inter.persist();

                    currentCar.setKilometers(kilometers);
                    currentCar.update();

                    if (isCalledFromTag){
                        GlobalContext.pushNotification("eCarNet", "Le plein a bien été ajouté à votre "+currentCar.getCarModel().toString());
                    }

                    onBackPressed();
                    finish();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            final Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            mDateTextView.setText(getFormattedDate(this.getContext(), c));
        }
    }

}
