package me.alpha12.ecarnet.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import me.alpha12.ecarnet.GlobalContext;
import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.fragments.GasFragment;
import me.alpha12.ecarnet.fragments.HomeFragment;
import me.alpha12.ecarnet.fragments.MemosFragment;
import me.alpha12.ecarnet.fragments.OperationsFragment;
import me.alpha12.ecarnet.fragments.ShareFragment;
import me.alpha12.ecarnet.fragments.TagsFragment;
import me.alpha12.ecarnet.interfaces.OnFragmentInteractionListener;
import me.alpha12.ecarnet.models.Car;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {
    public static final String FRAGMENT_MENU_ENTRY_ID = "fmei";

    private AppBarLayout appbar;
    private CollapsingToolbarLayout appbarLayout;
    private NestedScrollView scrollView;
    private Toolbar supportToolbar;

    private NavigationView navigationView;
    private View headerView;

    private FloatingActionButton addFillupFAB;
    private FloatingActionButton addOperationFAB;
    private FloatingActionButton addMemoFAB;

    private ImageView appBarImage;

    public HashMap<Integer, Car> cars = new HashMap<>();
    public Car currentCar;

    private boolean isProfilesMenuOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appbar          = (AppBarLayout) findViewById(R.id.appbar);
        scrollView      = (NestedScrollView) findViewById(R.id.scrollView);
        appbarLayout    = (CollapsingToolbarLayout) findViewById(R.id.appbarLayout);
        supportToolbar  = (Toolbar) findViewById(R.id.supportToolBar);
        setSupportActionBar(supportToolbar);


        cars = Car.findAll();

        addFillupFAB = (FloatingActionButton) findViewById(R.id.addFillupFAB);
        addMemoFAB = (FloatingActionButton) findViewById(R.id.addMemoFAB);
        addFillupFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FillUpActivity.class);
                intent.putExtra("carId", currentCar.getId());
                startActivity(intent);
            }
        });

        addMemoFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddMemoActivity.class);
                intent.putExtra("carId", currentCar.getId());
                startActivity(intent);
            }
        });

        addOperationFAB = (FloatingActionButton) findViewById(R.id.addOperationFAB);
        addOperationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddInterventionActivity.class);
                intent.putExtra("carId", currentCar.getId());
                startActivity(intent);
            }
        });

        appBarImage = (ImageView) findViewById(R.id.appBarImage);

        initNavDrawer();

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                            onBackPressed();
                        } else {
                            Fragment currentFragment = (Fragment) getSupportFragmentManager().findFragmentByTag("CURRENT_FRAGMENT");
                            if (navigationView != null && currentFragment != null && currentFragment.isVisible()) {
                                fragmentSelected(currentFragment.getArguments().getInt(FRAGMENT_MENU_ENTRY_ID));
                            }
                        }
                    }
                });


        int savedCarId = GlobalContext.getCurrentCar();
        Car savedCar = cars.get(savedCarId);
        if (savedCar != null) {
            changeCar(savedCar, true);
        } else {
            changeCar(cars.entrySet().iterator().next().getValue(), true);
        }
        if(getIntent().getExtras() != null) {
            String dateAlarm = getIntent().getExtras().getString("dateRappel");
            if (dateAlarm != null) {
                Toast.makeText(this, "Un rappel sera effectué le " + dateAlarm, Toast.LENGTH_LONG).show();
            }
        }
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int menuItemId = item.getItemId();

        switch (menuItemId) {
            case R.id.nav_home:
                openMainFragment(HomeFragment.newInstance(menuItemId), menuItemId);
                break;
            case R.id.nav_gas:
                openMainFragment(GasFragment.newInstance(menuItemId), menuItemId);
                break;
            case R.id.nav_repair:
                openMainFragment(OperationsFragment.newInstance(menuItemId), menuItemId);
                break;
            case R.id.nav_share:
                openMainFragment(ShareFragment.newInstance(menuItemId), menuItemId);
                break;
            case R.id.nav_nfc:
                openMainFragment(TagsFragment.newInstance(menuItemId), menuItemId);
                break;
            case R.id.nav_memos:
                openMainFragment(MemosFragment.newInstance(menuItemId), menuItemId);
                break;
            case R.id.nav_add_car:
                Intent intent = new Intent(this, AddCarActivity.class);
                intent.putExtra(AddCarActivity.FROM_MAIN_ACTIVITY, true);
                startActivityForResult(intent, 0);
                break;
            case R.id.nav_manage_car:
                openActivity(CarsMgmtActivity.class);
                break;
            case R.id.nav_about:
                openActivity(AboutActivity.class);
                break;
            default:
                Car selectedCar = cars.get(menuItemId);
                if (selectedCar != null){
                    changeCar(selectedCar, true);
                }
        }

        closeDrawer();
        return true;
    }

    public void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void openActivity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    public void openMainFragment(Fragment fragment, int fragmentId) {
        if (findViewById(R.id.fragment_container) != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (getSupportFragmentManager().getBackStackEntryCount() == 0){
                transaction.add(R.id.fragment_container, fragment, "CURRENT_FRAGMENT");
                transaction.addToBackStack("FIRST_FRAGMENT");
            } else {
                transaction.replace(R.id.fragment_container, fragment, "CURRENT_FRAGMENT");
                transaction.addToBackStack(null);
            }

            transaction.commit();

            fragmentSelected(fragmentId);
        }
    }

    public void fragmentSelected(int fragmentId) {
        navigationView.getMenu().findItem(fragmentId).setChecked(true);


        setAppBarScrollEnabled(fragmentId == R.id.nav_home);
        if (fragmentId == R.id.nav_home) {
            setTitle(currentCar.getCarModel().toString());
            addFillupFAB.show();
            addOperationFAB.hide();
            addMemoFAB.hide();
        } else if (fragmentId == R.id.nav_gas) {
            setTitle(R.string.title_fragment_gas);
            addFillupFAB.show();
            addOperationFAB.hide();
            addMemoFAB.hide();
        } else if (fragmentId == R.id.nav_repair) {
            setTitle(R.string.title_fragment_operations);
            addFillupFAB.hide();
            addOperationFAB.show();
            addMemoFAB.hide();
        } else if (fragmentId == R.id.nav_memos) {
            setTitle(R.string.title_fragment_share);
            addFillupFAB.hide();
            addOperationFAB.hide();
            addMemoFAB.show();
        } else if (fragmentId == R.id.nav_share) {
            setTitle(R.string.title_fragment_share);
            addFillupFAB.hide();
            addOperationFAB.hide();
            addMemoFAB.hide();
        } else if (fragmentId == R.id.nav_nfc) {
            setTitle(R.string.title_fragment_tags);
            addFillupFAB.hide();
            addOperationFAB.hide();
            addMemoFAB.hide();
        }
    }

    public void changeCar(Car newCar, boolean openFragment) {
        Log.d("fragment", "Change car (" + openFragment + ")");
        GlobalContext.setCurrentCar(newCar.getId());

        RelativeLayout header = (RelativeLayout) headerView.findViewById(R.id.drawer_header);
        ImageView brandImageView = (ImageView) headerView.findViewById(R.id.brand_image_view);

        if (currentCar != null){
            navigationView.getMenu().add(R.id.cars_mgmt_group, currentCar.getId(), 0, currentCar.toString());
            navigationView.getMenu().findItem(currentCar.getId()).setIcon(R.drawable.ic_car_circle);
        }
        navigationView.getMenu().removeItem(newCar.getId());

        isProfilesMenuOpen = false;

        refreshProfilesMenu();

        currentCar = newCar;
        TextView drawerTitle = (TextView) headerView.findViewById(R.id.car_name);
        drawerTitle.setText(currentCar.getCarModel().toString());

        TextView drawerDesc = (TextView) headerView.findViewById(R.id.car_desc);
        drawerDesc.setText(currentCar.getPlateNum());
        brandImageView.setImageDrawable(currentCar.getCarPicture(getBaseContext()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            header.setBackground(currentCar.getCarBanner(getBaseContext()));
            appBarImage.setImageDrawable(currentCar.getCarBanner(getBaseContext()));
        } else{
            header.setBackgroundDrawable(currentCar.getCarBanner(getBaseContext()));
            appBarImage.setImageDrawable(currentCar.getCarBanner(getBaseContext()));
        }

        LinearLayout carsLayout = (LinearLayout) headerView.findViewById(R.id.cars_icon_layout);
        carsLayout.removeViews(2, carsLayout.getChildCount() - 2);

        for(Map.Entry<Integer, Car> carEntry : cars.entrySet()) {
            Car car = carEntry.getValue();

            if (currentCar != null && car.getId() != currentCar.getId()) {
                ImageView carImage = new ImageView(getBaseContext());
                carImage.setImageDrawable(car.getCarPicture(getBaseContext()));

                int size = (int) getResources().getDimension(R.dimen.nav_header_other_car_size);
                int spacing = (int) getResources().getDimension(R.dimen.nav_header_other_car_spacing);
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
                                changeCar(associatedCar, true);
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

        if (openFragment) {
            getSupportFragmentManager().popBackStack("FIRST_FRAGMENT", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            openMainFragment(HomeFragment.newInstance(R.id.nav_home), R.id.nav_home);

        }

        closeDrawer();
    }

    public void refreshProfilesMenu() {
        navigationView.getMenu().setGroupVisible(R.id.views_group, !isProfilesMenuOpen);
        navigationView.getMenu().setGroupVisible(R.id.misc_group, !isProfilesMenuOpen);
        navigationView.getMenu().findItem(R.id.share_group_item).setVisible(!isProfilesMenuOpen);

        navigationView.getMenu().setGroupVisible(R.id.cars_mgmt_group, isProfilesMenuOpen);

        ImageView spinnerTrigger = (ImageView) headerView.findViewById(R.id.spinner_trigger);
        if (!isProfilesMenuOpen){
            spinnerTrigger.setImageResource(R.drawable.ic_arrow_drop_down_white_24dp);
        } else {
            spinnerTrigger.setImageResource(R.drawable.ic_arrow_drop_up_white_24dp);
        }
    }

    public void initNavDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, supportToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

        for (Map.Entry<Integer, Car> carEntry : cars.entrySet()) {
            navigationView.getMenu().add(R.id.cars_mgmt_group, carEntry.getValue().getId(), 0, carEntry.getValue().toString());
            navigationView.getMenu().findItem(carEntry.getValue().getId()).setIcon(R.drawable.ic_car_circle);
        }

        navigationView.getMenu().setGroupVisible(R.id.cars_mgmt_group, false);

        headerView = navigationView.getHeaderView(0);
        LinearLayout profileSpinner = (LinearLayout) headerView.findViewById(R.id.profile_spinner);
        profileSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProfilesMenuOpen = !isProfilesMenuOpen;
                refreshProfilesMenu();
            }
        });
    }

    public void setAppBarScrollEnabled(boolean enabled) {
        ViewCompat.setNestedScrollingEnabled(scrollView, enabled);
        appbar.setExpanded(enabled);
    }

    public void setTitle(String title) {
        if (appbarLayout != null)
            appbarLayout.setTitle(title);
        if (supportToolbar != null)
            supportToolbar.setTitle(title);
    }

    public void setTitle(int titleResId) {
        if (appbarLayout != null)
            appbarLayout.setTitle(getString(titleResId));
        if (supportToolbar != null)
            supportToolbar.setTitle(titleResId);
    }
}



