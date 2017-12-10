package pvtitov.timetable;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import pvtitov.timetable.model.City;
import pvtitov.timetable.model.Station;


/**
 * Created by Павел on 12.11.2017.
 */

public class ListActivity extends AppCompatActivity implements StationsAdapter.OnItemClickListener<Station>, ParseJson.OnParseListener{

    String mRequestedToOrFrom;
    StationsAdapter<Station> mAdapter = new StationsAdapter<>();
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    List<City> mCities = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        /*
        Получаем ссылку на RecyclerView
         */
        mRecyclerView = findViewById(R.id.recycler_list_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(ListActivity.this);

        /*
        Получаем из интента, вызвавшего данную активность,
        информацию о том, какой список подгрузить.
        И, в зависимости от
         */
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mRequestedToOrFrom = intent.getStringExtra(MainActivity.EXTRA_CHOOSE_ADAPTER);
        }

        setupAdapter(mRequestedToOrFrom);

        // Зарегистрировать данную активность на событие: завершение работы парсера
        ParseJson.setOnParseListener(this);

        final EditText searchEditText = findViewById(R.id.search_edit_text);
        ImageButton searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchEditText.getText().toString();
                searchByQuery(mCities, query);
            }
        });
    }

    private void setupAdapter(String requestedToOrFrom) {
        switch (requestedToOrFrom){
            case MainActivity.ADAPTER_FROM:
                if (App.getInstance().getModel().getCitiesFrom() != null)
                    mCities.addAll(App.getInstance().getModel().getCitiesFrom());
                break;
            case MainActivity.ADAPTER_TO:
                if (App.getInstance().getModel().getCitiesTo() != null)
                    mCities.addAll(App.getInstance().getModel().getCitiesTo());
                break;
        }

        if (mCities != null) {
            for (City city: mCities) {
                mAdapter.addItems(city.getStations());
                mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);
            }
        }
    }

    private void searchByQuery(final List<City> cities, final String query) {

        //  Обновляем список
        mAdapter.updateDataset(new ArrayList<Station>());
        mAdapter.notifyDataSetChanged();

        if (cities != null) {

            for (City city: cities) {
                for (final Station station: city.getStations()) {
                    // Здесь происходит фильтрация станций по поисковому запросу
                    if (station.getStation().toLowerCase()
                            .contains(query.toLowerCase())) {

                        mAdapter.addItem(station);
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
    public void onParseComplete() {
        setupAdapter(mRequestedToOrFrom);
    }
}