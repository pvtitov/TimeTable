package pvtitov.timetable;

import android.app.Activity;
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

    public static final String TAG_DATE_PICKER = "tag_date_picker";

    private static final String STATE_YEAR = "year";
    private static final String STATE_MONTH = "month";
    private static final String STATE_DAY = "day";
    private static final String STATE_IS_PICKED = "picked";

    private static final String STATE_STATION_FROM = "station_from";
    private static final String STATE_STATION_TO = "station_to";

    public static final String EXTRA_CHOOSE_ADAPTER = "choose_adapter";
    public static final String ADAPTER_FROM = "from";
    public static final String ADAPTER_TO = "to";

    public static final int REQUEST_CODE_STATION = 123;
    public static final String EXTRA_PASS_STATION = "pass_station";
    public static final String EXTRA_TO_OR_FROM = "to_or_from";


    private Date mDate;
    private Button mButtonDate;
    private boolean mDateIsPicked;

    private Button mButtonTo;
    private Button mButtonFrom;

    private String mStationFrom;
    private String mStationTo;

    private Intent mIntent;


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

        // Создаем единый Intent
        mIntent = new Intent(MainActivity.this, ListActivity.class);

        // Кнопка - станция отправления
        mButtonFrom = (Button) findViewById(R.id.button_from);
        mButtonFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent.putExtra(EXTRA_CHOOSE_ADAPTER, ADAPTER_FROM);
                startActivityForResult(mIntent, REQUEST_CODE_STATION);
            }
        });
        if (mStationFrom != null) mButtonFrom.setText(mStationFrom);

        // Кнопка - станция прибытия
        mButtonTo = (Button) findViewById(R.id.button_to);
        mButtonTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent.putExtra(EXTRA_CHOOSE_ADAPTER, ADAPTER_TO);
                startActivityForResult(mIntent, REQUEST_CODE_STATION);
            }
        });
        if (mStationTo != null) mButtonTo.setText(mStationTo);


        // Кнопка - выбор даты
        mButtonDate = (Button) findViewById(R.id.button_date);
        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment pickDateDialog = new DatePickerFragment();
                pickDateDialog.show(getSupportFragmentManager(), TAG_DATE_PICKER);
            }
        });
        if (mDateIsPicked && mDate != null) mButtonDate.setText(mDate.toString());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_STATION) {
            if (resultCode == Activity.RESULT_OK) {
                switch (data.getStringExtra(EXTRA_TO_OR_FROM)) {
                    case ADAPTER_FROM:
                        mStationFrom = data.getStringExtra(EXTRA_PASS_STATION);
                        mButtonFrom.setText(mStationFrom);
                        break;
                    case ADAPTER_TO:
                        mStationTo = data.getStringExtra(EXTRA_PASS_STATION);
                        mButtonTo.setText(mStationTo);
                        break;
                }
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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_STATION_FROM, mStationFrom);
        outState.putString(STATE_STATION_TO, mStationTo);
        outState.putBoolean(STATE_IS_PICKED, mDateIsPicked);
        if (mDateIsPicked) {
            outState.putInt(STATE_YEAR, mDate.getYear());
            outState.putInt(STATE_MONTH, mDate.getMonth());
            outState.putInt(STATE_DAY, mDate.getDay());
        }

        outState.putString(STATE_STATION_FROM, mStationFrom);
        outState.putString(STATE_STATION_TO, mStationTo);

        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mStationFrom = savedInstanceState.getString(STATE_STATION_FROM);
        mStationTo = savedInstanceState.getString(STATE_STATION_TO);
        mDateIsPicked = savedInstanceState.getBoolean(STATE_IS_PICKED);
        if (mDateIsPicked) {
            mDate = new Date();
            mDate.setYear(savedInstanceState.getInt(STATE_YEAR));
            mDate.setMonth(savedInstanceState.getInt(STATE_MONTH));
            mDate.setDay(savedInstanceState.getInt(STATE_DAY));
        }
    }
}
