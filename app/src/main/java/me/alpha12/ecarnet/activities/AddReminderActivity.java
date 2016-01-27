package me.alpha12.ecarnet.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.alpha12.ecarnet.GlobalContext;
import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.adapters.NotificationPublisher;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.Intervention;
import me.alpha12.ecarnet.models.Reminder;

public class AddReminderActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private CheckBox notificationCheckBox;
    private TextView notificationTextView;
    private TextView modificationDateTextView;
    private EditText descriptionEditText;
    private EditText distanceLimitEditText;
    private TextView dateLimitTextView;
    private Button   addButton;

    private Reminder currentReminder;
    private Car currentCar;
    private Calendar selectedDate;
    private Calendar currentDate;


    private Calendar notificationDateAlarm = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        currentDate = Calendar.getInstance();

        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            notificationCheckBox = (CheckBox) findViewById(R.id.notificationCheckBox);
            notificationCheckBox.setEnabled(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB);
            notificationTextView = (TextView) findViewById(R.id.notificationTextView);
            notificationTextView.setEnabled(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB);

            modificationDateTextView = (TextView) findViewById(R.id.modificationDateTextView);
            descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
            distanceLimitEditText = (EditText) findViewById(R.id.distanceLimitEditText);
            dateLimitTextView = (TextView) findViewById(R.id.dateLimitTextView);

            addButton = (Button) findViewById(R.id.confirmButton);

            selectedDate = Calendar.getInstance();

            if (intent.getExtras().containsKey("memoId")) {
                getSupportActionBar().setTitle(getString(R.string.edit_reminder));
                modificationDateTextView.setVisibility(View.VISIBLE);

                currentReminder = Reminder.get(intent.getExtras().getInt("memoId"));
                currentCar = Car.get(currentReminder.getCarId());

                selectedDate = currentReminder.getLimitDate();

                descriptionEditText.setText(currentReminder.getTitle());
                distanceLimitEditText.setText(Integer.toString(currentReminder.getKilometers()));
                dateLimitTextView.setText(GlobalContext.getFormattedDate(currentReminder.getLimitDate()));

                notificationDateAlarm.setTimeInMillis(getBestValue(getAlarmWithDate(currentReminder.getLimitDate()), getAlarmWithDistance(currentReminder.getKilometers())));
                if(currentReminder.isNotifSet()) {
                    notificationCheckBox.setChecked(true);
                }
                modificationDateTextView.setText(getString(R.string.last_edit_memo, GlobalContext.getFormattedMediumDate(currentReminder.getModifDate())));
            } else if (intent.getExtras().containsKey("carId")) {
                getSupportActionBar().setTitle(getString(R.string.add_reminder));
                modificationDateTextView.setVisibility(View.GONE);

                currentCar = Car.get(getIntent().getExtras().getInt("carId"));
            }

            dateLimitTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = DatePickerFragment.newInstance(
                            AddReminderActivity.this.selectedDate,
                            AddReminderActivity.this
                    );
                    newFragment.show(getSupportFragmentManager(), "datePicker");
                }
            });

            descriptionEditText.addTextChangedListener(mTextWatcher);
            distanceLimitEditText.addTextChangedListener(mTextWatcher);
            notificationCheckBox.setOnClickListener(this);
            addButton.setOnClickListener(this);

            checkForm();
        }
    }

    private void checkForm() {
        boolean isFormValid =
                !descriptionEditText.getText().toString().matches("") &&
                !distanceLimitEditText.getText().toString().matches("") &&
                selectedDate != null;
        addButton.setEnabled(isFormValid);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        String distanceString = distanceLimitEditText.getText().toString();
        int distance = -1;
        if (!distanceString.equals("")) {
            distance = Integer.parseInt(distanceString);
        }

        switch (v.getId()) {
            case R.id.notificationCheckBox:
                if(notificationCheckBox.isChecked()) {
                    if (distance >= 0) {
                        notificationDateAlarm.setTimeInMillis(getBestValue(getAlarmWithDate(selectedDate), getAlarmWithDistance(distance)));
                    } else {
                        notificationDateAlarm.setTimeInMillis(getBestValue(0, getAlarmWithDate(selectedDate)));
                    }
                }
                break;
            case R.id.confirmButton:
                if(currentReminder == null) {
                    currentReminder = new Reminder(
                            -1,
                            descriptionEditText.getText().toString(),
                            currentDate,
                            currentDate,
                            selectedDate,
                            distance,
                            notificationCheckBox.isChecked(),
                            false,
                            currentCar.getId()
                    );
                } else {
                    currentReminder.setTitle(descriptionEditText.getText().toString());
                    currentReminder.setModifDate(currentDate);
                    currentReminder.setLimitDate(selectedDate);
                    currentReminder.setKilometers(distance);
                    currentReminder.setNotifSet(notificationCheckBox.isChecked());
                    currentReminder.setArchived(false);
                }

                currentReminder.persist(true);

                if (notificationCheckBox.isChecked()){
                    scheduleNotification(getNotification("Rappel sur intervention : " + descriptionEditText.getText().toString()), currentReminder.getId());
                } else {
                    cancelNotification(currentReminder.getId());
                }

                finish();
                break;
        }
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
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), (AddReminderActivity) getActivity(), year, month, day);
            return dialog;
        }

        private void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
            this.onDateSetListener = listener;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        selectedDate = Calendar.getInstance();
        selectedDate.set(year, monthOfYear, dayOfMonth);
        dateLimitTextView.setText(GlobalContext.getFormattedDate(selectedDate));
        checkForm();
    }

    private void scheduleNotification(Notification notification, int id) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, id);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(notificationDateAlarm.getTimeInMillis()-currentDate.getTimeInMillis() > 0) {
            long futureInMillis = SystemClock.elapsedRealtime() + (notificationDateAlarm.getTimeInMillis()-currentDate.getTimeInMillis());
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        }

    }

    private void cancelNotification(int id) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, id);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, notificationIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);
    }



    private Notification getNotification(String content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentTitle("Ecarnet - votre intervention");
            builder.setContentText(content);
            builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
            builder.setSmallIcon(R.drawable.ic_directions_car_white_24dp);
            return builder.build();
        } else {
            return null;
        }
    }

    private long getAlarmWithDate(Calendar limit) {
        if(currentDate.before(limit)) {
            return limit.getTimeInMillis();
        }
        else return 0;
    }

    private long getAlarmWithDistance(int distance) {
        float average = 0f;
        ArrayList<Intervention> myInterventions = Intervention.find10ByCar(currentCar.getId());
        if(myInterventions.size()>1) {
            for(int i=1; i<myInterventions.size(); i++) {
                long currentTime = (myInterventions.get(i).getDate().getTime() - myInterventions.get(i-1).getDate().getTime());
                if(currentTime != 0) {
                    int currentKilometers = myInterventions.get(i).getKilometers() - myInterventions.get(i-1).getKilometers();
                    average += (float)currentKilometers / (float)currentTime;
                }
            }
            average = average / myInterventions.size();
            long timeToReach = (long)(1/(average / (float)(distance - currentCar.getKilometers())));
            return timeToReach + currentDate.getTimeInMillis();
        }
        else return 0;
    }

    private long getBestValue(long valueA, long valueB) {
        if(valueA > valueB && valueB !=0) return valueB;
        if(valueB > valueA && valueA !=0) return valueA;
        return 0;
    }
}
