package pvtitov.timetable;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
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
import pvtitov.timetable.put_aside.MainActivity;
import pvtitov.timetable.put_aside.StationDetailsFragment;
import pvtitov.timetable.put_aside.StationsAdapter;


/**
 * Created by Павел on 12.11.2017.
 */

public class ListActivity extends AppCompatActivity implements StationsAdapter.OnStationClickListener, LoaderManager.LoaderCallbacks<List<City>>{

    public static final int LOADER_ID = 1;

    Loader<List<City>> mLoader;
    String mRequestedToOrFrom;
    StationsAdapter mAdapter = new StationsAdapter();
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    List<City> mCities = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mRecyclerView = findViewById(R.id.recycler_list_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(ListActivity.this);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mRequestedToOrFrom = intent.getStringExtra(MainActivity.EXTRA_CHOOSE_ADAPTER);
        }


        final EditText searchEditText = findViewById(R.id.search_edit_text);
        ImageButton searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(view -> {
            String query = searchEditText.getText().toString();
            ListActivity.this.searchByQuery(mCities, query);
        });

        Bundle bundle = new Bundle();
        bundle.putString(ParseJson.BUNDLE_KEY, mRequestedToOrFrom);
        mLoader = getLoaderManager().initLoader(LOADER_ID, bundle, this);
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


    @Override
    public void onClick(Station stationItem) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.EXTRA_PASS_STATION, stationItem.getStation());
        intent.putExtra(MainActivity.EXTRA_TO_OR_FROM, mRequestedToOrFrom);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
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
    public Loader<List<City>> onCreateLoader(int id, Bundle args) {
        ParseJson.PickCities toOrFrom = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Objects.equals(args.getString(ParseJson.BUNDLE_KEY), MainActivity.ADAPTER_TO)) toOrFrom = ParseJson.PickCities.TO;
            if (Objects.equals(args.getString(ParseJson.BUNDLE_KEY), MainActivity.ADAPTER_FROM)) toOrFrom = ParseJson.PickCities.FROM;
        } else {
            Log.d("debugging", "onCreateLoader(): ELSE");
            return null;
        }
        Log.d("debugging", "onCreateLoader(): SUCCESS - " + toOrFrom.getValue());
        return new ParseJson(this, toOrFrom);
    }

    @Override
    public void onLoadFinished(Loader<List<City>> loader, List<City> data) {
        mCities = data;

        if (mCities != null) {
            for (City city: mCities) {
                mAdapter.addStations(city.getStations());
                mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}