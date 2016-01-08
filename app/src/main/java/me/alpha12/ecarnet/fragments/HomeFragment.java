package me.alpha12.ecarnet.fragments;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.activities.MainActivity;
import me.alpha12.ecarnet.charts.BarChartCustom;
import me.alpha12.ecarnet.charts.LineChartCustom;
import me.alpha12.ecarnet.database.EcarnetHelper;
import me.alpha12.ecarnet.interfaces.OnFragmentInteractionListener;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.Intervention;
import me.alpha12.ecarnet.models.Operation;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private int mMenuEntryId;
    private LineChart mainChart;

    private OnFragmentInteractionListener mListener;

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


        title.setText(currentCar.getModel().getBrand() + " " + currentCar.getModel().getModel());
        TextView consumption = (TextView) view.findViewById(R.id.consumptionValue);

        HashMap<Integer, String> months = new HashMap<>();
        months.put(0, "Jan");
        months.put(1, "Fev");
        months.put(2, "Mar");
        months.put(3, "Avr");
        months.put(4, "Mai");
        months.put(5, "Jui");
        months.put(6, "Juil");
        months.put(7, "Aou");
        months.put(8, "Sep");
        months.put(9, "Oct");
        months.put(10, "Nov");
        months.put(11, "Dec");


        ArrayList<Intervention> interventions = Intervention.getAllIntervention(EcarnetHelper.bdd, currentCar.getUuid());

        //fake data
        interventions.add(new Intervention(currentCar.getUuid(), 10123, 5, new java.util.Date(2014, 11, 12), new ArrayList<Operation>(), 12.5));
        interventions.add(new Intervention(currentCar.getUuid(), 10223, 50.25, new java.util.Date(2015, 0, 12), new ArrayList<Operation>(), 15.5));
        interventions.add(new Intervention(currentCar.getUuid(), 10456, 50.25, new java.util.Date(2015, 2, 12), new ArrayList<Operation>(), 18.5));
        interventions.add(new Intervention(currentCar.getUuid(), 10710, 200.43, new java.util.Date(2015, 3, 12), new ArrayList<Operation>(), 0));
        interventions.add(new Intervention(currentCar.getUuid(), 11070, 50.90, new java.util.Date(2015, 3, 12), new ArrayList<Operation>(), 15));
        interventions.add(new Intervention(currentCar.getUuid(), 11340, 108.23, new java.util.Date(2015, 4, 12), new ArrayList<Operation>(), 0));
        interventions.add(new Intervention(currentCar.getUuid(), 11390, 200, new java.util.Date(2015, 5, 12), new ArrayList<Operation>(), 0));
        interventions.add(new Intervention(currentCar.getUuid(), 11701, 60.83, new java.util.Date(2015, 6, 12), new ArrayList<Operation>(), 25.3));
        interventions.add(new Intervention(currentCar.getUuid(), 11925, 120, new java.util.Date(2015, 6, 12), new ArrayList<Operation>(), 0));
        interventions.add(new Intervention(currentCar.getUuid(), 12780, 100.33, new java.util.Date(2015, 8, 12), new ArrayList<Operation>(), 0));
        interventions.add(new Intervention(currentCar.getUuid(), 13332, 130, new java.util.Date(2015, 10, 12), new ArrayList<Operation>(), 45.3));
        interventions.add(new Intervention(currentCar.getUuid(), 13782, 130, new java.util.Date(2015, 11, 12), new ArrayList<Operation>(), 45.3));
        interventions.add(new Intervention(currentCar.getUuid(), 14600, 130, new java.util.Date(2016, 0, 12), new ArrayList<Operation>(), 45.3));
        interventions.add(new Intervention(currentCar.getUuid(), 15770, 130, new java.util.Date(2016, 0, 12), new ArrayList<Operation>(), 45.3));

        if(interventions.size() != 0) {
            ArrayList<Intervention>interventionsUpToDate = getCars(interventions);
            ArrayList<String> labels = getLabels(months, interventionsUpToDate);
            ArrayList<Entry> kilometersChart = getKilometers(interventionsUpToDate, currentCar);
            ArrayList<BarEntry> fillUpChart = getAmoutsFillUp(interventionsUpToDate);
            ArrayList<BarEntry> interventionChart = getAmoutsIntervention(interventionsUpToDate);

            LineChart kilometersLine = (LineChart) view.findViewById(R.id.kilometersChart);
            LineChartCustom kilometersLineCustom = new LineChartCustom(kilometersLine, kilometersChart, "", labels, null);

            TextView kilometersText = (TextView) view.findViewById(R.id.kilometersData);
            int sum = 0;
            for (Entry value : kilometersChart)
            {
                sum += (int)value.getVal();
            }
            kilometersText.setText(Integer.toString(sum) + " km");
            kilometersText.setTextSize(16);

            BarChart costLine = (BarChart) view.findViewById(R.id.costChart);
            BarChartCustom costLineCustom = new BarChartCustom(costLine, interventionChart, "", labels, null);
            costLineCustom.addEntries(fillUpChart);

            TextView costText = (TextView) view.findViewById(R.id.costData);
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
            animateTextView(0.0f, getConsumption(interventions, currentCar), consumption);
        }
        return view;
    }


    public float getConsumption(ArrayList<Intervention> inters, Car current)
    {
        float totalConsumption = 0;
        float currentConsumption;
        int numberOfInters = 0;
        int oldKilometers = current.getKilometers();
        for(Intervention value : inters) {
            if(value.getAmount()!=0) {
                currentConsumption = (float)value.getAmount() * 100 / (value.getKilometers() - oldKilometers);
                totalConsumption +=currentConsumption;
                numberOfInters++;
                oldKilometers = value.getKilometers();
                Log.d("consommation mens", ""+currentConsumption);
            }
        }
        return totalConsumption/numberOfInters;
    }

    public ArrayList<Intervention> getCars(ArrayList<Intervention> interventions)
    {
        ArrayList<Intervention> inters = new ArrayList<>();
        Date indexDate = interventions.get(interventions.size() - 1).getDateIntervention();
        indexDate.setYear(indexDate.getYear() - 1);
        indexDate.setMonth(indexDate.getMonth() + 1);
        indexDate.setDate(1);
        for (Intervention value : interventions)
        {
            if(value.getDateIntervention().after(indexDate))
            {
                inters.add(value);
            }
        }
        return inters;
    }


    public ArrayList<String> getLabels(HashMap<Integer, String> labels, ArrayList<Intervention> inters)
    {
        ArrayList<String> labs = new ArrayList<>();
        Date indexDate = inters.get(inters.size()-1).getDateIntervention();
        indexDate.setYear(indexDate.getYear() - 1);
        indexDate.setMonth(indexDate.getMonth() + 1);
        indexDate.setDate(1);
        int month = indexDate.getMonth();
        int year = indexDate.getYear();
        String lab = labels.get(indexDate.getMonth()) + indexDate.getYear();
        labs.add(lab);
        for(int i=1; i<12; i++)
        {
            month++;
            if(month > 11) {
                month = 0;
                year++;
            }
            lab = labels.get(month)+year;
            labs.add(lab);
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


    public float[] getSumIntervention(ArrayList<Intervention> inters)
    {
        //-----------this function will regroup prices of interventions monthly and return into a Hashmap
        float[] values = new float[12];
        for(int i =0; i<inters.size(); i++)
            if(inters.get(i).getAmount()==0) {
                int indice = inters.get(i).getDateIntervention().getMonth();
                values[indice] += (float) inters.get(i).getPrice();
            }
        return values;
    }


    public float[] getSumFillUp(ArrayList<Intervention> inters)
    {
        //-----------this function will regroup prices of fills up monthly and return into a Hashmap
        float[] values = new float[12];
        for(int i =0; i<inters.size(); i++)
            if(inters.get(i).getAmount()!=0) {
                int indice = inters.get(i).getDateIntervention().getMonth();
                values[indice] += (float) inters.get(i).getPrice();
            }
        return values;
    }


    public ArrayList<BarEntry> getAmoutsFillUp(ArrayList<Intervention> inters)
    {
        ArrayList<BarEntry> amountsOfFillUp = new ArrayList<>();
        float[] fillUpTab = getSumFillUp(inters);
        for(Integer i=0; i<fillUpTab.length; i++)
            amountsOfFillUp.add(new BarEntry(fillUpTab[i], i));
        return amountsOfFillUp;
    }

    public ArrayList<BarEntry> getAmoutsIntervention(ArrayList<Intervention> inters)
    {
        ArrayList<BarEntry> amountsOfIntervention = new ArrayList<>();
        float[] interventionTab = getSumIntervention(inters);
        for(Integer i=0; i<interventionTab.length; i++)
            amountsOfIntervention.add(new BarEntry(interventionTab[i], i));
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
}
