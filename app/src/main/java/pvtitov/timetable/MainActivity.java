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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import pvtitov.timetable.model.Date;
import pvtitov.timetable.searchable_spinner.SearchableSpinner;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatePickerFragment.DateConsumer {

    private static final String STATE_YEAR = "year";
    private static final String STATE_MONTH = "month";
    private static final String STATE_DAY = "day";
    private static final String STATE_IS_PICKED = "picked";

    private Date mDate;
    private TextView mDateTextView;
    private boolean mDateIsPicked;
    SearchableSpinner mSpinnerFrom;


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


        // Использую стороннюю библиотеку для раскрывающегося списка с поиском с автозаполнением:
        // https://github.com/miteshpithadiya/SearchableSpinner


        SearchableSpinner spinnerFrom = (SearchableSpinner) findViewById(R.id.spinner_from);
        CitiesArrayAdapter mFromAdapter = App.getInstance().getFromAdapter();
        spinnerFrom.setAdapter(mFromAdapter);
        spinnerFrom.setTitle("Пункт отправления:");

/*
        mSpinnerFrom = new SearchableSpinner(MainActivity.this);
        final CitiesArrayAdapter fromAdapter = App.getInstance().getFromAdapter();
        mSpinnerFrom.setAdapter(fromAdapter);

        Button buttonFrom = (Button) findViewById(R.id.button_from);
        buttonFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromAdapter.getCount() != 0) mSpinnerFrom.performClick();
            }
        });
*/

        /*SearchableSpinner spinnerTo = new SearchableSpinner(this);
        CitiesArrayAdapter mToAdapter = App.getInstance().getToAdapter();
        spinnerTo.setAdapter(mToAdapter);
        spinnerTo.setTitle("Пункт прибытия:");

        mDateTextView = (TextView) findViewById(R.id.textview_date);
        mDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment pickDateDialog = new DatePickerFragment();
                pickDateDialog.show(getSupportFragmentManager(), "date_picker");
            }
        });*/
/*
        ListView listFrom = (ListView) findViewById(R.id.from_list);
        CitiesArrayAdapter mFromAdapter = App.getInstance().getFromAdapter();
        listFrom.setAdapter(mFromAdapter);

        ListView listTo = (ListView) findViewById(R.id.to_list);
        CitiesArrayAdapter mToAdapter = App.getInstance().getToAdapter();
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
        if (mDateTextView != null) {
            mDateTextView.setText(mDate.getDay() + "." + mDate.getMonth() + "." + mDate.getYear());
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

            if (mDateTextView != null) mDateTextView.setText(mDate.getDay() + "." + mDate.getMonth() + "." + mDate.getYear());
        }
    }
}
