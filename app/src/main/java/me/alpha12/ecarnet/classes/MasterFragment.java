package me.alpha12.ecarnet.classes;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;

import me.alpha12.ecarnet.activities.MainActivity;
import me.alpha12.ecarnet.interfaces.OnFragmentInteractionListener;
import me.alpha12.ecarnet.models.Car;

public abstract class MasterFragment extends Fragment implements View.OnClickListener {
    private FloatingActionButton fab;

    protected int fragmentId;
    protected MainActivity parentActivity;
    protected OnFragmentInteractionListener mListener;

    protected Car currentCar;

    private String defaultTitle;
    private String defaultSubTitle;

    public MasterFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            fragmentId = getArguments().getInt(MainActivity.FRAGMENT_MENU_ENTRY_ID);
            parentActivity = (MainActivity) getActivity();
            currentCar = parentActivity.currentCar;
        }
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
    public void onResume() {
        super.onResume();
        invalidateTopBar();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) { }

    public void setDefaultTitle(String value) {
        this.defaultTitle = value;
        invalidateTopBar();
    }

    public String getDefaultTitle() {
        return defaultTitle;
    }

    public void setDefaultSubTitle(String value) {
        this.defaultSubTitle = value;
        invalidateTopBar();
    }

    public void setFragmentId(int fragmentId) {
        Bundle args = new Bundle();
        args.putInt(MainActivity.FRAGMENT_MENU_ENTRY_ID, fragmentId);
        this.setArguments(args);
    }

    public void registerFloatingActionButton(int fabId) {
        fab = (FloatingActionButton) parentActivity.findViewById(fabId);
        fab.setOnClickListener(this);
        parentActivity.registerFloatingActionButton(fragmentId, fab);
    }

    private void invalidateTopBar() {
        parentActivity.setTitle(defaultTitle);
        parentActivity.setSubTitle(defaultSubTitle);
    }
}
