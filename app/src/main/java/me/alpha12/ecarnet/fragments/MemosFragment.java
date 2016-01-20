package me.alpha12.ecarnet.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.activities.MainActivity;
import me.alpha12.ecarnet.adapters.MemoRecyclerAdapter;
import me.alpha12.ecarnet.interfaces.OnFragmentInteractionListener;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.Memo;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemosFragment extends Fragment {
    private int mMenuEntryId;

    private OnFragmentInteractionListener mListener;
    private RecyclerView myMemos;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param menuEntryId Drawer Menu Item Id.
     * @return A new instance of fragment ShareFragment.
     */
    public static MemosFragment newInstance(int menuEntryId) {
        MemosFragment fragment = new MemosFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivity.FRAGMENT_MENU_ENTRY_ID, menuEntryId);
        fragment.setArguments(args);
        return fragment;
    }

    public MemosFragment() {
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
        View view = inflater.inflate(R.layout.fragment_memo, container, false);
        Car currentCar = ((MainActivity) getActivity()).currentCar;

        ArrayList<Memo> memoList = new ArrayList<>();
        memoList = Memo.findAllByCar(currentCar.getId());

        if(!memoList.isEmpty()) {
            myMemos = (RecyclerView) view.findViewById(R.id.memoList);

            myMemos.setHasFixedSize(true);
            myMemos.setAdapter(new MemoRecyclerAdapter(getContext(), memoList));
            myMemos.setLayoutManager(new LinearLayoutManager(getContext()));
        }
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
