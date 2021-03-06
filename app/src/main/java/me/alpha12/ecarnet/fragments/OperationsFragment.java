package me.alpha12.ecarnet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.alpha12.ecarnet.GlobalContext;
import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.activities.AddInterventionActivity;
import me.alpha12.ecarnet.activities.HistoryActivity;
import me.alpha12.ecarnet.charts.LineChartCustom;
import me.alpha12.ecarnet.models.Intervention;

public class OperationsFragment extends MasterFragment {
    private ArrayList<Intervention> interventionListForGrid;
    private ArrayList<Intervention> interventionListForChart;

    LinearLayout dateChartDetails;
    TextView chartDataDescription;
    TextView chartDataCost;
    TextView chartDataDate;
    Calendar mCurrentDate;
    TextView operationChartTitle;
    LineChart operationChart;
    TableLayout myIntervention;
    View view;
    LayoutInflater inflater;


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
    public void onResume(){
        super.onResume();
        updateLists();
        if(!interventionListForGrid.isEmpty()) {
            CardView chartCard = (CardView) view.findViewById(R.id.chartCard);
            chartCard.setVisibility(View.VISIBLE);
            CardView gridCard = (CardView) view.findViewById(R.id.gridCard);
            gridCard.setVisibility(View.VISIBLE);
            LinearLayout summaryPrices = (LinearLayout) view.findViewById(R.id.summaryPrices);
            summaryPrices.setVisibility(View.VISIBLE);
            TextView noItemTextTitle = (TextView) view.findViewById(R.id.noItemTextTitle);
            noItemTextTitle.setVisibility(View.GONE);
            TextView noItemTextDesc = (TextView) view.findViewById(R.id.noItemTextDesc);
            noItemTextDesc.setVisibility(View.GONE);
            ImageView noItemImageView = (ImageView) view.findViewById(R.id.noItemImageView);
            noItemImageView.setVisibility(View.GONE);
            updateGrid();
            updateChart();
            Date limit = mCurrentDate.getTime();
            limit.setMonth(0);
            limit.setDate(0);
            updatePrices(limit);
            Button allInterventionButton = (Button) view.findViewById(R.id.allInterventionButton);
            allInterventionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), HistoryActivity.class);
                    intent.putExtra("carid", currentCar.getId());
                    startActivity(intent);
                }
            });
        }
        else {
            CardView chartCard = (CardView) view.findViewById(R.id.chartCard);
            chartCard.setVisibility(View.GONE);
            CardView gridCard = (CardView) view.findViewById(R.id.gridCard);
            gridCard.setVisibility(View.GONE);
            LinearLayout summaryPrices = (LinearLayout) view.findViewById(R.id.summaryPrices);
            summaryPrices.setVisibility(View.GONE);
            TextView noItemTextTitle = (TextView) view.findViewById(R.id.noItemTextTitle);
            noItemTextTitle.setVisibility(View.VISIBLE);
            TextView noItemTextDesc = (TextView) view.findViewById(R.id.noItemTextDesc);
            noItemTextDesc.setVisibility(View.VISIBLE);
            ImageView noItemImageView = (ImageView) view.findViewById(R.id.noItemImageView);
            noItemImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        mCurrentDate = Calendar.getInstance();
        Date limit = mCurrentDate.getTime();
        limit.setMonth(0);
        limit.setDate(0);
        view = inflater.inflate(R.layout.fragment_operations, container, false);
        dateChartDetails = (LinearLayout) view.findViewById(R.id.dateChartDetails);
        chartDataDescription = (TextView) view.findViewById(R.id.chartDataDescription);
        chartDataCost = (TextView) view.findViewById(R.id.chartDataCost);
        chartDataDate = (TextView) view.findViewById(R.id.chartDataDate);
        operationChartTitle = (TextView) view.findViewById(R.id.operationChartTitle);
        operationChart = (LineChart) view.findViewById(R.id.operationChart);
        myIntervention = (TableLayout) view.findViewById(R.id.OperationList);


        interventionListForGrid = Intervention.findInterventionByNumericLimit(currentCar.getId(), " ORDER BY " + Intervention.DBModel.C_DATE + " DESC, "+ Intervention.DBModel.C_ID + " DESC ", 3);
        interventionListForChart = Intervention.findInterventionByNumericLimit(currentCar.getId(), " ORDER BY " + Intervention.DBModel.C_DATE +  " ASC, "+ Intervention.DBModel.C_ID + " ASC ", 0);

        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
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


    public void updateGrid(){
        myIntervention.removeAllViews();
        View child = inflater.inflate(R.layout.custom_operation_data_table, null);
        myIntervention.addView(child);

        for (int i = 0; i < interventionListForGrid.size(); i++) {
            child = inflater.inflate(R.layout.custom_operation_data_table, null);
            TextView name = (TextView) child.findViewById(R.id.operationTitle);
            TextView cost = (TextView) child.findViewById(R.id.costOperation);
            TextView date = (TextView) child.findViewById(R.id.dateOperation);
            name.setText(interventionListForGrid.get(i).getDescription());
            cost.setText(String.format("%1$,.2f €", interventionListForGrid.get(i).getPrice()));
            date.setText(GlobalContext.getFormattedSmallDate(interventionListForGrid.get(i).getDate()));
            if (i == interventionListForGrid.size() - 1) {
                View divider = (View) child.findViewById(R.id.divider);
                divider.setVisibility(View.GONE);
            }

            myIntervention.addView(child);
        }
    }

    public void updateChart(){
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        operationChartTitle.setText("Mes dernières interventions");
        chartDataDescription.setText(String.format(getResources().getString(R.string.chartDesciption), interventionListForGrid.get(0).getDescription()));
        chartDataCost.setText(String.format(getResources().getString(R.string.chartCost), (float) interventionListForGrid.get(0).getPrice()));
        chartDataDate.setText(GlobalContext.getFormattedSmallDate(interventionListForGrid.get(0).getDate()));
        for(int i=0; i< interventionListForChart.size(); i++) {
            entries.add(new Entry((float)interventionListForChart.get(i).getPrice(), i));
            labels.add(GlobalContext.getFormattedSmallDate(interventionListForChart.get(i).getDate()));
        }
        LineChartCustom lcc = new LineChartCustom(operationChart, entries, "Mes interventions", labels, null);
        lcc.getChart().setTouchEnabled(true);
        lcc.getChart().setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                chartDataDescription.setText(String.format(getResources().getString(R.string.chartDesciption), interventionListForChart.get(e.getXIndex()).getDescription()));
                chartDataCost.setText(String.format(getResources().getString(R.string.chartCost), (float) e.getVal()));
                chartDataDate.setText(GlobalContext.getFormattedSmallDate(interventionListForChart.get(e.getXIndex()).getDate()));
            }

            @Override
            public void onNothingSelected() {
            }
        });
    }

    public void updatePrices(Date limit){
        TextView totalPrice = (TextView) view.findViewById(R.id.totalPrice);
        totalPrice.setText(String.format(getResources().getString(R.string.averageOperationPrice), Intervention.getTotalInterventionPrice(currentCar.getId(), limit)));
        TextView averagePrice = (TextView) view.findViewById(R.id.averagePrice);
        averagePrice.setText(String.format(getResources().getString(R.string.averageOperationPrice), Intervention.getAverageInterventionPrice(currentCar.getId())));
    }

    public void updateLists()
    {
        interventionListForGrid = Intervention.findInterventionByNumericLimit(currentCar.getId(), " ORDER BY " + Intervention.DBModel.C_DATE + " DESC, "+ Intervention.DBModel.C_ID + " DESC ", 3);
        interventionListForChart = Intervention.findInterventionByNumericLimit(currentCar.getId(), " ORDER BY " + Intervention.DBModel.C_DATE + " ASC, "+ Intervention.DBModel.C_ID + " ASC ", 0);
    }
}
