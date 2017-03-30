package me.alpha12.ecarnet.fragments;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import me.alpha12.ecarnet.GlobalContext;
import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.activities.AddCarActivity;
import me.alpha12.ecarnet.activities.AddFillUpActivity;
import me.alpha12.ecarnet.activities.AddInterventionActivity;
import me.alpha12.ecarnet.activities.CarProfileActivity;
import me.alpha12.ecarnet.charts.LineChartCustom;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.Intervention;
import me.alpha12.ecarnet.models.Reminder;

public class HomeFragment extends MasterFragment {
    private CardView unfinishedConfigurationCard;
    private Button completeConfigButton;

    private CardView nextReminderCard;
    private TextView reminderTitleTextView;
    private TextView reminderLimitTextView;

    private CardView consumptionCard;
    private TextView consumptionTextView;

    private CardView kilometersCard;
    private LineChart kilometersChart;
    private TextView kilometersTextView;

    private CardView fillUpCard;
    private TextView lastFillUpQtyTextView;
    private TextView lastFillUpAmountTextView;
    private LineChart fillUpChart;
    private TextView fillUpChartDataKilometers;
    private TextView fillUpChartDataCost;
    private TextView fillUpChartDataDate;

    private CardView noFillUpCard;
    private Button addFillUpButton;
    private CardView noInterventionCard;
    private Button addInterventionButton;

    public static HomeFragment newInstance(int fragmentId) {
        HomeFragment fragment = new HomeFragment();
        fragment.setFragmentId(fragmentId);
        return fragment;
    }

    public HomeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        registerFloatingActionButton(R.id.addFillupFAB);

