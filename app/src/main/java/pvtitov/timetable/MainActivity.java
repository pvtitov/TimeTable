package pvtitov.timetable;

import android.content.Intent;
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
import android.view.View;
import android.widget.Button;


import pvtitov.timetable.model.Date;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatePickerFragment.DateConsumer {

    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY = "day";
    public static final String DATE_PICKED = "date_picked";
    public static final String TO_OR_FROM = "to_or_from";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String CITY = "city";
    public static final String BUNDLE = "bundle";


    private Date mDate;
    private Button mButtonDate;
    private boolean mDateIsPicked;

    private String mCityFrom;
    private String mCityTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Intent queryIntent = new Intent(Intent.ACTION_PICK, null, MainActivity.this, ListActivity.class);

        Button buttonFrom = (Button) findViewById(R.id.button_from);
        buttonFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryIntent.putExtra(TO_OR_FROM, FROM);
                startActivity(queryIntent);
            }
        });


        Button buttonTo = (Button) findViewById(R.id.button_to);
        buttonTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryIntent.putExtra(TO_OR_FROM, TO);
                startActivity(queryIntent);
            }
        });


        mButtonDate = (Button) findViewById(R.id.button_date);
        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment pickDateDialog = new DatePickerFragment();
                pickDateDialog.show(getSupportFragmentManager(), "date_picker");
            }
        });

        stateFromIntent();
        if (mCityFrom != null) buttonFrom.setText(mCityFrom);
        if (mCityTo != null) buttonTo.setText(mCityTo);
        if ((mDateIsPicked) && (mDate != null)) mButtonDate.setText(mDate.toString());

        /*
        После всего укладываем состояние MainActivity в Bundle
        и передаем Intent.
        Bundle должен вернуться с Intent, который запустит MainActivity.
         */
        Bundle bundle = new Bundle();
        if (mDateIsPicked) {
            bundle.putInt(YEAR, mDate.getYear());
            bundle.putInt(MONTH, mDate.getMonth());
            bundle.putInt(DAY, mDate.getDay());
            bundle.putBoolean(DATE_PICKED, mDateIsPicked);
        }

        bundle.putString(FROM, mCityFrom);
        bundle.putString(TO, mCityTo);

        queryIntent.putExtra(BUNDLE, bundle);
    }

    /*@Override
    protected void onResume() {
        super.onResume();

        if (mCityFrom != null) buttonFrom.setText(mCityFrom);
        if (mCityTo != null) buttonTo.setText(mCityTo);
    }*/


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onDatePicked(Date date) {
        mDate = date;
        mDateIsPicked = true;
        if (mButtonDate != null) {
            mButtonDate.setText(mDate.toString());
        }
    }


    //TODO сохраняет города Из и В попеременно, взаимноисключающе

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mDateIsPicked) {
            outState.putInt(YEAR, mDate.getYear());
            outState.putInt(MONTH, mDate.getMonth());
            outState.putInt(DAY, mDate.getDay());
            outState.putBoolean(DATE_PICKED, mDateIsPicked);
        }

        outState.putString(FROM, mCityFrom);
        outState.putString(TO, mCityTo);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        stateFromIntent();

        if (savedInstanceState.getBoolean(DATE_PICKED)) {
            mDate = new Date();
            mDate.setYear(savedInstanceState.getInt(YEAR));
            mDate.setMonth(savedInstanceState.getInt(MONTH));
            mDate.setDay(savedInstanceState.getInt(DAY));
            mDateIsPicked = savedInstanceState.getBoolean(DATE_PICKED);
        }

        mCityFrom = savedInstanceState.getString(FROM);
        mCityTo = savedInstanceState.getString(TO);
    }

    private void stateFromIntent() {
        Intent intent = getIntent();
        if (Intent.ACTION_ANSWER.equals(intent.getAction())) {
            Bundle bundle = intent.getBundleExtra(BUNDLE);
            if (bundle.getBoolean(DATE_PICKED)) {
                mDate.setDay(bundle.getInt(DAY));
                mDate.setMonth(bundle.getInt(MONTH));
                mDate.setYear(bundle.getInt(YEAR));
            }
            mCityFrom = bundle.getString(FROM);
            mCityTo = bundle.getString(TO);
            switch (intent.getStringExtra(TO_OR_FROM)) {
                case FROM:
                    mCityFrom = intent.getStringExtra(CITY);
                    break;
                case TO:
                    mCityTo = intent.getStringExtra(CITY);
                    break;
            }
        }
    }
}
