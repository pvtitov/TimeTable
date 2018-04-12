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

public class ListActivity extends AppCompatActivity{

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
        mDatabaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mIsFrom = intent.getBooleanExtra(MainActivity.FROM, false);
            mIsTo = intent.getBooleanExtra(MainActivity.TO, false);
        }

        loadStations();

        final EditText searchEditText = findViewById(R.id.search_edit_text);
        ImageButton searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(view -> {
            String query = searchEditText.getText().toString();
            searchByQuery(mCities, query);
        });

        mBroadcastReceiver = new ParseBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(ParseService.ACTION_PARSE_SERVICE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void loadStations() {
        SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        String table = mIsFrom? StationsContract.Entry.TABLE_CITIES_FROM:StationsContract.Entry.TABLE_CITIES_TO;
        try (Cursor cursor = database.query(table, null, null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                Station station = new Station();
                station.setCountry(
                        cursor.getString(
                                cursor.getColumnIndex(
                                        StationsContract.Entry.COLUMN_STATION_COUNTRY
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


    private void searchByQuery(final List<City> cities, final String query) {

        //  Обновляем список
        mAdapter.updateDataset(new ArrayList<>());
        mAdapter.notifyDataSetChanged();

        if (cities != null) {

            for (City city: cities) {
                for (final Station station: city.getStations()) {
                    // Здесь происходит фильтрация станций по поисковому запросу
                    if (station.getStation().toLowerCase()
                            .contains(query.toLowerCase())) {

                        mAdapter.addStation(station);
                        mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);
                    }
                }
            }

            mRecyclerView.scrollToPosition(0);
        }
    }


    //@Override
    public void onClick(Station stationItem) {
        Intent intent = new Intent();
        intent.putExtra(STATION_EXTRA, stationItem.getStation());
        if(mIsFrom) intent.putExtra(MainActivity.FROM, mIsFrom);
        if(mIsTo) intent.putExtra(MainActivity.TO, mIsTo);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    //@Override
    public void onLongClick(Station stationItem) {

        // Формирование строки с детальной информацией о станции
        String details = stationItem.getStation()+ "\n\n" +
                stationItem.getCity();
        if (!stationItem.getRegion().equals(""))
            details = details + "\n\n" + stationItem.getRegion();
        details = details + "\n\n" + stationItem.getCountry();


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
            loadStations();
        }
    }
}