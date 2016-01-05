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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.alpha12.ecarnet.Adapters.ModelAdapter;
import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.database.EcarnetHelper;
import me.alpha12.ecarnet.models.Model;

public class AddCarActivity extends AppCompatActivity {


    //affiliated to search bar
    private List<Model> allModels;
    private List<Model> filteredModels;
    private boolean isSearchOpened = false;
    private EditText searchTextEdit;
    private MenuItem searchAction;

    private String currentQuery;
    private ListView modelListView;
    private Drawable iconOpenSearch;
    private Drawable iconCloseSearch;
    private Model selectedCar;


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
        setContentView(R.layout.activity_add_car);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (savedInstanceState == null) {
            allModels = Model.getAllModel(EcarnetHelper.bdd);
            filteredModels = allModels;
            isSearchOpened = false;
            currentQuery = "";
        } else {
            allModels = savedInstanceState.getParcelableArrayList(MODEL);
            filteredModels = savedInstanceState.getParcelableArrayList(MODEL_FILTERED);
            isSearchOpened = savedInstanceState.getBoolean(SEARCH_OPENED);
            currentQuery = savedInstanceState.getString(SEARCH_QUERY);
        }

        for (int i = 0; i < filteredModels.size(); i++)
        {
            if(!years.contains(new Integer(filteredModels.get(i).getYear())))
                years.add(filteredModels.get(i).getYear());
            if(!ratedHP.contains(new Integer(filteredModels.get(i).getRatedHP())))
                ratedHP.add(filteredModels.get(i).getRatedHP());
        }

        iconOpenSearch = getResources().getDrawable(R.drawable.ic_search_white_24dp);
        iconCloseSearch = getResources().getDrawable(R.drawable.ic_clear_white_24dp);

        modelListView = (ListView) findViewById(R.id.modelList);

        this.adapter = new ModelAdapter(this, R.id.modelList, filteredModels);
        modelListView.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        modelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Model model = (Model) modelListView.getItemAtPosition(position);
                System.out.println(model.toString());
                Intent intent = new Intent(AddCarActivity.this, AddCarPersonaliseActivity.class);
                intent.putExtra("id", model.getId());
                AddCarActivity.this.startActivity(intent);
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MODEL, (ArrayList<Model>) allModels);
        outState.putParcelableArrayList(MODEL_FILTERED, (ArrayList<Model>) filteredModels);
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
            filteredModels = allModels;
            adapter = new ModelAdapter(getBaseContext(), R.id.modelList, filteredModels);
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
                    filteredModels = performSearch(currentQuery);
                    adapter = new ModelAdapter(getBaseContext(), R.id.modelList, filteredModels);
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


    private List<Model> performSearch(String query) {

        // First we split the query so that we're able
        // to search word by word (in lower case).
        String[] queryByWords = query.toLowerCase().split(" ");

        // Empty list to fill with matches.
        List<Model> modelsFiltered = new ArrayList<Model>();

        // Go through initial releases and perform search.
        for (Model model : allModels) {

            // Content to search through (in lower case).
            String content = (
                    model.getBrand() + " " + model.getModel() + " " + String.valueOf(model.getYear()) + " " + model.getEngine() + " " +
                            model.getRatedHP() + " " + model.getEnergy() + model.getSubModel())
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
}
