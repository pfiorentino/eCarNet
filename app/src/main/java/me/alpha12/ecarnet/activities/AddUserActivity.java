package me.alpha12.ecarnet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.database.EcarnetHelper;
import me.alpha12.ecarnet.models.User;

/**
 * Created by guilhem on 20/12/2015.
 */
public class AddUserActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView firstNameText;
    private TextView lastNameText;
    private TextView emailText;
    private Button addUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addUserButton = (Button) findViewById(R.id.btn_addUser);
        addUserButton.setOnClickListener(this);

        firstNameText = (TextView) findViewById(R.id.firstName);
        lastNameText = (TextView) findViewById(R.id.lastName);
        emailText = (TextView) findViewById(R.id.email);
    }

    @Override
    public void onClick(View v) {
        if(firstNameText.getText() != null && lastNameText.getText() != null && emailText.getText() != null)
        {
            User.addUser(new User(0,firstNameText.getText().toString(),lastNameText.getText().toString(),emailText.getText().toString(), "azerty"), EcarnetHelper.bdd);
            Toast.makeText(getBaseContext(), "Added user : " + firstNameText.getText().toString() + "  " + lastNameText.getText().toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddUserActivity.this, AddCarActivity.class);
            AddUserActivity.this.startActivity(intent);
        }
    }
}
