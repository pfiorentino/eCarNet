package me.alpha12.ecarnet.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.activities.MainActivity;
import me.alpha12.ecarnet.charts.BarChartCustom;
import me.alpha12.ecarnet.charts.LineChartCustom;
import me.alpha12.ecarnet.charts.PieChartCustom;
import me.alpha12.ecarnet.charts.RadarChartCustom;
import me.alpha12.ecarnet.interfaces.OnFragmentInteractionListener;
import me.alpha12.ecarnet.models.Car;

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

        //fake data ----------------------------------------
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(37.5f, 0));
        entries.add(new BarEntry(18.0f, 1));
        entries.add(new BarEntry(26.3f, 2));
        entries.add(new BarEntry(10f, 3));
        entries.add(new BarEntry(15.0f, 4));
        entries.add(new BarEntry(19.3f, 5));

        BarDataSet dataset = new BarDataSet(entries, "");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Janvier");
        labels.add("Fevrier");
        labels.add("Mars");
        labels.add("Avril");
        labels.add("Mai");
        labels.add("Juin");

        BarChart chart = (BarChart) view.findViewById(R.id.mainChart);
        BarChartCustom barCustom = new BarChartCustom(chart, entries, "", labels, null);



        ArrayList<Entry> entriesPie = new ArrayList<>();
        entriesPie.add(new Entry(37.5f, 0));
        entriesPie.add(new Entry(18.0f, 1));
        entriesPie.add(new Entry(26.3f, 2));
        entriesPie.add(new Entry(10f, 3));
        entriesPie.add(new Entry(15.0f, 4));
        entriesPie.add(new Entry(19.3f, 5));
        PieChart pie = (PieChart) view.findViewById(R.id.mainChartPie);
        PieChartCustom pieCustom = new PieChartCustom(pie, entriesPie, "", labels, null);


        ArrayList<Entry> oldEntries = new ArrayList<>();
        oldEntries.add(new Entry(17.5f, 0));
        oldEntries.add(new Entry(38.0f, 1));
        oldEntries.add(new Entry(26.3f, 2));
        oldEntries.add(new Entry(30f, 3));
        oldEntries.add(new Entry(19.0f, 4));
        oldEntries.add(new Entry(39.3f, 5));

        RadarChart radar =(RadarChart) view.findViewById(R.id.radar);
        RadarChartCustom radarCustom = new RadarChartCustom(radar, entriesPie, "", labels, null);
        radarCustom.addEntries(oldEntries);
        TextView tv = (TextView) view.findViewById(R.id.main_text);


        LineChart line = (LineChart) view.findViewById(R.id.line);
        LineChartCustom lineCustom = new LineChartCustom(line, entriesPie, "", labels, null);
        //lineCustom.addEntries(oldEntries);

        tv.setText("Home car: "+currentCar.getPlateNum());

        return view;
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

}
