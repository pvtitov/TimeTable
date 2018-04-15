package pvtitov.timetable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;


import pvtitov.timetable.contracts.Date;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatePickerFragment.DateConsumer {

    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";
    private static final String DATE_IS_PICKED = "picked";

    public static final String FROM = "from";
    public static final String TO = "to";

    public static final int PICK_STATION_CODE = 1;


    private Date mDate;
    private Button mButtonDate;
    private boolean mDateIsPicked;

    private Button mButtonTo;
    private Button mButtonFrom;

    private String mStationFrom;
    private String mStationTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mButtonFrom = findViewById(R.id.button_from);
        mButtonFrom.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            intent.putExtra(FROM, true);
            startActivityForResult(intent, PICK_STATION_CODE);
        });


        mButtonTo = findViewById(R.id.button_to);
        mButtonTo.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            intent.putExtra(TO, true);
            startActivityForResult(intent, PICK_STATION_CODE);
        });


        mButtonDate = findViewById(R.id.button_date);
        mButtonDate.setOnClickListener(view -> {
            DialogFragment pickDateDialog = new DatePickerFragment();
            pickDateDialog.show(getSupportFragmentManager(), "date_picker");
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (mStationFrom != null) mButtonFrom.setText(mStationFrom);
        if (mStationTo != null) mButtonTo.setText(mStationTo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_STATION_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                if (data.getBooleanExtra(FROM, false)){
                    mStationFrom = data.getStringExtra(ListActivity.STATION_EXTRA);
                    mButtonFrom.setText(mStationFrom);
                }

                if (data.getBooleanExtra(TO, false)){
                    mStationTo = data.getStringExtra(ListActivity.STATION_EXTRA);
                    mButtonTo.setText(mStationTo);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_timetable:
                break;
            case R.id.nav_info:
                startActivity(new Intent(this, InfoActivity.class));
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onDatePicked(Date date) {
        mDate = date;
        mDateIsPicked = true;
        if (mButtonDate != null) {
            mButtonDate.setText(mDate.getDay() + "." + mDate.getMonth() + "." + mDate.getYear());
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mDateIsPicked) {
            outState.putInt(YEAR, mDate.getYear());
            outState.putInt(MONTH, mDate.getMonth());
            outState.putInt(DAY, mDate.getDay());
            outState.putBoolean(DATE_IS_PICKED, mDateIsPicked);
        }

        outState.putString(FROM, mStationFrom);
        outState.putString(TO, mStationTo);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.getBoolean(DATE_IS_PICKED)) {
            mDate = new Date();
            mDate.setYear(savedInstanceState.getInt(YEAR));
            mDate.setMonth(savedInstanceState.getInt(MONTH));
            mDate.setDay(savedInstanceState.getInt(DAY));
            mDateIsPicked = savedInstanceState.getBoolean(DATE_IS_PICKED);

            if (mButtonDate != null) mButtonDate.setText(mDate.getDay() + "." + mDate.getMonth() + "." + mDate.getYear());
        }

        mStationFrom = savedInstanceState.getString(FROM);
        mStationTo = savedInstanceState.getString(TO);
    }
}
