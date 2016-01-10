package me.alpha12.ecarnet.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.activities.MainActivity;
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
 * Use the {@link GasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GasFragment extends Fragment {
    private int mMenuEntryId;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param menuEntryId Drawer Menu Item Id.
     * @return A new instance of fragment GasFragment.
     */
    public static GasFragment newInstance(int menuEntryId) {
        GasFragment fragment = new GasFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivity.FRAGMENT_MENU_ENTRY_ID, menuEntryId);
        fragment.setArguments(args);
        return fragment;
    }

    public GasFragment() {
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

        View view = inflater.inflate(R.layout.fragment_gas, container, false);

        Car currentCar = ((MainActivity) getActivity()).currentCar;



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



        TextView kmTotalText = (TextView) view.findViewById(R.id.kmTotal);
        kmTotalText.setText("Compteur total");
        TextView qteText = (TextView) view.findViewById(R.id.qte);
        qteText.setText("Quantité");
        TextView priceText = (TextView) view.findViewById(R.id.price);
        priceText.setText("Prix");

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