        setDefaultTitle(currentCar.getModelString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        this.unfinishedConfigurationCard = (CardView) view.findViewById(R.id.unfinished_configuration_card);
        this.completeConfigButton = (Button) view.findViewById(R.id.complete_config_button);

        this.nextReminderCard = (CardView) view.findViewById(R.id.next_reminder_card);
        this.reminderTitleTextView = (TextView) view.findViewById(R.id.reminder_title_text_view);
        this.reminderLimitTextView = (TextView) view.findViewById(R.id.reminder_limit_text_view);

        this.consumptionCard = (CardView) view.findViewById(R.id.consumption_card);
        this.consumptionTextView = (TextView) view.findViewById(R.id.consumption_text_view);

        this.kilometersCard = (CardView) view.findViewById(R.id.kilometers_card);
        this.kilometersChart = (LineChart) view.findViewById(R.id.kilometers_chart);
        this.kilometersTextView = (TextView) view.findViewById(R.id.kilometers_text_view);

        this.fillUpCard = (CardView) view.findViewById(R.id.fill_up_card);
        this.lastFillUpQtyTextView = (TextView) view.findViewById(R.id.last_fill_up_qty_text_view);
        this.lastFillUpAmountTextView = (TextView) view.findViewById(R.id.last_fill_up_amount_text_view);
        this.fillUpChart = (LineChart) view.findViewById(R.id.fill_up_chart);
        this.fillUpChartDataKilometers = (TextView) view.findViewById(R.id.chartDataDescription);
        this.fillUpChartDataCost = (TextView) view.findViewById(R.id.chartDataCost);
        this.fillUpChartDataDate = (TextView) view.findViewById(R.id.chartDataDate);

        this.noFillUpCard = (CardView) view.findViewById(R.id.no_fill_up_card);
        this.addFillUpButton = (Button) view.findViewById(R.id.add_fill_up_button);
        this.addFillUpButton.setOnClickListener(this);
        this.noInterventionCard = (CardView) view.findViewById(R.id.no_intervention_card);
        this.addInterventionButton = (Button) view.findViewById(R.id.add_intervention_button);
        this.addInterventionButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        currentCar = Car.get(currentCar.getId());
        if (currentCar.isDefined())
            setDefaultSubTitle(currentCar.getDetails());

        refreshUnfinishedConfigurationCard();
        refreshConsumptionCard();
        refreshReminderCard();
        refreshKilometersCard();
        refreshFillUpCard();
        refreshInterventionCard();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.home_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.car_info_menu_item:
                Intent intent = new Intent(this.getContext(), CarProfileActivity.class);
                intent.putExtra("carId", currentCar.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshUnfinishedConfigurationCard() {
        if (currentCar.isDefined()){
            this.unfinishedConfigurationCard.setVisibility(View.GONE);
        } else {
            this.unfinishedConfigurationCard.setVisibility(View.VISIBLE);
            this.completeConfigButton.setOnClickListener(this);
        }
    }

    private void refreshReminderCard() {
        Reminder nextIntervention = Reminder.findNextIntervention(currentCar.getId());

        if (nextIntervention != null) {
            this.reminderTitleTextView.setText(nextIntervention.getTitle());
            this.reminderLimitTextView.setText(nextIntervention.getLimitText());
            this.nextReminderCard.setVisibility(View.VISIBLE);
        } else {
            this.nextReminderCard.setVisibility(View.GONE);
        }
    }

    private void refreshConsumptionCard() {
        float avgConsumption = Intervention.getAverageConsumption(currentCar.getId());

        if (avgConsumption >= 0.0f) {
            animateTextView(0.0f, avgConsumption, consumptionTextView);
            this.consumptionCard.setVisibility(View.VISIBLE);
        } else {
            this.consumptionCard.setVisibility(View.GONE);
        }
    }

    private void refreshKilometersCard() {
        ArrayList<Intervention> lastInterventions = Intervention.findAllByCar(currentCar.getId(), 10);

        if (lastInterventions.size() > 1) {
            ArrayList<Entry> chartData = getInterventionsDistances(lastInterventions);
            ArrayList<String> chartLabels = getInterventionsChartLabels(lastInterventions, 1);

            new LineChartCustom(this.kilometersChart, chartData, "", chartLabels, null);


            int totalDistance = getTotalDistance(lastInterventions);
            this.kilometersTextView.setText(totalDistance + " km effectués");
            this.kilometersCard.setVisibility(View.VISIBLE);
        } else {
            this.kilometersCard.setVisibility(View.GONE);
        }
    }

    private void refreshFillUpCard() {
        final ArrayList<Intervention> lastFillUps = Intervention.findFillUpsByCar(currentCar.getId(), 10);

        if (lastFillUps.size() > 0) {
            ArrayList<Entry> chartData = getInterventionsAmounts(lastFillUps);
            ArrayList<String> chartLabels = getInterventionsChartLabels(lastFillUps, 0);

            fillUpChartDataKilometers.setText(String.format(getResources().getString(R.string.chartKilomters), lastFillUps.get(0).getKilometers()));
            fillUpChartDataCost.setText(String.format(getResources().getString(R.string.chartCost), (float) lastFillUps.get(0).getPrice()));
            fillUpChartDataDate.setText(GlobalContext.getFormattedSmallDate(lastFillUps.get(0).getDate()));

            LineChartCustom lcc = new LineChartCustom(this.fillUpChart, chartData, "", chartLabels, null);
            lcc.getChart().setTouchEnabled(true);
            lcc.getChart().setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                    fillUpChartDataKilometers.setText(String.format(getResources().getString(R.string.chartKilomters), lastFillUps.get(e.getXIndex()).getKilometers()));
                    fillUpChartDataCost.setText(String.format(getResources().getString(R.string.chartCost), (float) e.getVal()));
                    fillUpChartDataDate.setText(GlobalContext.getFormattedSmallDate(lastFillUps.get(e.getXIndex()).getDate()));
                }

                @Override
                public void onNothingSelected() {
                }
            });

            Intervention lastFillUp = lastFillUps.get(lastFillUps.size() - 1);
            animateTextView(0, lastFillUp.getQuantity(), this.lastFillUpQtyTextView);
            animateTextView(0, lastFillUp.getPrice(), this.lastFillUpAmountTextView);

            this.fillUpCard.setVisibility(View.VISIBLE);
            this.noFillUpCard.setVisibility(View.GONE);
        } else {
            this.fillUpCard.setVisibility(View.GONE);
            this.noFillUpCard.setVisibility(View.VISIBLE);
        }

    }

    private void refreshInterventionCard() {
        ArrayList<Intervention> lastInterventions = Intervention.findOtherByCar(currentCar.getId(), 10);

        if (lastInterventions.size() > 0) {
            this.noInterventionCard.setVisibility(View.GONE);
        } else {
            this.noInterventionCard.setVisibility(View.VISIBLE);
        }

    }

    public void animateTextView(float initialValue, float finalValue, final TextView textview) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (finalValue > 10)
                initialValue = finalValue-10;

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(initialValue, finalValue);
            valueAnimator.setDuration(1800);

            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        float value = Float.parseFloat(valueAnimator.getAnimatedValue().toString());
                        textview.setText(String.format("%.2f", value));
                    }
                }
            });
            valueAnimator.start();
        } else {
            textview.setText(String.format("%.2f", finalValue));
        }
    }

    public ArrayList<Entry> getInterventionsDistances(ArrayList<Intervention> interventionsList) {
        ArrayList<Entry> byInterventionDistances = new ArrayList<>();
        int interventionDistance = 0;

        for(int i = 1; i < interventionsList.size(); i++) {
            interventionDistance = interventionsList.get(i).getKilometers() - interventionsList.get(i-1).getKilometers();
            byInterventionDistances.add(new Entry(interventionDistance, i-1));
        }

        return byInterventionDistances;
    }

    public ArrayList<Entry> getInterventionsAmounts(ArrayList<Intervention> interventionsList) {
        ArrayList<Entry> amounts = new ArrayList<>();

        for(int i = 0; i < interventionsList.size(); i++) {
            amounts.add(new Entry(interventionsList.get(i).getPrice(), i));
        }

        return amounts;
    }

    public ArrayList<String> getInterventionsChartLabels(ArrayList<Intervention> interventionsList, int firstIndex) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yy", Locale.FRENCH);

        ArrayList<String> labels = new ArrayList<>();

        for(int i = firstIndex; i < interventionsList.size(); i++) {
            String dateOutput = sdf.format(interventionsList.get(i).getDate());
            labels.add(dateOutput);
        }

        return labels;
    }

    public int getTotalDistance(ArrayList<Intervention> interventionsList) {
        int totalDistance = -1;

        if (interventionsList.size() > 1){
            totalDistance = interventionsList.get(interventionsList.size()-1).getKilometers() - interventionsList.get(0).getKilometers();
        }

        return totalDistance;
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.addFillupFAB:
            case R.id.add_fill_up_button:
                intent = new Intent(v.getContext(), AddFillUpActivity.class);
                intent.putExtra("carId", currentCar.getId());
                startActivity(intent);
                break;
            case R.id.complete_config_button:
                intent = new Intent(v.getContext(), AddCarActivity.class);
                intent.putExtra("carId", currentCar.getId());
                startActivityForResult(intent, 0);
                break;
            case R.id.add_intervention_button:
                intent = new Intent(v.getContext(), AddInterventionActivity.class);
                intent.putExtra("carId", currentCar.getId());
                startActivity(intent);
                break;
        }
    }
}
