package me.alpha12.ecarnet.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.nfc.NfcAdapter;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import me.alpha12.ecarnet.GlobalContext;
import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.fragments.GasFragment;
import me.alpha12.ecarnet.fragments.HomeFragment;
import me.alpha12.ecarnet.fragments.OperationsFragment;
import me.alpha12.ecarnet.fragments.ReminderFragment;
import me.alpha12.ecarnet.fragments.ShareFragment;
import me.alpha12.ecarnet.fragments.TagsFragment;
import me.alpha12.ecarnet.interfaces.OnFragmentInteractionListener;
import me.alpha12.ecarnet.models.Car;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {
    public static final String FRAGMENT_MENU_ENTRY_ID = "fmei";
    private static final int CARS_MGMT_INTENT = 1;

    private TextView subTitle;

    private AppBarLayout appbar;
    private CollapsingToolbarLayout appbarLayout;
    private NestedScrollView scrollView;
    private Toolbar supportToolbar;

    private NavigationView navigationView;
    private View headerView;

    private HashMap<Integer, FloatingActionButton> fabs = new HashMap<>();

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

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        cars = Car.findAllHashMap();

        appBarImage = (ImageView) findViewById(R.id.appBarImage);
        subTitle = (TextView) findViewById(R.id.subTitle);

        initNavDrawer();

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                            onBackPressed();
                        } else {
                            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("CURRENT_FRAGMENT");
                            if (navigationView != null && currentFragment != null && currentFragment.isVisible()) {
                                int fragmentId = currentFragment.getArguments().getInt(FRAGMENT_MENU_ENTRY_ID);
                                navigationView.getMenu().findItem(fragmentId).setChecked(true);
                                setAppBarScrollEnabled(fragmentId == R.id.nav_home);
                                refreshFABs(fragmentId);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CARS_MGMT_INTENT) {
            if (resultCode == CarsMgmtActivity.CARS_DELETED_RESULT){
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                finish();
            } else {
                isProfilesMenuOpen = false;
                refreshProfilesMenu();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("CURRENT_FRAGMENT");
            if (currentFragment != null) {
                int fragmentId = currentFragment.getArguments().getInt(FRAGMENT_MENU_ENTRY_ID);
                navigationView.getMenu().findItem(fragmentId).setChecked(false);
            }

            super.onBackPressed();
        }
    }

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
                openMainFragment(ReminderFragment.newInstance(menuItemId), menuItemId);
                break;
            case R.id.nav_add_car:{
                Intent intent = new Intent(this, AddCarActivity.class);
                intent.putExtra(AddCarActivity.FROM_MAIN_ACTIVITY, true);
                startActivityForResult(intent, 0);
                break;
            }
            case R.id.nav_manage_car:
                Intent intent = new Intent(this, CarsMgmtActivity.class);
                startActivityForResult(intent, CARS_MGMT_INTENT);
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

            navigationView.getMenu().findItem(fragmentId).setChecked(true);
            setAppBarScrollEnabled(fragmentId == R.id.nav_home);
            refreshFABs(fragmentId);
        }
    }

    public void registerFloatingActionButton(int fragmentId, FloatingActionButton fab){
        fabs.put(fragmentId, fab);
    }

    private void refreshFABs(int fragmentId) {
        FloatingActionButton fabToShow = fabs.get(fragmentId);

        for(Map.Entry<Integer, FloatingActionButton> entry : fabs.entrySet()) {
            if (entry.getValue() != fabToShow)
                entry.getValue().hide();
        }

        if (fabToShow != null)
            fabToShow.show();
    }

    public void changeCar(Car newCar, boolean openFragment) {
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
        drawerTitle.setText(currentCar.getModelString());

        TextView drawerDesc = (TextView) headerView.findViewById(R.id.car_desc);
        drawerDesc.setText(currentCar.getStringPlateNum());
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
        navigationView.getMenu().setGroupVisible(R.id.cars_mgmt_group_actions, isProfilesMenuOpen);

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            PackageManager pm = getPackageManager();
            if(pm.hasSystemFeature(PackageManager.FEATURE_NFC) && NfcAdapter.getDefaultAdapter(this) != null) {
                navigationView.getMenu().findItem(R.id.nav_nfc).setEnabled(true);
            }
        }

        for (Map.Entry<Integer, Car> carEntry : cars.entrySet()) {
            navigationView.getMenu().add(R.id.cars_mgmt_group, carEntry.getValue().getId(), 0, carEntry.getValue().toString());
            navigationView.getMenu().findItem(carEntry.getValue().getId()).setIcon(R.drawable.ic_car_circle);
        }

        navigationView.getMenu().setGroupVisible(R.id.cars_mgmt_group, false);
        navigationView.getMenu().setGroupVisible(R.id.cars_mgmt_group_actions, false);

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

    public void setSubTitle(String subTitle) {
        if (this.subTitle != null) {
            this.subTitle.setText(subTitle);
        }
    }
}



