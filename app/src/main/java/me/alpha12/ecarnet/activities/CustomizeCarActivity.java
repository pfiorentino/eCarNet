package me.alpha12.ecarnet.activities;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.CarModel;

/**
 * Created by guilhem on 04/01/2016.
 */
public class CustomizeCarActivity extends AppCompatActivity implements OnDateSetListener {
    private CarModel selectedCar;
    private Calendar selectedDate;

    private TextView titleTextView;
    private TextView dateTextView;

    private EditText imat;
    private EditText kilometers;

    private Button finishButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_car);

        int idModel = getIntent().getExtras().getInt("id");
        selectedCar = CarModel.findById(idModel);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        titleTextView.append(" "+selectedCar.getBrand()+" "+selectedCar.getModel());

        imat = (EditText) findViewById(R.id.imat);
        imat.addTextChangedListener(mTextWatcher);
        kilometers = (EditText) findViewById(R.id.kilometers);
        kilometers.addTextChangedListener(mTextWatcher);

        dateTextView = (TextView) findViewById(R.id.dateTextView);
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = DatePickerFragment.newInstance(
                        CustomizeCarActivity.this.selectedDate,
                        CustomizeCarActivity.this
                );
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        finishButton = (Button) findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car car = new Car(imat.getText().toString(), selectedCar);
                car.persist();

                Intent intent = new Intent(CustomizeCarActivity.this, MainActivity.class);
                CustomizeCarActivity.this.startActivity(intent);
                setResult(MainActivity.RESULT_CLOSE_ALL);
                finish();
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
            checkForm();
        }
    };

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        selectedDate = Calendar.getInstance();
        selectedDate.set(year, monthOfYear, dayOfMonth);
        dateTextView.setText(getFormattedDate(this, selectedDate));

        checkForm();
    }

    private void checkForm() {
        finishButton.setEnabled(
                !imat.getText().toString().matches("") &&
                        !kilometers.getText().toString().matches("") &&
                        selectedDate != null
        );
    }

    private static String getFormattedDate(Context ctx, Calendar c) {
        return DateUtils.formatDateTime(ctx, c.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_ABBREV_WEEKDAY | DateUtils.FORMAT_ABBREV_MONTH);
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
            return new DatePickerDialog(getActivity(), (CustomizeCarActivity) getActivity(), year, month, day);
        }

        private void setOnDateSetListener(OnDateSetListener listener) {
            this.onDateSetListener = listener;
        }
    }

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
