package me.alpha12.ecarnet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.activities.AddInterventionActivity;
import me.alpha12.ecarnet.adapters.OperationAdapter;
import me.alpha12.ecarnet.classes.AdaptedListView;
import me.alpha12.ecarnet.classes.MasterFragment;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_operations, container, false);
        final ArrayList<Intervention> interventionList = Intervention.find10ByCar(currentCar.getId());
        interventionList.add(0, null);
        interventionList.add(1, new Intervention(0, currentCar.getId(), 1, "vidange et filtre a gazoil", 15000, new Date(), 80.4, 45.3));

        if (!interventionList.isEmpty()) {
            AdaptedListView myIntervention;
            myIntervention = (AdaptedListView) view.findViewById(R.id.OperationList);
            myIntervention.setAdapter(new OperationAdapter(getContext(), interventionList, getActivity()));
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

    @Override
    public void setTitle() {
        parentActivity.setTitle(R.string.title_fragment_operations);
    }
}
