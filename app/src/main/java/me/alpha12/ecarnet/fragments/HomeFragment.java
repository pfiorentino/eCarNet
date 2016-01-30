package me.alpha12.ecarnet.fragments;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.activities.AddFillUpActivity;
import me.alpha12.ecarnet.activities.CarProfileActivity;
import me.alpha12.ecarnet.charts.LineChartCustom;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.Intervention;
import me.alpha12.ecarnet.models.Reminder;

public class HomeFragment extends MasterFragment {
    private Reminder lastMemo;

    private ArrayList<Intervention> myInterventions = new ArrayList<>();
    private ArrayList<Intervention> allMyInterventions = new ArrayList<>();
    private ArrayList<Intervention> myFillsUp = new ArrayList<>();

    private TextView titleMemo;
    private TextView limitMemo;

    public static HomeFragment newInstance(int fragmentId) {
        HomeFragment fragment = new HomeFragment();
        fragment.setFragmentId(fragmentId);
        return fragment;
    }

    public HomeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerFloatingActionButton(R.id.addFillupFAB);

        setDefaultTitle(currentCar.getModelString());
        if (currentCar.isDefined())
            setDefaultSubTitle(currentCar.getStringPlateNum()+" - "+currentCar.getKilometers()+" km");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        titleMemo = (TextView) view.findViewById(R.id.reminderTitleTextView);
        limitMemo = (TextView) view.findViewById(R.id.reminderDateTextView);

        lastMemo = Reminder.getLastByCar(currentCar.getId());

        TextView consumption = (TextView) view.findViewById(R.id.consumptionValue);
        LineChart kilometersLine = (LineChart) view.findViewById(R.id.kilometersChart);
        TextView kilometersText = (TextView) view.findViewById(R.id.kilometersData);

        Calendar c = Calendar.getInstance();
        Date limit = c.getTime();
        limit.setDate(1);

        myFillsUp = Intervention.findInterventionByLimit(currentCar.getId(), limit);

        myInterventions = Intervention.find10ByCar(currentCar.getId());
        allMyInterventions = Intervention.findAllByCar(currentCar.getId());

        //prepare note if exist
        //lastMemo = new Memo(0,"Vidange + filtre à diesel", limit, 15000, false, true, false, currentCar.getId());
        useNotedCard(view);

        if(myInterventions.size() != 0) {
            ArrayList<Entry> kilometersChart = getKilometers(myInterventions, currentCar);
            ArrayList<Entry> fillUpChart = getAmoutsFillUp();

            LineChartCustom kilometersLineCustom = new LineChartCustom(kilometersLine, kilometersChart, "", getLineChartLabels(myInterventions, 1), null);


            int sum = 0;
            for (Entry value : kilometersChart)
            {
                sum += (int)value.getVal();
            }
            kilometersText.setText(Integer.toString(sum) + " km effectués");
            kilometersText.setTextSize(16);

            float floatSum = 0f;
            for(int i = 0; i<fillUpChart.size(); i++)
            {
                floatSum+= fillUpChart.get(i).getVal();
            }
            animateTextView(0.0f, getConsumption(), consumption);
        } else {
            //view.findViewById(R.id.topCharts).setVisibility(View.GONE);
        }

        return view;
    }

    public float getConsumption() {
        float totalConsumption = 0;
        float currentConsumption;
        int numberOfInters = 0;
        for(int i=1; i<myInterventions.size(); i++) {
            if(myInterventions.get(i).getQuantity()!=0 && myInterventions.get(i-1).getKilometers() < myInterventions.get(1).getKilometers()) {
                currentConsumption = (float)myInterventions.get(i).getQuantity() * 100 / (myInterventions.get(i).getKilometers() - myInterventions.get(i-1).getKilometers());
                totalConsumption +=currentConsumption;
            }
        }
        return totalConsumption/(myInterventions.size()-1);
    }

    public ArrayList<String> getLineChartLabels(ArrayList<Intervention> myInterventions, int start)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yy", Locale.FRENCH);
        ArrayList<String> labs = new ArrayList<>();
        for(int i=start; i<myInterventions.size(); i++)
        {
            String dateOutput = sdf.format(myInterventions.get(i).getDate());
            labs.add(dateOutput);
        }
        Log.d("nb int", labs.size() + "");
        return labs;
    }



    public void animateTextView(float initialValue, float finalValue, final TextView  textview) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(initialValue, finalValue);
            valueAnimator.setDuration(1800);

            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        float value = Float.parseFloat(valueAnimator.getAnimatedValue().toString());
                        textview.setText(String.format("%.1f", value));
                    }
                }
            });
            valueAnimator.start();
        }
    }

    public ArrayList<Entry> getAmoutsFillUp()
    {
        ArrayList<Entry> amountsOfFillUp = new ArrayList<>();
        for(Integer i=0; i<myFillsUp.size(); i++)
            amountsOfFillUp.add(new Entry((float)myFillsUp.get(i).getPrice(), i));
        return amountsOfFillUp;
    }


    public ArrayList<Entry> getKilometers(ArrayList<Intervention> inters, Car current) {
        ArrayList<Entry> sumOfkilmeters = new ArrayList<>();
        int newValue = 0;
        for(int i=1; i<inters.size(); i++) {
            newValue = inters.get(i).getKilometers() - inters.get(i-1).getKilometers();
            sumOfkilmeters.add(new Entry(newValue, i-1));
        }
        Log.d("nb kil", sumOfkilmeters.size()+"");
        return sumOfkilmeters;
    }


    private void useNotedCard(View view) {
        if (lastMemo == null) {
            CardView card = (CardView) view.findViewById(R.id.newNoteCard);
            card.setVisibility(View.GONE);
        } else {
            titleMemo.setText(lastMemo.getTitle());
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", Locale.FRENCH);
            limitMemo.setText(lastMemo.getKilometers() + " km ou " + sdf.format(lastMemo.getLimitDate().getTime()));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addFillupFAB:
                Intent intent = new Intent(v.getContext(), AddFillUpActivity.class);
                intent.putExtra("carId", currentCar.getId());
                startActivity(intent);
                break;
        }
    }
}
