package pvtitov.timetable;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import pvtitov.timetable.model.City;
import pvtitov.timetable.model.Model;
import pvtitov.timetable.model.Station;


/**
 * Created by Павел on 12.11.2017.
 */

public class ListActivity extends AppCompatActivity implements StationsAdapter.OnItemClickListener<Station>{

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    String mRequestedToOrFrom;
    StationsAdapter<Station> mAdapter;
    List<City> mCities;
    List<Station> mStations = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        /*
        Получаем ссылку на RecyclerView
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_list_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new StationsAdapter<>();
        mRecyclerView.setAdapter(mAdapter);
        if (mAdapter != null) mAdapter.setOnItemClickListener(ListActivity.this);

        /*
        Получаем из вызвавшего интента информацию о том, какой список подгрузить
         */
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mRequestedToOrFrom = intent.getStringExtra(MainActivity.EXTRA_CHOOSE_ADAPTER);
        }

        Model dataStorage = App.getInstance().getModel();
        switch (mRequestedToOrFrom){
            case MainActivity.ADAPTER_FROM:
                mCities = dataStorage.getCitiesFrom();
                setupAdapter(mCities);
                break;
            case MainActivity.ADAPTER_TO:
                mCities = dataStorage.getCitiesTo();
                setupAdapter(mCities);
                break;
        }

        final EditText searchEditText = (EditText) findViewById(R.id.search_edit_text);
        ImageButton searchButton = (ImageButton) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchEditText.getText().toString();

                searchByQuery(mStations, query);
            }
        });
    }

    private void setupAdapter(List<City> cities) {
        if (cities != null) {
            for (int i = 0; i < cities.size(); i++) {
                List<Station> stationsAdded = cities.get(i).getStations();
                mStations.addAll(stationsAdded);
            }
            mAdapter.updateDataset(mStations);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void searchByQuery(final List<Station> oldList, final String query) {

        final Handler handler = new Handler();

        final List<Station> newList = new ArrayList<>();
        mAdapter.updateDataset(newList);
        mAdapter.notifyDataSetChanged();

        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {

                for (final Station station: oldList) {
                    if (station.getStation().toLowerCase()
                            .contains(query.toLowerCase())) {

                        newList.add(station);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.addItem(station);
                                mAdapter.notifyItemChanged(newList.size() - 1);
                            }
                        });
                    }
                }
            }
        });

        backgroundThread.start();
    }


    @Override
    public void onClick(Station stationItem) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.EXTRA_PASS_STATION, stationItem.getStation());
        intent.putExtra(MainActivity.EXTRA_TO_OR_FROM, mRequestedToOrFrom);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}