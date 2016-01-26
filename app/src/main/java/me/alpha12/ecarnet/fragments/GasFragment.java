package me.alpha12.ecarnet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.activities.MainActivity;
import me.alpha12.ecarnet.classes.MasterFragment;
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

        ArrayList<Intervention> interventions = Intervention.findFillUpByCar(currentCar.getId());

        if (interventions != null && interventions.size() > 0) {
            TextView kmTotalText = (TextView) view.findViewById(R.id.kmTotalValue);
            kmTotalText.setText(String.valueOf(interventions.get(0).getKilometers()));
            TextView qteText = (TextView) view.findViewById(R.id.qteValue);
            qteText.setText(String.valueOf(interventions.get(0).getQuantity()));
            TextView priceText = (TextView) view.findViewById(R.id.priceValue);
            priceText.setText(String.valueOf(interventions.get(0).getPrice()));
        }
        return view;
    }
}
