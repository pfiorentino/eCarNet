package me.alpha12.ecarnet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import me.alpha12.ecarnet.GlobalContext;
import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.activities.AddInterventionActivity;
import me.alpha12.ecarnet.models.Intervention;

public class OperationsFragment extends MasterFragment {
    private FloatingActionButton fab;

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

        View view = inflater.inflate(R.layout.fragment_operations, container, false);
        final ArrayList<Intervention> interventionList = Intervention.find10ByCar(currentCar.getId());
        //interventionList.add(0, null);
        interventionList.add(new Intervention(0, currentCar.getId(), 1, "vidange et filtre a gazoil", 15000, new Date(), 80.4, 45.3));
        interventionList.add(new Intervention(0, currentCar.getId(), 1, "vidange", 15000, new Date(), 80.4, 45.3));
        interventionList.add(new Intervention(0, currentCar.getId(), 1, "courroie", 15000, new Date(), 80.4, 45.3));

        if (!interventionList.isEmpty()) {
            TableLayout myIntervention;
            myIntervention = (TableLayout) view.findViewById(R.id.OperationList);
            for(Intervention value : interventionList){
                TableRow tr = constructRow(value, view);
                tr.setLayoutParams(getParams());
                tr.removeView(view);
                myIntervention.addView(tr, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            }


            //myIntervention.setAdapter(new OperationAdapter(getContext(), interventionList, getActivity()));
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

    public TableRow constructRow(Intervention inter, View v){
        TableRow tr = (TableRow)v.findViewById(R.id.tableRow);
        tr.setId(inter.getId());
        TextView tvTitle = new TextView(getContext()); //(TextView) myIntervention.findViewById(R.id.operationTitle);
        tvTitle.setId(100 + inter.getId());
        tvTitle.setText(inter.getDescription());
        TextView tvCost = new TextView(getContext());//(TextView) myIntervention.findViewById(R.id.costOperation);
        tvCost.setId(200 + inter.getId());
        tvCost.setPadding(8, 16, 16, 8);
        tvCost.setGravity(Gravity.RIGHT);
        tvCost.setText(String.valueOf(inter.getPrice()));
        TextView tvDate = new TextView(getContext()); //(TextView) myIntervention.findViewById(R.id.dateOperation);
        tvDate.setId(300 + inter.getId());
        tvDate.setPadding(8, 16, 16, 0);
        tvDate.setGravity(Gravity.RIGHT);
        tvDate.setText(GlobalContext.getFormattedMediumDate(inter.getDate()));
        tr.addView(tvTitle);
        tr.addView(tvCost);
        tr.addView(tvDate);
        return tr;
    }

    public TableLayout.LayoutParams getParams(){
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int trHeight = (int) (64 * scale + 0.5f);
        return new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, trHeight);
    }
/*
    @Override
    public void setTitle() {
        parentActivity.setTitle(R.string.title_fragment_operations);
    }
    */
}
