package me.alpha12.ecarnet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
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
        interventionList.add(new Intervention(0, currentCar.getId(), 1, "vidange et filtre a gazoil", 15000, new Date(), 80.4, 45.3));
        interventionList.add(new Intervention(0, currentCar.getId(), 1, "vidange", 15000, new Date(), 80.4, 45.3));
        interventionList.add(new Intervention(0, currentCar.getId(), 1, "courroie", 15000, new Date(), 80.4, 45.3));

        if (!interventionList.isEmpty()) {
            TableLayout myIntervention;

            myIntervention = (TableLayout) view.findViewById(R.id.OperationList);
            View child = inflater.inflate(R.layout.custom_operation_data_table, null);
            myIntervention.addView(child);

            for (int i = 0; i < interventionList.size(); i++) {
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
