package me.alpha12.ecarnet.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import me.alpha12.ecarnet.BuildConfig;
import me.alpha12.ecarnet.R;

public class AboutActivity extends AppCompatActivity {
    private TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        version = (TextView) findViewById(R.id.version_text_view);
        version.setText(getString(R.string.app_version ,BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
    }
}
