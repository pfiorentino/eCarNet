package me.alpha12.ecarnet.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import me.alpha12.ecarnet.R;

public class IncompleteConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomplete_config);

        TextView changeAccountTextView = (TextView) findViewById(R.id.changeAccountTextView);
        changeAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IncompleteConfigActivity.this, AddUserActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button finishConfigButton = (Button) findViewById(R.id.finishConfigButton);
        finishConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IncompleteConfigActivity.this, AddCarActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
