package me.alpha12.ecarnet.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
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
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.adapters.NotificationPublisher;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.Intervention;
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
        private Date alarmDate;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            int carId = getIntent().getExtras().getInt("carId");
            currentCar = Car.findCarById(carId);

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_memo);

            mCurrentDate = Calendar.getInstance();
            current = mCurrentDate.getTime();

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
                        Date d = sdf.parse(mDateTextView.getText().toString());
                        int kilometers = Integer.parseInt(kilometersLimit.getText().toString());
                        Memo memo = new Memo(0, title.getText().toString(), current, d, kilometers, notifActivated, false, false, currentCar.getId());
                        memo.persist(false);
                        Intent intent = new Intent(this, MainActivity.class);
                        if(notifActivated)
                        {
                            scheduleNotification(getNotification("Rappel sur intervention : " + title.getText().toString()), giveMeTheTimeBitch(d, kilometers), memo.getId());
                            intent.putExtra("dateRappel", sdf.format(alarmDate));
                        }
                        else
                        {
                            cancelNotification(memo.getId());
                        }
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


    private void scheduleNotification(Notification notification, long delay, int id) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, id);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
                ;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private void cancelNotification(int id)
    {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, id);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, notificationIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);
    }



    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Ecarnet - votre intervention");
        builder.setContentText(content);
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        builder.setSmallIcon(R.drawable.ic_directions_car_white_24dp);
        return builder.build();
    }


    private long giveMeTheTimeBitch(Date limit, int kilometers)
    {
        long timeRemining = 0;
        timeRemining = limit.getTime();
        Date setAlarm = new Date();


        float average = 0f;
        ArrayList<Intervention> inters = Intervention.find10ByCar(currentCar.getId());
        if (inters.size() == 10) {
            int kilometersOfCar = currentCar.getKilometers();
            int dayBeteweenInterventions = 0;
            int kilometersBeteweenInterventions = 0;
            Date oldDay = inters.get(0).getDateIntervention();
            int oldKilometers = inters.get(0).getKilometers();
            for (int i = 1; i < inters.size(); i++) {
                dayBeteweenInterventions = new Date(inters.get(i).getDateIntervention().getTime()-oldDay.getTime()).getDate();
                kilometersBeteweenInterventions = inters.get(i).getKilometers() - oldKilometers;
                average += kilometersBeteweenInterventions / dayBeteweenInterventions;
                oldDay = inters.get(i).getDateIntervention();
                oldKilometers = inters.get(i).getKilometers();
            }
            float globalAverage = average / inters.size();
            int toDoIn = kilometers-kilometersOfCar;
            float numbersOfDays = (globalAverage)/toDoIn;
            numbersOfDays = 1/numbersOfDays;
            Date alarm = new Date();
            alarm.setTime(current.getTime());
            alarm.setDate(setAlarm.getDate() + Math.round(numbersOfDays) - 5);
            if(timeRemining > alarm.getTime())
            {
                setAlarm.setDate(alarm.getDate() - 5);
            }
            else
            {
                setAlarm.setTime(timeRemining);
                setAlarm.setDate(setAlarm.getDate()-5);
            }
        }
        else
        {
            setAlarm.setTime(timeRemining);
            setAlarm.setDate(setAlarm.getDate()-5);
        }

        alarmDate = new Date();
        alarmDate.setTime(setAlarm.getTime());
        Log.d("Times : ", setAlarm.getTime()+ " - " + current.getTime());
        Log.d("estimated time", setAlarm.getTime() - current.getTime() + "");
        return setAlarm.getTime()-current.getTime();
    }
}
