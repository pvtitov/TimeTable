package pvtitov.timetable;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pvtitov.timetable.contracts.City;
import pvtitov.timetable.contracts.Station;
import pvtitov.timetable.database.DatabaseHelper;
import pvtitov.timetable.database.StationsContract;


/**
 * Created by Павел on 12.11.2017.
 */

public class ListActivity extends AppCompatActivity implements StationsAdapter.StationsInteractCallback {

    public static final String STATION_EXTRA = "station";

    private boolean mIsTo = false;
    private boolean mIsFrom = false;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private DatabaseHelper mDatabaseHelper;

    // TODO проверить на дублирование
    private List<City> mCities = new ArrayList<>();
    private StationsAdapter mAdapter = new StationsAdapter();
    private BroadcastReceiver mBroadcastReceiver;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mRecyclerView = findViewById(R.id.recycler_list_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setCallback(this);
        mDatabaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mIsFrom = intent.getBooleanExtra(MainActivity.FROM, false);
            mIsTo = intent.getBooleanExtra(MainActivity.TO, false);
        }

        loadStations(null, null);

        final EditText searchEditText = findViewById(R.id.search_edit_text);
        ImageButton searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(view -> {

            mAdapter.updateDataset(new ArrayList<>());
            mAdapter.notifyDataSetChanged();

            String selection = StationsContract.Entry.COLUMN_STATION_TITLE + " LIKE ?";
            String[] selectionArgs = {"%" + searchEditText.getText().toString() + "%"};
            loadStations(selection, selectionArgs);

        });

        mBroadcastReceiver = new ParseBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(ParseService.ACTION_PARSE_SERVICE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void loadStations(String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        String table = mIsFrom ? StationsContract.Entry.TABLE_CITIES_FROM : StationsContract.Entry.TABLE_CITIES_TO;
        try (Cursor cursor = database.query(table, null, selection, selectionArgs, null, null, null)) {
            while (cursor.moveToNext()) {
                Station station = new Station();
                station.setCountry(
                        cursor.getString(
                                cursor.getColumnIndex(
                                        StationsContract.Entry.COLUMN_STATION_COUNTRY
                                )));
                station.setRegion(
                        cursor.getString(
                                cursor.getColumnIndex(
                                        StationsContract.Entry.COLUMN_STATION_REGION
                                )));
                station.setCity(
                        cursor.getString(
                                cursor.getColumnIndex(
                                        StationsContract.Entry.COLUMN_STATION_CITY_TITLE
                                )));
                station.setStation(
                        cursor.getString(
                                cursor.getColumnIndex(
                                        StationsContract.Entry.COLUMN_STATION_TITLE
                                )));
                mAdapter.addStation(station);
                mAdapter.notifyItemInserted(cursor.getPosition());
            }
        }
    }


    @Override
    public void pickStation(Station s) {
        Intent intent = new Intent();
        intent.putExtra(STATION_EXTRA, s.getStation());
        if(mIsFrom) intent.putExtra(MainActivity.FROM, mIsFrom);
        if(mIsTo) intent.putExtra(MainActivity.TO, mIsTo);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void showStationDetails(Station s) {
        String details = s.getStation()+ "\n\n" +
                s.getCity();
        if (!s.getRegion().equals(""))
            details = details + "\n\n" + s.getRegion();
        details = details + "\n\n" + s.getCountry();


        StationDetailsFragment stationDetailsFragment = new StationDetailsFragment();
        stationDetailsFragment.setDetails(details);
        stationDetailsFragment.show(getSupportFragmentManager(), "stations_details");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mBroadcastReceiver);
    }

    public class ParseBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            loadStations(null, null);
        }
    }
}