package me.alpha12.ecarnet.fragments;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.activities.MainActivity;
import me.alpha12.ecarnet.adapters.SchedulerBuilder;
import me.alpha12.ecarnet.charts.BarChartCustom;
import me.alpha12.ecarnet.charts.LineChartCustom;
import me.alpha12.ecarnet.interfaces.OnFragmentInteractionListener;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.Intervention;
import me.alpha12.ecarnet.models.Note;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private int mMenuEntryId;
    private OnFragmentInteractionListener mListener;


    private boolean isDone;
    private boolean isNotifSet;
    private Note lastNote;

    private ArrayList<Intervention> myInterventions = new ArrayList<>();
    private ArrayList<Intervention> allMyInterventions = new ArrayList<>();
    private ArrayList<Intervention> myFillsUp = new ArrayList<>();

    private ImageButton notifButton;
    private ImageButton editButton;
    private ImageButton doneButton;
    private FrameLayout flagFrame;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param menuEntryId Drawer Menu Item Id.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(int menuEntryId) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivity.FRAGMENT_MENU_ENTRY_ID, menuEntryId);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMenuEntryId = getArguments().getInt(MainActivity.FRAGMENT_MENU_ENTRY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Car currentCar = ((MainActivity) getActivity()).currentCar;

        SchedulerBuilder alarm = new SchedulerBuilder();
        alarm.setAlarm(getContext());

        lastNote = Note.getLastNote(currentCar.getId());
        notifButton = (ImageButton) view.findViewById(R.id.button_notification);
        editButton = (ImageButton) view.findViewById(R.id.button_edit);
        doneButton = (ImageButton) view.findViewById(R.id.button_done);
        flagFrame = (FrameLayout) view.findViewById(R.id.flagFrame);

        TextView consumption = (TextView) view.findViewById(R.id.consumptionValue);
        LineChart kilometersLine = (LineChart) view.findViewById(R.id.kilometersChart);
        TextView kilometersText = (TextView) view.findViewById(R.id.kilometersData);

        LineChart costLine = (LineChart) view.findViewById(R.id.costChart);
        TextView costText = (TextView) view.findViewById(R.id.costData);

        TextView title = (TextView) view.findViewById(R.id.titleCar);
        title.setText(currentCar.getCarModel().toString());

        Calendar c = Calendar.getInstance();
        Date limit = c.getTime();
        limit.setDate(1);

        myFillsUp = Intervention.findInterventionByLimit(currentCar.getId(), limit);

        myInterventions = Intervention.find10ByCar(currentCar.getId());
        allMyInterventions = Intervention.findAllByCar(currentCar.getId());

        //prepare note if exist
        lastNote = new Note(0,"Vidange + filtre à diesel", limit, 15000, false, true, false, currentCar.getId());
        useNotedCard(view);

        if(myInterventions.size() != 0) {
            ArrayList<Entry> kilometersChart = getKilometers(myInterventions, currentCar);
            ArrayList<Entry> fillUpChart = getAmoutsFillUp();

            LineChartCustom kilometersLineCustom = new LineChartCustom(kilometersLine, kilometersChart, "", getLineChartLabels(myInterventions), null);


            int sum = 0;
            for (Entry value : kilometersChart)
            {
                sum += (int)value.getVal();
            }
            kilometersText.setText(Integer.toString(sum) + " km effectués");
            kilometersText.setTextSize(16);

            LineChartCustom costLineCustom = new LineChartCustom(costLine, fillUpChart, "", getLineChartLabels(myFillsUp), null);

            float floatSum = 0f;
            for(int i = 0; i<fillUpChart.size(); i++)
            {
                floatSum+= fillUpChart.get(i).getVal();
            }
            costText.setText(String.format("%.2f€", floatSum)+ " dépensés");
            costText.setTextSize(16);
            animateTextView(0.0f, getConsumption(currentCar), consumption);
        }
        return view;
    }

    public float getConsumption(Car current)
    {
        float totalConsumption = 0;
        float currentConsumption;
        int numberOfInters = 0;
        int oldKilometers = current.getKilometers();
        for(Intervention value : allMyInterventions) {
            if(value.getQuantity()!=0 && oldKilometers < value.getKilometers()) {
                currentConsumption = (float)value.getQuantity() * 100 / (value.getKilometers() - oldKilometers);
                Log.d("current consumption", ""+currentConsumption);
                totalConsumption +=currentConsumption;
                numberOfInters++;
                oldKilometers = value.getKilometers();
            }
        }
        Log.d("total consumption", ""+totalConsumption);
        Log.d("number of entries", ""+numberOfInters);
        Log.d("final consumption", ""+totalConsumption/numberOfInters);
        return totalConsumption/numberOfInters;
    }

    public ArrayList<String> getLineChartLabels(ArrayList<Intervention> myInterventions)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yy", Locale.FRENCH);
        ArrayList<String> labs = new ArrayList<>();
        for(Intervention value : myInterventions)
        {
            String dateOutput = sdf.format(value.getDateIntervention());
            labs.add(dateOutput);
        }
        return labs;
    }



    public void animateTextView(float initialValue, float finalValue, final TextView  textview) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(initialValue, finalValue);
        valueAnimator.setDuration(1800);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = Float.parseFloat(valueAnimator.getAnimatedValue().toString());
                textview.setText(String.format("%.1f", value));

            }
        });
        valueAnimator.start();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public ArrayList<Entry> getAmoutsFillUp()
    {
        ArrayList<Entry> amountsOfFillUp = new ArrayList<>();
        for(Integer i=0; i<myFillsUp.size(); i++)
            amountsOfFillUp.add(new Entry((float)myFillsUp.get(i).getPrice(), i));
        return amountsOfFillUp;
    }


    public ArrayList<Entry> getKilometers(ArrayList<Intervention> inters, Car current)
    {
        ArrayList<Entry> sumOfkilmeters = new ArrayList<>();
        int oldValue = current.getKilometers();
        int newValue = 0;
        for(int i=0; i<inters.size(); i++) {
            newValue = inters.get(i).getKilometers() - oldValue;
            sumOfkilmeters.add(new Entry(newValue, i));
            oldValue = inters.get(i).getKilometers();
        }
        return sumOfkilmeters;
    }


    private void useNotedCard(View view) {
        if (lastNote == null) {
            CardView card = (CardView) view.findViewById(R.id.noteCard);
            card.setVisibility(View.GONE);
        } else {
            TextView titleNote = (TextView) view.findViewById(R.id.titleNote);
            titleNote.setText(lastNote.getTitle());
            TextView kilometerNote = (TextView) view.findViewById(R.id.kilometersNote);
            kilometerNote.setText(lastNote.getKilometers() + " km");
            TextView dateNote = (TextView) view.findViewById(R.id.dateNote);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
            dateNote.setText(sdf.format(lastNote.getDateNote()));
            if (!lastNote.isNotifSet())
                notifButton.setBackgroundResource(R.drawable.ic_notifications_off_black_36dp);
            if (lastNote.isDone()) {
                doneButton.setBackgroundResource(R.drawable.ic_undo_black_36dp);
                flagFrame.setBackgroundColor(0xFFA5D6A7);
            } else {
                doneButton.setBackgroundResource(R.drawable.ic_done_black_36dp);
            }
            isNotifSet = lastNote.isNotifSet();
            isDone = lastNote.isDone();
            notifButton.setOnClickListener(this);
            doneButton.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button_notification :
                if(isNotifSet)
                {
                    notifButton.setBackgroundResource(R.drawable.ic_notifications_off_black_36dp);
                    isNotifSet = false;
                    //lastNote.setNotif(false);
                }
                else {

                    NotificationCompat.Builder notif = new NotificationCompat.Builder(getContext());
                    notif.setContentTitle("Test notif");
                    notif.setContentText("pensez à effectuer votre opération !!!");
                    notif.setSmallIcon(R.drawable.ic_directions_car_white_24dp);
                    notif.setDefaults(NotificationCompat.DEFAULT_SOUND);
                    Notification notification = notif.build();
                    NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    int notificationId = 0;
                    notificationManager.notify(notificationId, notification);

                    notifButton.setBackgroundResource(R.drawable.ic_notifications_black_36dp);
                    isNotifSet = true;
                    //lastNote.setNotif(true);
                }
                break;
            case R.id.button_done :
                if(isDone)
                {
                    //L'utilisateur undone la note
                    doneButton.setBackgroundResource(R.drawable.ic_done_black_36dp);
                    isDone = false;
                    flagFrame.setBackgroundColor(0xFFEF9A9A);
                    //lastNote.setDone(false);
                }
                else
                {
                    //L'uilisateur done la note
                    doneButton.setBackgroundResource(R.drawable.ic_undo_black_36dp);
                    isDone = true;
                    flagFrame.setBackgroundColor(0xFFA5D6A7);
                    //lastNote.setDone(true);
                }
        }
    }
}
