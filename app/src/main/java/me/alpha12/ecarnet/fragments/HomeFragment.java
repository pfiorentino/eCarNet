package me.alpha12.ecarnet.fragments;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private LineChart mainChart;

    private boolean isDone;
    private boolean isNotifSet;
    private Note lastNote;

    private OnFragmentInteractionListener mListener;


    private ArrayList<Intervention> myInterventions = new ArrayList<>();
    private ArrayList<Intervention> allMyInterventions = new ArrayList<>();
    private ArrayList<Intervention> myFillsUp = new ArrayList<>();
    private ArrayList<Intervention> myFixes = new ArrayList<>();
    private ArrayList<String> barLabels = new ArrayList<>();


    private ImageButton notifButton;
    private ImageButton editButton;
    private ImageButton deleteButton;
    private ImageButton doneButton;
    private ImageView flag;
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

        mainChart = (LineChart) view.findViewById(R.id.chart);
        TextView title = (TextView) view.findViewById(R.id.titleCar);

        //about notes
        lastNote = Note.getLastNote(currentCar.getId());
        notifButton = (ImageButton) view.findViewById(R.id.button_notification);
        editButton = (ImageButton) view.findViewById(R.id.button_edit);
        deleteButton = (ImageButton) view.findViewById(R.id.button_delete);
        doneButton = (ImageButton) view.findViewById(R.id.button_done);
        flag = (ImageView) view.findViewById(R.id.flagDone);



        TextView consumption = (TextView) view.findViewById(R.id.consumptionValue);
        LineChart kilometersLine = (LineChart) view.findViewById(R.id.kilometersChart);
        //CardView kilometersCard = (CardView) view.findViewById(R.id.kilometerscard);
        TextView kilometersText = (TextView) view.findViewById(R.id.kilometersData);
        BarChart costLine = (BarChart) view.findViewById(R.id.costChart);
        TextView costText = (TextView) view.findViewById(R.id.costData);

        title.setText(currentCar.getCarModel().toString());

        Calendar c = Calendar.getInstance();
        Date limit = c.getTime();
        limit.setDate(1);

        myFillsUp = Intervention.findFillUpByLimit(currentCar.getId(), limit);


        myFixes = Intervention.findFixesByLimit(currentCar.getId(), limit);

        myInterventions = Intervention.find10ByCar(currentCar.getId());
        allMyInterventions = Intervention.findAllByCar(currentCar.getId());

        //fake data
        lastNote = new Note(1, "Vidange", new Date(116, 0, 17), 13000, false, false, false, currentCar.getId());
        myInterventions.add(new Intervention(currentCar.getId(), 10123, 5, 12.5, new Date(114, 11, 12)));
        myInterventions.add(new Intervention(currentCar.getId(), 10223, 50.25, 15.5, new Date(115, 0, 12)));
        myInterventions.add(new Intervention(currentCar.getId(), 10456, 50.25, 18.5, new Date(115, 2, 12)));
        myInterventions.add(new Intervention(currentCar.getId(), 10710, 200.43, 0, new Date(115, 3, 12)));
        myInterventions.add(new Intervention(currentCar.getId(), 11070, 50.90, 15, new Date(115, 3, 12)));
        myInterventions.add(new Intervention(currentCar.getId(), 11340, 108.23, 0, new Date(115, 4, 12)));
        myInterventions.add(new Intervention(currentCar.getId(), 11390, 200, 0, new Date(115, 5, 12)));
        myInterventions.add(new Intervention(currentCar.getId(), 11701, 60.83, 25.3, new Date(116, 0, 1)));
        myInterventions.add(new Intervention(currentCar.getId(), 11925, 120, 0, new Date(116, 0, 8)));
        myInterventions.add(new Intervention(currentCar.getId(), 12780, 100.33, 0, new Date(116, 0, 12)));
        allMyInterventions = myInterventions;

        myFillsUp.add(new Intervention(currentCar.getId(), 11701, 60.83, 25.3, new Date(116, 0, 1)));
        myFillsUp.add(new Intervention(currentCar.getId(), 11701, 100.83, 25.3, new Date(116, 0, 1)));
        myFixes.add(new Intervention(currentCar.getId(), 11925, 120, 0, new Date(116, 0, 8)));
        myFixes.add(new Intervention(currentCar.getId(), 12780, 100.33, 0, new Date(116, 0, 12)));

        barLabels = getBarChartLabels(limit);

        if(lastNote == null)
        {
            CardView card = (CardView)view.findViewById(R.id.noteCard);
            card.setVisibility(View.GONE);
        }
        else
        {
            TextView titleNote = (TextView) view.findViewById(R.id.titleNote);
            titleNote.setText(lastNote.getTitle());
            TextView kilometerNote = (TextView) view.findViewById(R.id.kilometersNote);
            kilometerNote.setText(lastNote.getKilometers() + " km");
            TextView dateNote = (TextView) view.findViewById(R.id.dateNote);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
            dateNote.setText(sdf.format(lastNote.getDateNote()));
            if(!lastNote.isNotifSet())
                notifButton.setBackgroundResource(R.drawable.ic_notifications_off_black_24dp);
            if(lastNote.isDone()) {
                doneButton.setBackgroundResource(R.drawable.ic_done_black_24dp);
                flag.setBackgroundResource(R.drawable.ic_bookmark_green_400_24dp);
            }
            isNotifSet = lastNote.isNotifSet();
            isDone = lastNote.isDone();
            notifButton.setOnClickListener(this);
            doneButton.setOnClickListener(this);
        }

        if(myInterventions.size() != 0) {
            ArrayList<String> labels = getLineChartLabels();
            ArrayList<Entry> kilometersChart = getKilometers(myInterventions, currentCar);
            ArrayList<BarEntry> fillUpChart = getAmoutsFillUp();
            ArrayList<BarEntry> interventionChart = getAmoutsIntervention();

            //tempory affectation
            LineChartCustom kilometersLineCustom = new LineChartCustom(kilometersLine, kilometersChart, "", labels, null);


            int sum = 0;
            for (Entry value : kilometersChart)
            {
                sum += (int)value.getVal();
            }
            kilometersText.setText(Integer.toString(sum) + " km");
            kilometersText.setTextSize(16);

            BarChartCustom costLineCustom = new BarChartCustom(costLine, interventionChart, "", barLabels, null);
            costLineCustom.addEntries(fillUpChart);

            float floatSum = 0f;
            for(int i = 0; i<fillUpChart.size(); i++)
            {
                floatSum+= fillUpChart.get(i).getVal();
            }
            for(int i = 0; i<interventionChart.size(); i++)
            {
                floatSum+= interventionChart.get(i).getVal();
            }
            costText.setText(String.format("%.2f€", floatSum));
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
                totalConsumption +=currentConsumption;
                numberOfInters++;
                oldKilometers = value.getKilometers();
            }
        }
        return totalConsumption/numberOfInters;
    }

    public float[] additionAllPrices(ArrayList<Intervention> inters)
    {
        float values[] = new float[barLabels.size()];
        for(Intervention value : inters)
        {
            Integer key = value.getDateIntervention().getDate();
            values[key-1] += value.getPrice();
        }
        return values;
    }

    public ArrayList<String> getLineChartLabels()
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


    public ArrayList<String> getBarChartLabels(Date limit)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy", Locale.FRENCH);
        ArrayList<String> labs = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        while(limit.before(now))
        {
            String dateOutput = sdf.format(limit);
            labs.add(dateOutput);
            limit.setDate(limit.getDate()+1);
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


    public int[] getSumKilometers(ArrayList<Intervention> inters, Car current)
    {
        //-----------this function will regroup kilometers of interventino monthly and return into a Hashmap
        int[] values = new int[12];
        int oldkilometers = current.getKilometers();
        int newkilometers = 0;
        for(int i =0; i<inters.size(); i++) {
            newkilometers = inters.get(i).getKilometers() - oldkilometers;
            int indice = inters.get(i).getDateIntervention().getMonth();
            values[indice] += newkilometers;
            oldkilometers = inters.get(i).getKilometers();
        }
        return values;
    }

    public ArrayList<BarEntry> getAmoutsFillUp()
    {
        ArrayList<BarEntry> amountsOfFillUp = new ArrayList<>();
        float[] sums = additionAllPrices(myFillsUp);
        for(Integer i=0; i<sums.length; i++)
            amountsOfFillUp.add(new BarEntry((float)sums[i], i));
        return amountsOfFillUp;
    }

    public ArrayList<BarEntry> getAmoutsIntervention()
    {
        ArrayList<BarEntry> amountsOfIntervention = new ArrayList<>();
        float[] sums = additionAllPrices(myFixes);
        for(Integer i=0; i<sums.length; i++)
            amountsOfIntervention.add(new BarEntry((float)sums[i], i));
        return amountsOfIntervention;
    }


    public ArrayList<Entry> getKilometers(ArrayList<Intervention> inters, Car current)
    {
        ArrayList<Entry> sumOfkilmeters = new ArrayList<>();
        int[] fillUpTab = getSumKilometers(inters, current);
        for(int i=0; i<fillUpTab.length; i++) {
            if(fillUpTab[i]!=0 || i==0 || i==11)
                sumOfkilmeters.add(new BarEntry(fillUpTab[i], i));
        }
        return sumOfkilmeters;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button_notification :
                if(isNotifSet)
                {
                    notifButton.setBackgroundResource(R.drawable.ic_notifications_off_black_24dp);
                    isNotifSet = false;
                    //lastNote.setNotif(false);
                }
                else {
                    notifButton.setBackgroundResource(R.drawable.ic_notifications_black_24dp);
                    isNotifSet = true;
                    //lastNote.setNotif(true);
                }
                break;
            case R.id.button_done :
                if(isDone)
                {
                    Log.d("ok", "button_done pressed");
                    doneButton.setBackgroundResource(R.drawable.ic_done_grey_600_24dp);
                    isDone = false;
                    flag.setImageResource(R.drawable.ic_bookmark_red_400_24dp);
                    //lastNote.setDone(false);
                }
                else
                {
                    Log.d("ko", "button_done pressed");
                    doneButton.setBackgroundResource(R.drawable.ic_done_black_24dp);
                    isDone = true;
                    flag.setImageResource(R.drawable.ic_bookmark_green_400_24dp);
                    //lastNote.setDone(true);
                }
        }
    }
}
