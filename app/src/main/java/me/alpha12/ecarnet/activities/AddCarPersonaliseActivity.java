package me.alpha12.ecarnet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.database.EcarnetHelper;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.Model;
import me.alpha12.ecarnet.models.User;

/**
 * Created by guilhem on 04/01/2016.
 */
public class AddCarPersonaliseActivity extends AppCompatActivity {

    //affiliated to search bar
    private EditText imat;
    private EditText kilometers;
    private Model selectedCar;
    private Button addCarButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_personalise);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int idModel = getIntent().getExtras().getInt("id");
        Log.d("idModel", idModel+"");
        selectedCar = Model.getModelById(EcarnetHelper.bdd, idModel);

        imat = (EditText) findViewById(R.id.imat);
        kilometers = (EditText) findViewById(R.id.kilometers);

        addCarButton = (Button) findViewById(R.id.btn_save);
        addCarButton.setEnabled(true);

        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car.addCar(new Car(0, imat.getText().toString(), selectedCar), EcarnetHelper.bdd);
                User.activateUser(EcarnetHelper.bdd);
                Toast.makeText(getBaseContext(), "added car : " + selectedCar.getBrand() + " - " + selectedCar.getModel(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddCarPersonaliseActivity.this, MainActivity.class);
                AddCarPersonaliseActivity.this.startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
