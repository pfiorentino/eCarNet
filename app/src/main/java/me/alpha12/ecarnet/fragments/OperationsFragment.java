package me.alpha12.ecarnet.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import me.alpha12.ecarnet.GlobalContext;
import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.activities.AddInterventionActivity;
import me.alpha12.ecarnet.adapters.OperationAdapter;
import me.alpha12.ecarnet.charts.LineChartCustom;
import me.alpha12.ecarnet.classes.AdaptedListView;
import me.alpha12.ecarnet.classes.MasterFragment;
import me.alpha12.ecarnet.models.Intervention;

public class OperationsFragment extends MasterFragment {
    private FloatingActionButton fab;
    LinearLayout dateChartDetails;
    TextView chartDataDescription;
    TextView chartDataCost;
    TextView chartDataDate;
    Calendar mCurrentDate;

    public static OperationsFragment newInstance(int fragmentId) {
        OperationsFragment fragment = new OperationsFragment();
        fragment.setFragmentId(fragmentId);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerFloatingActionButton(R.id.addOperationFAB);

        setDefaultTitle(getString(R.string.title_fragment_operations));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mCurrentDate = Calendar.getInstance();
        Date limit = mCurrentDate.getTime();
        limit.setMonth(0);
        limit.setDate(0);
        final View view = inflater.inflate(R.layout.fragment_operations, container, false);
        dateChartDetails = (LinearLayout) view.findViewById(R.id.dateChartDetails);
        chartDataDescription = (TextView) view.findViewById(R.id.chartDataDescription);
        chartDataCost = (TextView) view.findViewById(R.id.chartDataCost);
        chartDataDate = (TextView) view.findViewById(R.id.chartDataDate);

        final ArrayList<Intervention> interventionList = Intervention.find10ByCar(currentCar.getId());
        interventionList.add(new Intervention(0, currentCar.getId(), 1, "vidange et filtre a gazoil", 15000, new Date(), 80.4, 45.3));
        interventionList.add(new Intervention(0, currentCar.getId(), 1, "vidange", 15000, new Date(), 135.4, 45.3));
        interventionList.add(new Intervention(0, currentCar.getId(), 1, "courroie", 15000, new Date(), 32.75, 45.3));
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        if (!interventionList.isEmpty()) {
            TextView operationChartTitle = (TextView) view.findViewById(R.id.operationChartTitle);
            operationChartTitle.setText("Mes dernières interventions");

            LineChart operationChart = (LineChart) view.findViewById(R.id.operationChart);
            chartDataDescription.setText(String.format(getResources().getString(R.string.chartDesciption), interventionList.get(0).getDescription()));
            chartDataCost.setText(String.format(getResources().getString(R.string.chartCost), interventionList.get(0).getPrice()));
            chartDataDate.setText(GlobalContext.getFormattedSmallDate(interventionList.get(0).getDate()));

            TextView totalPrice = (TextView) view.findViewById(R.id.totalPrice);
            totalPrice.setText(String.valueOf(Intervention.getTotalInterventionPrice(currentCar.getId(), limit)));

            TableLayout myIntervention;

            myIntervention = (TableLayout) view.findViewById(R.id.OperationList);
            View child = inflater.inflate(R.layout.custom_operation_data_table, null);
            myIntervention.addView(child);

            for (int i = 0; i < interventionList.size(); i++) {
                entries.add(new Entry((float)interventionList.get(i).getPrice(), i));
                labels.add(GlobalContext.getFormattedSmallDate(interventionList.get(i).getDate()));

                child = inflater.inflate(R.layout.custom_operation_data_table, null);
                TextView name = (TextView) child.findViewById(R.id.operationTitle);
                TextView cost = (TextView) child.findViewById(R.id.costOperation);
                TextView date = (TextView) child.findViewById(R.id.dateOperation);
                name.setText(interventionList.get(i).getDescription());
                cost.setText(String.valueOf(interventionList.get(i).getPrice()));
                date.setText(GlobalContext.getFormattedSmallDate(interventionList.get(i).getDate()));
                if (i == interventionList.size() - 1) {
                    View divider = (View) child.findViewById(R.id.divider);
                    divider.setVisibility(View.GONE);
                }
                LineChartCustom lcc = new LineChartCustom(operationChart, entries, "Mes interventions", labels, null);
                lcc.getChart().setTouchEnabled(true);
                lcc.getChart().setOnChartValueSelectedListener(new OnChartValueSelectedListener() {


                    @Override

                    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                        chartDataDescription.setText(String.format(getResources().getString(R.string.chartDesciption), interventionList.get(e.getXIndex()).getDescription()));
                        chartDataCost.setText(String.format(getResources().getString(R.string.chartCost), e.getVal()));
                        chartDataDate.setText(GlobalContext.getFormattedSmallDate(interventionList.get(e.getXIndex()).getDate()));
                    }

                    @Override
                    public void onNothingSelected() {
                    }
                });
                    myIntervention.addView(child);
                }

            }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addOperationFAB:
                Intent intent = new Intent(v.getContext(), AddInterventionActivity.class);
                intent.putExtra("carId", currentCar.getId());
                startActivity(intent);
                break;
        }
    }
}
