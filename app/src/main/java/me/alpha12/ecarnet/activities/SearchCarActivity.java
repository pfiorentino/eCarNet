package me.alpha12.ecarnet.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.adapters.ModelAdapter;
import me.alpha12.ecarnet.models.CarModel;

public class SearchCarActivity extends AppCompatActivity {


    //affiliated to search bar
    private List<CarModel> allCarModels;
    private List<CarModel> filteredCarModels;
    private boolean isSearchOpened = false;
    private EditText searchTextEdit;
    private MenuItem searchAction;

    private String currentQuery;
    private ListView modelListView;
    private Drawable iconOpenSearch;
    private Drawable iconCloseSearch;
    private CarModel selectedCar;


    private final static String MODEL = "model";
    private final static String MODEL_FILTERED = "filtered";
    private final static String SEARCH_OPENED = "opened";
    private final static String SEARCH_QUERY = "query";

    //spinners
    private List<Integer> years = new ArrayList<Integer>();
    private List<Integer> ratedHP = new ArrayList<Integer>();

    private ModelAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_car);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if(intent.getStringExtra("brand") != null && intent.getStringExtra("model") != null) {
                allCarModels = CarModel.findByBrandModel(intent.getStringExtra("brand"), intent.getStringExtra("model"));
            }
            filteredCarModels = allCarModels;
            isSearchOpened = false;
            currentQuery = "";
        } else {
            allCarModels = savedInstanceState.getParcelableArrayList(MODEL);
            filteredCarModels = savedInstanceState.getParcelableArrayList(MODEL_FILTERED);
            isSearchOpened = savedInstanceState.getBoolean(SEARCH_OPENED);
            currentQuery = savedInstanceState.getString(SEARCH_QUERY);
        }

        for (int i = 0; i < filteredCarModels.size(); i++)
        {
            if(!years.contains(new Integer(filteredCarModels.get(i).getYear())))
                years.add(filteredCarModels.get(i).getYear());
            if(!ratedHP.contains(new Integer(filteredCarModels.get(i).getRatedHP())))
                ratedHP.add(filteredCarModels.get(i).getRatedHP());
        }

        iconOpenSearch = getResources().getDrawable(R.drawable.ic_search_white_24dp);
        iconCloseSearch = getResources().getDrawable(R.drawable.ic_clear_white_24dp);

        modelListView = (ListView) findViewById(R.id.modelList);

        this.adapter = new ModelAdapter(this, R.id.modelList, filteredCarModels);
        modelListView.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        modelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CarModel model = (CarModel) modelListView.getItemAtPosition(position);

                Intent intent = new Intent(SearchCarActivity.this, CustomizeCarActivity.class);
                intent.putExtra("id", model.getId());
                SearchCarActivity.this.startActivityForResult(intent, 0);
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MODEL, (ArrayList<CarModel>) allCarModels);
        outState.putParcelableArrayList(MODEL_FILTERED, (ArrayList<CarModel>) filteredCarModels);
        outState.putBoolean(SEARCH_OPENED, isSearchOpened);
        outState.putString(SEARCH_QUERY, currentQuery);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_car_drawer, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        searchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.action_search) {
            handleMenuSearch();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void handleMenuSearch()
    {
        ActionBar action = getSupportActionBar();
        if (isSearchOpened) {
            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true);

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            searchTextEdit = (EditText)action.getCustomView().findViewById(R.id.etSearch);
            filteredCarModels = allCarModels;
            adapter = new ModelAdapter(getBaseContext(), R.id.modelList, filteredCarModels);
            modelListView.setAdapter(adapter);



            searchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_white_24dp));
            isSearchOpened = false;
        } else {
            action.setDisplayShowCustomEnabled(true);
            action.setCustomView(R.layout.search);
            action.setDisplayShowTitleEnabled(false);
            searchTextEdit = (EditText)action.getCustomView().findViewById(R.id.etSearch);
            searchTextEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    currentQuery = searchTextEdit.getText().toString();
                    filteredCarModels = performSearch(currentQuery);
                    adapter = new ModelAdapter(getBaseContext(), R.id.modelList, filteredCarModels);
                    modelListView.setAdapter(adapter);
                }
            });
            searchTextEdit.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchTextEdit, InputMethodManager.SHOW_IMPLICIT);
            searchAction.setIcon(getResources().getDrawable(R.drawable.ic_clear_white_24dp));
            isSearchOpened = true;
        }
    }


    private List<CarModel> performSearch(String query) {

        // First we split the query so that we're able
        // to search word by word (in lower case).
        String[] queryByWords = query.toLowerCase().split(" ");

        // Empty list to fill with matches.
        List<CarModel> modelsFiltered = new ArrayList<CarModel>();

        // Go through initial releases and perform search.
        for (CarModel model : allCarModels) {

            // Content to search through (in lower case).
            String content = (
                    model.getBrand() + " " + model.getModel() + " " + String.valueOf(model.getYear()) + " " + model.getEngine() + " " +
                            model.getRatedHP()+"CV" + " " + model.getEnergy() + model.getSubModel())
                    .toLowerCase();

            int numberOfMatches = queryByWords.length;

            for (String word : queryByWords) {

                Log.d("search_debug", content+" - "+word);
                // There is a match only if all of the words are contained.


                // All query words have to be contained,
                // otherwise the release is filtered out.
                if (content.contains(word)) {
                    numberOfMatches--;
                }

                // They all match.
                if (numberOfMatches == 0) {
                    modelsFiltered.add(model);
                }
            }
        }
        return modelsFiltered;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode) {
            case MainActivity.RESULT_CLOSE_ALL:
                setResult(MainActivity.RESULT_CLOSE_ALL);
                finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
