package me.alpha12.ecarnet.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.adapters.CarInfoPagerAdapter;
import me.alpha12.ecarnet.models.Car;

public class CarProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CollapsingToolbarLayout toolbarLayout;
    private TextView subtitleTextView;
    private ImageView toolbarBackgroundImage;

    private Car currentCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_profile);

        currentCar = Car.get(getIntent().getIntExtra("carId", -1));

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbarLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        CarInfoPagerAdapter adapter = new CarInfoPagerAdapter(this);
        adapter.setData(currentCar);
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        subtitleTextView = (TextView) findViewById(R.id.subtitle);
        toolbarBackgroundImage = (ImageView) findViewById(R.id.toolbarBackgroundImage);

        if (currentCar == null){
            Log.e("eCarNet error", "Car not found");
        } else {
            toolbarLayout.setTitle(currentCar.getModelString());
            toolbar.setTitle(currentCar.getModelString());
            if (currentCar.isDefined())
                subtitleTextView.setText(currentCar.getDetails());
            toolbarBackgroundImage.setImageDrawable(currentCar.getCarBanner(this));
        }

    }

}
