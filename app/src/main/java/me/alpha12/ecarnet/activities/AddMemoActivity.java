package me.alpha12.ecarnet.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.Memo;

/**
 * Created by guilhem on 17/01/2016.
 */
public class AddMemoActivity  extends AppCompatActivity implements View.OnClickListener {
        private static TextView mDateTextView;
        private Date current;
        private Calendar mCurrentDate;
        private TextView title;
        private TextView kilometersLimit;
        private Button addButton;
        private Button backButton;
        private Car currentCar;
        private Spinner notifyActive;
        private boolean notifActivated;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            int carId = getIntent().getExtras().getInt("idCar");
            currentCar = Car.findCarById(carId);

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_memo);

            mCurrentDate = Calendar.getInstance();
            current = mCurrentDate.getTime();
            Log.d("date courante", current.toString());

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

            title = (TextView) findViewById(R.id.titleMemo);
            title.addTextChangedListener(mTextWatcher);
            kilometersLimit = (TextView) findViewById(R.id.limit);
            kilometersLimit.addTextChangedListener(mTextWatcher);
            addButton = (Button) findViewById(R.id.addMemoButton);
            backButton = (Button) findViewById(R.id.backButton);
            backButton.setOnClickListener(this);
            addButton.setOnClickListener(this);

            notifyActive = (Spinner) findViewById(R.id.notifications);
            List<String> notifValues = new ArrayList<>();
            notifValues.add("non");
            notifValues.add("oui");
            final ArrayAdapter<String> notifAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, notifValues);
            notifyActive.setAdapter(notifAdapter);

            notifyActive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    notifActivated = (position==1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
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
                addButton.setEnabled(
                        !title.getText().toString().matches("") &&
                                !kilometersLimit.getText().toString().matches("") &&
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
                case R.id.addMemoButton:
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM yyyy", Locale.FRENCH);
                    try {
                        Log.d("date courante add memo", current.toString());
                        Date d = sdf.parse(mDateTextView.getText().toString());
                        int kilometers = Integer.parseInt(kilometersLimit.getText().toString());
                        Memo memo = new Memo(0, title.getText().toString(), current, d, kilometers, notifActivated, false, false, currentCar.getId());
                        memo.persist(false);
                        Intent intent = new Intent(this, MainActivity.class);
                        this.startActivity(intent);
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
