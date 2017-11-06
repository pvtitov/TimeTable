package pvtitov.timetable;

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
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;


import pvtitov.timetable.model.City;
import pvtitov.timetable.model.Date;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatePickerFragment.DateConsumer {

    private static final String STATE_YEAR = "year";
    private static final String STATE_MONTH = "month";
    private static final String STATE_DAY = "day";
    private static final String STATE_IS_PICKED = "picked";

    private Date mDate;
    private TextView mDateTextView;
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


        // Использую стороннюю библиотеку для раскрывающегося списка с поиском с автозаполнением:
        // https://github.com/miteshpithadiya/SearchableSpinner

        SearchableSpinner spinnerFrom = (SearchableSpinner) findViewById(R.id.spinner_from);
        spinnerFrom.setTitle("From:");
        CustomArrayAdapter<City> mFromAdapter = App.getInstance().getFromAdapter();
        spinnerFrom.setAdapter(mFromAdapter);

        SearchableSpinner spinnerTo = (SearchableSpinner) findViewById(R.id.spinner_to);
        spinnerTo.setTitle("To:");
        CustomArrayAdapter<City> mToAdapter = App.getInstance().getToAdapter();
        spinnerTo.setAdapter(mToAdapter);

        mDateTextView = (TextView) findViewById(R.id.textview_date);
        mDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment pickDateDialog = new DatePickerFragment();
                pickDateDialog.show(getSupportFragmentManager(), "date_picker");
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_timetable:
                Toast.makeText(this, "Расписание", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_info:
                Toast.makeText(this, "Инфо", Toast.LENGTH_SHORT).show();
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
