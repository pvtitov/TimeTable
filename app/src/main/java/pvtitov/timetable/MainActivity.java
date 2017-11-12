package pvtitov.timetable;

import android.content.Intent;
import android.os.Bundle;
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

    private static final String STATE_YEAR = "year";
    private static final String STATE_MONTH = "month";
    private static final String STATE_DAY = "day";
    private static final String STATE_IS_PICKED = "picked";

    public static final String EXTRA_CHOOSE_ADAPTER = "choose_adapter";
    public static final String ADAPTER_FROM = "from";
    public static final String ADAPTER_TO = "to";

    private Date mDate;
    private Button mButtonDate;
    private boolean mDateIsPicked;



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


        Button buttonFrom = (Button) findViewById(R.id.button_from);
        buttonFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra(EXTRA_CHOOSE_ADAPTER, ADAPTER_FROM);
                //TODO startActivityForResult
                startActivity(intent);
            }
        });


        Button buttonTo = (Button) findViewById(R.id.button_to);
        buttonTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra(EXTRA_CHOOSE_ADAPTER, ADAPTER_TO);
                //TODO startActivityForResult
                startActivity(intent);
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
/*
        ListView listFrom = (ListView) findViewById(R.id.from_list);
        StationsAdapter mFromAdapter = App.getInstance().getFromAdapter();
        listFrom.setAdapter(mFromAdapter);

        ListView listTo = (ListView) findViewById(R.id.to_list);
        StationsAdapter mToAdapter = App.getInstance().getToAdapter();
        listTo.setAdapter(mToAdapter);
        */
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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
            mButtonDate.setText(mDate.getDay() + "." + mDate.getMonth() + "." + mDate.getYear());
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mDateIsPicked) {
            outState.putInt(STATE_YEAR, mDate.getYear());
            outState.putInt(STATE_MONTH, mDate.getMonth());
            outState.putInt(STATE_DAY, mDate.getDay());
            outState.putBoolean(STATE_IS_PICKED, mDateIsPicked);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.getBoolean(STATE_IS_PICKED)) {
            mDate = new Date();
            mDate.setYear(savedInstanceState.getInt(STATE_YEAR));
            mDate.setMonth(savedInstanceState.getInt(STATE_MONTH));
            mDate.setDay(savedInstanceState.getInt(STATE_DAY));
            mDateIsPicked = savedInstanceState.getBoolean(STATE_IS_PICKED);

            if (mButtonDate != null) mButtonDate.setText(mDate.getDay() + "." + mDate.getMonth() + "." + mDate.getYear());
        }
    }
}
