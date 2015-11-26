package me.alpha12.ecarnet.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.classes.Car;
import me.alpha12.ecarnet.classes.Model;
import me.alpha12.ecarnet.classes.OnFragmentInteractionListener;
import me.alpha12.ecarnet.fragments.GasFragment;
import me.alpha12.ecarnet.fragments.HomeFragment;
import me.alpha12.ecarnet.fragments.OperationsFragment;
import me.alpha12.ecarnet.fragments.ShareFragment;
import me.alpha12.ecarnet.fragments.TagsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {
    public final static String FRAGMENT_MENU_ENTRY_ID = "fmei";

    public HashMap<String, Car> cars = new HashMap<>();
    public Car currentCar;

    NavigationView navigationView;
    FloatingActionButton fab;
    Toolbar toolbar;

    boolean isProfilesMenuOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_fragment_home);
        setSupportActionBar(toolbar);

        Model model1 = new Model("Renault", "Clio 2.2", "1.5l DCI 65ch");
        Model model2 = new Model("Peugeot", "206+", "1.4l 70ch");
        Model model3 = new Model("Citroën", "Saxo", "1.0l 50ch");

        cars.put("uuid_101", new Car(101, "71 AFB 34", model1));
        cars.put("uuid_102", new Car(102, "CT 091 DQ", model2));
        cars.put("uuid_203", new Car(203, "XX 180 TG", model3));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FillUpActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

        for(Map.Entry<String, Car> carEntry : cars.entrySet()) {
            navigationView.getMenu().add(R.id.cars_mgmt_group, carEntry.getValue().uuid, 0, carEntry.getValue().plateNum+" - "+carEntry.getValue().model.engine);
        }

        navigationView.getMenu().setGroupVisible(R.id.cars_mgmt_group, false);

        LinearLayout profileSpinner = (LinearLayout) findViewById(R.id.profile_spinner);
        profileSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProfilesMenuOpen = !isProfilesMenuOpen;
                refreshProfilesMenu();
            }
        });

        changeCar(cars.entrySet().iterator().next().getValue());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, HomeFragment.newInstance(R.id.nav_home), "CURRENT_FRAGMENT").commit();

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        Fragment currentFragment = (Fragment) getSupportFragmentManager().findFragmentByTag("CURRENT_FRAGMENT");
                        if (navigationView != null && currentFragment != null && currentFragment.isVisible()) {
                            navigationView.getMenu().findItem(currentFragment.getArguments().getInt(FRAGMENT_MENU_ENTRY_ID)).setChecked(true);
                            setFABVisibility(currentFragment.getArguments().getInt(FRAGMENT_MENU_ENTRY_ID));
                            setFragmentTitle(currentFragment.getArguments().getInt(FRAGMENT_MENU_ENTRY_ID));
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (findViewById(R.id.fragment_container) != null) {
            if (id == R.id.nav_home) {
                fab.show();
                toolbar.setTitle(R.string.title_fragment_home);
                openMainFragment(HomeFragment.newInstance(id));
            } else if (id == R.id.nav_gas) {
                fab.show();
                toolbar.setTitle(R.string.title_fragment_gas);
                openMainFragment(GasFragment.newInstance(id));
            } else if (id == R.id.nav_repair) {
                fab.hide();
                toolbar.setTitle(R.string.title_fragment_operations);
                openMainFragment(OperationsFragment.newInstance(id));
            } else if (id == R.id.nav_share) {
                fab.hide();
                toolbar.setTitle(R.string.title_fragment_share);
                openMainFragment(ShareFragment.newInstance(id));
            } else if (id == R.id.nav_nfc) {
                fab.hide();
                toolbar.setTitle(R.string.title_fragment_tags);
                openMainFragment(TagsFragment.newInstance(id));
            } else if (id == R.id.nav_add_car) {
                Intent intent = new Intent(this.getBaseContext(), AddCarActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_manage_car) {
                Intent intent = new Intent(this.getBaseContext(), CarsMgmtActivity.class);
                startActivity(intent);
            } else {
                Car selectedCar = cars.get("uuid_"+id);
                if (selectedCar != null){
                    changeCar(selectedCar);
                }
            }

            closeDrawer();
        }

        return true;
    }

    public void setFragmentTitle(int menuItemId) {
        if (toolbar != null) {
            if (menuItemId == R.id.nav_home) {
                toolbar.setTitle(R.string.title_fragment_home);
            } else if (menuItemId == R.id.nav_gas) {
                toolbar.setTitle(R.string.title_fragment_gas);
            } else if (menuItemId == R.id.nav_repair) {
                toolbar.setTitle(R.string.title_fragment_operations);
            } else if (menuItemId == R.id.nav_share) {
                toolbar.setTitle(R.string.title_fragment_share);
            } else if (menuItemId == R.id.nav_nfc) {
                toolbar.setTitle(R.string.title_fragment_tags);
            }
        }
    }

    public void setFABVisibility(int menuItemId) {
        if (fab != null) {
            switch (menuItemId) {
                case R.id.nav_home:
                case R.id.nav_gas:
                    fab.show();
                    break;
                default:
                    fab.hide();
            }
        }
    }

    public void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void openMainFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, "CURRENT_FRAGMENT");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void changeCar(Car newCar) {
        LinearLayout header = (LinearLayout) findViewById(R.id.drawer_header);
        ImageView brandImageView = (ImageView) findViewById(R.id.brand_image_view);

        if (currentCar != null){
            navigationView.getMenu().add(R.id.cars_mgmt_group, currentCar.uuid, 0, currentCar.plateNum + " - " + currentCar.model.engine);
        }
        navigationView.getMenu().removeItem(newCar.uuid);

        isProfilesMenuOpen = false;
        refreshProfilesMenu();

        currentCar = newCar;

        TextView drawerTitle = (TextView) findViewById(R.id.car_name);
        drawerTitle.setText(currentCar.model.brand + " " + currentCar.model.model);

        TextView drawerDesc = (TextView) findViewById(R.id.car_desc);
        drawerDesc.setText(currentCar.plateNum + " - " + currentCar.model.engine);

        switch (currentCar.model.brand){
            case "Renault":
                header.setBackgroundResource(R.drawable.background_renault);
                brandImageView.setImageResource(R.mipmap.ic_renault);
                break;
            case "Peugeot":
                header.setBackgroundResource(R.drawable.background_peugeot);
                brandImageView.setImageResource(R.mipmap.ic_peugeot);
                break;
            case "Citroën":
                header.setBackgroundResource(R.drawable.background_citroen);
                brandImageView.setImageResource(R.mipmap.ic_citroen);
                break;
        }

        LinearLayout carsLayout = (LinearLayout) findViewById(R.id.cars_icon_layout);
        carsLayout.removeViews(2, carsLayout.getChildCount() - 2);

        for(Map.Entry<String, Car> carEntry : cars.entrySet()) {
            Car car = carEntry.getValue();

            if (currentCar != null && car.uuid != currentCar.uuid) {
                ImageView carImage = new ImageView(getBaseContext());
                switch (car.model.brand) {
                    case "Renault":
                        carImage.setImageResource(R.mipmap.ic_renault);
                        break;
                    case "Peugeot":
                        carImage.setImageResource(R.mipmap.ic_peugeot);
                        break;
                    case "Citroën":
                        carImage.setImageResource(R.mipmap.ic_citroen);
                        break;
                }

                int size = (int) getResources().getDimension(R.dimen.other_car_size);
                int spacing = (int) getResources().getDimension(R.dimen.other_car_spacing);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
                layoutParams.setMargins(spacing, 0, 0, 0);
                carImage.setLayoutParams(layoutParams);
                carImage.setClickable(true);
                carImage.setTag(car);

                carImage.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN: {
                                ImageView view = (ImageView) v;
                                //overlay is black with transparency of 0x77 (119)
                                view.getDrawable().setColorFilter(getResources().getColor(R.color.colorAccentAlpha), PorterDuff.Mode.SRC_ATOP);
                                view.invalidate();
                                break;
                            }
                            case MotionEvent.ACTION_UP: {
                                Car associatedCar = (Car) v.getTag();
                                changeCar(associatedCar);
                            }
                            case MotionEvent.ACTION_CANCEL: {
                                ImageView view = (ImageView) v;
                                //clear the overlay
                                view.getDrawable().clearColorFilter();
                                view.invalidate();
                                break;
                            }
                        }

                        return true;
                    }
                });

                carsLayout.addView(carImage);
            }
        }

        closeDrawer();
    }

    public void refreshProfilesMenu() {
        navigationView.getMenu().setGroupVisible(R.id.views_group, !isProfilesMenuOpen);
        navigationView.getMenu().setGroupVisible(R.id.misc_group, !isProfilesMenuOpen);
        navigationView.getMenu().findItem(R.id.share_group_item).setVisible(!isProfilesMenuOpen);

        navigationView.getMenu().setGroupVisible(R.id.cars_mgmt_group, isProfilesMenuOpen);

        ImageView spinnerTrigger = (ImageView) findViewById(R.id.spinner_trigger);
        if (!isProfilesMenuOpen){
            spinnerTrigger.setImageResource(R.drawable.ic_arrow_drop_down_white_24dp);
        } else {
            spinnerTrigger.setImageResource(R.drawable.ic_arrow_drop_up_white_24dp);
        }
    }
}
