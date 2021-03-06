package me.alpha12.ecarnet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import me.alpha12.ecarnet.activities.AddFillUpActivity;
import me.alpha12.ecarnet.activities.AddReminderActivity;
import me.alpha12.ecarnet.adapters.FillupAdapter;
import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.models.Intervention;

public class GasFragment extends MasterFragment {

    public static GasFragment newInstance(int fragmentId) {
        GasFragment fragment = new GasFragment();
        fragment.setFragmentId(fragmentId);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultTitle(getString(R.string.title_fragment_gas));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gas, container, false);
        registerFloatingActionButton(R.id.addFillupFromFillupFAB);


        ArrayList<Intervention> interventions = Intervention.findFillUpByCar(currentCar.getId());

        if (interventions != null && interventions.size() > 0) {

            ListView listView = (ListView) view.findViewById(R.id.fillup_list_view);

            FillupAdapter adapter = new FillupAdapter(getContext(), interventions);

            listView.setAdapter(adapter);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addFillupFromFillupFAB:
                Intent intent = new Intent(v.getContext(), AddFillUpActivity.class);
                intent.putExtra("carId", currentCar.getId());
                startActivity(intent);
                break;
        }
    }
}
