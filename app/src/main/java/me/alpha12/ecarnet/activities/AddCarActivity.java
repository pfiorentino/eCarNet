package me.alpha12.ecarnet.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import me.alpha12.ecarnet.Adapters.ModelAdapter;
import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.database.EcarnetHelper;
import me.alpha12.ecarnet.models.Model;

public class AddCarActivity extends AppCompatActivity {


    //values of spinners
    private ArrayList<String> brands = Model.getBrands(EcarnetHelper.bdd);
    private ArrayList<String> models = new ArrayList<String>();
    private ArrayList<Model> subModel = new ArrayList<Model>();


    //affiliated to search bar
    private List<Model> allModels;
    private List<Model> filteredModels;
    private boolean searchOpened;
    private String currentQuery;
    private ListView modelListView;
    private Drawable iconOpenSearch;
    private Drawable iconCloseSearch;
    private EditText searchTextEdit;
    private MenuItem searchAction;
    private Model selectedCar;


    private final static String MODEL = "model";
    private final static String MODEL_FILTERED = "filtered";
    private final static String SEARCH_OPENED = "opened";
    private final static String SEARCH_QUERY = "query";



    //value of selected item;
    private String currentBrand;
    private String currentModel;

    private ArrayAdapter brandAdapter;
    private ArrayAdapter modelAdapter;
    private ArrayAdapter subModelAdapter;

    //spinners

    private Spinner subModelSpinner;
    private AutoCompleteTextView brandText;
    private AutoCompleteTextView modelText;


    private Button addCarButton;

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
            searchOpened = false;
            currentQuery = "";
        } else {
            allModels = savedInstanceState.getParcelableArrayList(MODEL);
            filteredModels = savedInstanceState.getParcelableArrayList(MODEL_FILTERED);
            searchOpened = savedInstanceState.getBoolean(SEARCH_OPENED);
            currentQuery = savedInstanceState.getString(SEARCH_QUERY);
        }


        iconOpenSearch = getResources().getDrawable(R.drawable.ic_search_black_24dp);
        iconCloseSearch = getResources().getDrawable(R.drawable.ic_close_black_24dp);

        modelListView = (ListView) findViewById(R.id.modelList);

        this.adapter = new ModelAdapter(this, R.id.modelList, filteredModels);
        modelListView.setAdapter(adapter);

        modelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Model model = (Model)modelListView.getItemAtPosition(position);
                System.out.println(model.toString());
                Intent intent = new Intent(AddCarActivity.this, AddCarPersonaliseActivity.class);
                intent.putExtra("id", model.getId());
                AddCarActivity.this.startActivity(intent);
            }
        });

        if (searchOpened) {
            openSearchBar(currentQuery);
        }

/*
        currentBrand = "";
        currentModel = "";

        //findView of spinners
        subModelSpinner = (Spinner) findViewById(R.id.sub_model_spinner);
        brandText = (AutoCompleteTextView) findViewById(R.id.brand);
        modelText = (AutoCompleteTextView) findViewById(R.id.model);
        modelText.setEnabled(false);
        subModelSpinner.setEnabled(false);


        addCarButton = (Button) findViewById(R.id.btn_addUser);
        addCarButton.setEnabled(false);

        //initialize default value
        brandAdapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, brands);
        brandText.setAdapter(brandAdapter);
        brandText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentBrand = parent.getItemAtPosition(position).toString();
                models = Model.getModelFromBrand(EcarnetHelper.bdd, currentBrand);
                modelAdapter = new ArrayAdapter(parent.getContext(), android.R.layout.simple_dropdown_item_1line, models);
                modelText.setAdapter(modelAdapter);
                modelText.setEnabled(true);
                subModel = new ArrayList<Model>();
                subModelAdapter = new ArrayAdapter(parent.getContext(), android.R.layout.simple_dropdown_item_1line, subModel);
                subModelSpinner.setAdapter(subModelAdapter);
            }
        });


        modelText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentModel = parent.getItemAtPosition(position).toString();
                subModel = Model.getModelFromBrandModel(EcarnetHelper.bdd, currentBrand, currentModel);
                subModelSpinner.setEnabled(true);
                subModelAdapter = new ArrayAdapter(parent.getContext(), android.R.layout.simple_dropdown_item_1line, subModel);
                subModelSpinner.setAdapter(subModelAdapter);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subModelSpinner.setOnItemSelectedListener(this);
        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Car.addCar(new Car(0, "CZ-123-PB", (Model) subModelSpinner.getSelectedItem()), EcarnetHelper.bdd);
                User.activateUser(EcarnetHelper.bdd);
                Toast.makeText(getBaseContext(), "added car : " + currentBrand + " - " + currentModel, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddCarActivity.this, MainActivity.class);
                AddCarActivity.this.startActivity(intent);
            }
        });

    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (subModelSpinner.getSelectedItem() != null)
            addCarButton.setEnabled(true);
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
        return;
    }

*/
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MODEL, (ArrayList<Model>) allModels);
        outState.putParcelableArrayList(MODEL_FILTERED, (ArrayList<Model>) filteredModels);
        outState.putBoolean(SEARCH_OPENED, searchOpened);
        outState.putString(SEARCH_QUERY, currentQuery);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
            if (searchOpened) {
                closeSearchBar();
            } else {
                openSearchBar(currentQuery);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSearchBar(String queryText) {

        // Set custom view on action bar.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.search);

        // Search edit text field setup.
        searchTextEdit = (EditText) actionBar.getCustomView().findViewById(R.id.etSearch);
        searchTextEdit.addTextChangedListener(new CarSearch());
        searchTextEdit.setText(queryText);
        searchTextEdit.requestFocus();

        // Change search icon accordingly.
        actionBar.setIcon(iconCloseSearch);
        searchOpened = true;

    }

    private void closeSearchBar() {

        // Remove custom view.
        getSupportActionBar().setDisplayShowCustomEnabled(false);

        // Change search icon accordingly.
        searchAction.setIcon(iconOpenSearch);
        searchOpened = false;

    }


    private class CarSearch implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence c, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence c, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            currentQuery = searchTextEdit.getText().toString();
            filteredModels = performSearch(allModels, currentQuery);
            getListAdapter().update(filteredModels);
        }


        private ModelAdapter getListAdapter() {
            return (ModelAdapter) modelListView.getAdapter();
        }

    }



    private List<Model> performSearch(List<Model> models, String query) {

        // First we split the query so that we're able
        // to search word by word (in lower case).
        String[] queryByWords = query.toLowerCase().split("\\s+");

        // Empty list to fill with matches.
        List<Model> modelsFiltered = new ArrayList<Model>();

        // Go through initial releases and perform search.
        for (Model model : allModels) {

            // Content to search through (in lower case).
            String content = (
                    model.getBrand() + " " + model.getModel() + " " + String.valueOf(model.getYear()) + " " + model.getEngine() + " " +
                            model.getRatedHP() + " " + model.getEnergy())
                    .toLowerCase();

            for (String word : queryByWords) {

                // There is a match only if all of the words are contained.
                int numberOfMatches = queryByWords.length;

                // All query words have to be contained,
                // otherwise the release is filtered out.
                if (content.contains(word)) {
                    numberOfMatches--;
                } else {
                    break;
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
