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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import me.alpha12.ecarnet.models.CarModel;
import me.alpha12.ecarnet.models.Intervention;
import me.alpha12.ecarnet.models.Memo;

/**
 * Created by guilhem on 17/01/2016.
 */
public class AddMemoActivity  extends AppCompatActivity implements View.OnClickListener {


    private final String STATE_TITLE = "title";
    private final String STATE_DISTANCE_LIMIT = "limitDistance";
    private final String STATE_DATE_SET = "addedDate";
    private final String STATE_DATE_LIMIT = "limitDate";
    private final String STATE_ID = "idMemo";
    private final String STATE_CAR_ID = "idCar";
    private final String STATE_NOTFICATION = "notif";
    private final String STATE_NOTIFICATION_DATE = "notifDate";
    private final String STATE_DATE_LAST_MODIFICATION = "lastModif";


    private static TextView mDateTextView;
    private TextView title;
    private TextView kilometersLimit;
    private TextView mlastModificationTextView;
    private Button addButton;
    private Button backButton;
    private CheckBox notification;
    private TextView mNotificationDateTextView;
    private TextView mLastModicationDateTextView;

    Memo memo;

    private Date current;
    private Date notificationDateAlarm = new Date();
    private Calendar mCurrentDate;

    private Car currentCar;
    private int currentMemoId;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_memo);

            mDateTextView = (TextView) findViewById(R.id.date);
            title = (TextView) findViewById(R.id.titleMemo);
            kilometersLimit = (TextView) findViewById(R.id.limit);
            addButton = (Button) findViewById(R.id.addMemoButton);
            backButton = (Button) findViewById(R.id.backButton);
            notification = (CheckBox) findViewById(R.id.eneableNotification);
            mNotificationDateTextView = (TextView) findViewById(R.id.dateNotification);
            mLastModicationDateTextView = (TextView) findViewById(R.id.lastModDate);
            mlastModificationTextView = (TextView) findViewById(R.id.lastModText);
            mCurrentDate = Calendar.getInstance();
            current = mCurrentDate.getTime();

            Intent intent = getIntent();
            if(intent.getExtras() != null && intent.getExtras().containsKey("isNewItem")){
                SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM yyyy", Locale.FRENCH);
                memo = Memo.findMemoById(intent.getExtras().getInt("idMemo"));
                title.setText(memo.getTitle());
                mDateTextView.setText(sdf.format(memo.getLimitDate()));
                kilometersLimit.setText(Integer.toString(memo.getKilometers()));
                currentCar = Car.findCarById(memo.getCarId());
                notificationDateAlarm.setTime(getBestValue(getAlarmWithDate(memo.getLimitDate()), getAlarmWithDistance(memo.getKilometers())));
                if(notificationDateAlarm.getTime()>0)
                {
                    mNotificationDateTextView.setText(sdf.format(notificationDateAlarm));
                    notification.setChecked(true);
                }
            }
            else    //creation
            {
                int carId = getIntent().getExtras().getInt("carId");
                currentCar = Car.findCarById(carId);
                mDateTextView.setText(getFormattedDate(this, mCurrentDate));
                mLastModicationDateTextView.setText("");
                mlastModificationTextView.setVisibility(View.GONE);
            }

            //listeners
            mDateTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "datePicker");
                }
            });
            mDateTextView.addTextChangedListener(mTextWatcher);
            title.addTextChangedListener(mTextWatcher);
            kilometersLimit.addTextChangedListener(mTextWatcher);
            backButton.setOnClickListener(this);
            addButton.setOnClickListener(this);
            notification.setOnClickListener(this);
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
                case R.id.eneableNotification:
                    if(notification.isChecked()) {
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM yyyy", Locale.FRENCH);
                        Date d = null;
                        try {
                            d = sdf.parse(mDateTextView.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (!kilometersLimit.getText().toString().equals("")) {
                            int kilometers = Integer.parseInt(kilometersLimit.getText().toString());

                            notificationDateAlarm.setTime(getBestValue(getAlarmWithDate(d), getAlarmWithDistance(kilometers)));
                        } else {
                            notificationDateAlarm.setTime(getBestValue(0, getAlarmWithDate(d)));
                        }
                        if(notificationDateAlarm.getTime()>0)
                            mNotificationDateTextView.setText(sdf.format(notificationDateAlarm));
                        else mNotificationDateTextView.setText("Limite invalide");
                    }
                    else mNotificationDateTextView.setText("");
                    break;
                case R.id.backButton:
                    onBackPressed();
                    break;
                case R.id.addMemoButton:
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM yyyy", Locale.FRENCH);
                    try {
                        Date d = sdf.parse(mDateTextView.getText().toString());
                        int kilometers = Integer.parseInt(kilometersLimit.getText().toString());
                        memo = new Memo(0, title.getText().toString(), d, current, kilometers, notification.isEnabled(), false, false, currentCar.getId());
                        memo.persist(false);
                        Intent intent = new Intent(this, MainActivity.class);
                        if(notification.isEnabled())
                        {
                            scheduleNotification(getNotification("Rappel sur intervention : " + title.getText().toString()), memo.getId());
                            intent.putExtra("dateRappel", sdf.format(notificationDateAlarm));
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


    private void scheduleNotification(Notification notification, int id) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, id);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(notificationDateAlarm.getTime()-current.getTime() > 0) {
            long futureInMillis = SystemClock.elapsedRealtime() + (notificationDateAlarm.getTime()-current.getTime());
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
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Ecarnet - votre intervention");
        builder.setContentText(content);
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        builder.setSmallIcon(R.drawable.ic_directions_car_white_24dp);
        return builder.build();
    }

    private long getAlarmWithDate(Date limit) {
        if(current.before(limit)) {
            return limit.getTime();
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
            return timeToReach + current.getTime();
        }
        else return 0;
    }

    private long getBestValue(long valueA, long valueB) {
        if(valueA > valueB && valueB !=0) return valueB;
        if(valueB > valueA && valueA !=0) return valueA;
        return 0;
    }
}
