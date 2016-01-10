package me.alpha12.ecarnet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

        addUserButton = (Button) findViewById(R.id.addUser);
        addUserButton.setOnClickListener(this);

        firstNameText = (TextView) findViewById(R.id.firstname);
        firstNameText.addTextChangedListener(mTextWatcher);
        lastNameText = (TextView) findViewById(R.id.lastname);
        lastNameText.addTextChangedListener(mTextWatcher);
        emailText = (TextView) findViewById(R.id.email);
        emailText.addTextChangedListener(mTextWatcher);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) { }
        @Override
        public void afterTextChanged(Editable editable) {
            addUserButton.setEnabled(
                    !firstNameText.getText().toString().matches("") &&
                    !lastNameText.getText().toString().matches("") &&
                    !emailText.getText().toString().matches("")
            );
        }
    };

    @Override
    public void onClick(View v) {
        User.addUser(new User(0, firstNameText.getText().toString(), lastNameText.getText().toString(), emailText.getText().toString(), "azerty"), EcarnetHelper.bdd);
        User.activateUser(EcarnetHelper.bdd);
        Intent intent = new Intent(this, AddCarActivity.class);
        startActivityForResult(intent, 0);
        finish();
    }
}
