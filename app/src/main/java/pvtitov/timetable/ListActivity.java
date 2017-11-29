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

public class ListActivity extends AppCompatActivity implements StationsAdapter.OnItemClickListener<Station>{

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    String mRequestedToOrFrom = null;
    StationsAdapter<Station> mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mRequestedToOrFrom = intent.getStringExtra(MainActivity.EXTRA_CHOOSE_ADAPTER);
            mAdapter = getProperAdapter(mRequestedToOrFrom);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_list_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        if (mAdapter != null) mAdapter.setOnItemClickListener(ListActivity.this);

        final EditText searchEditText = (EditText) findViewById(R.id.search_edit_text);
        ImageButton searchButton = (ImageButton) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchEditText.getText().toString();
                mAdapter = filterAdapter(getProperAdapter(mRequestedToOrFrom), query);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(ListActivity.this);
            }
        });
    }

    private StationsAdapter<Station> filterAdapter(StationsAdapter<Station> adapter, String query) {
        List<Station> oldList = adapter.getDataList();
        List<Station> newList = new ArrayList<>();
        for (Station station: oldList) {
            if (station.getStation().substring(0, query.length()).equalsIgnoreCase(query)) newList.add(station);
        }
        return new StationsAdapter<>(newList);
    }

    // Загружаем список станций отправления, либо станций прибытия
    private StationsAdapter<Station> getProperAdapter(String requestedToOrFrom) {
        StationsAdapter<Station> adapter = null;
        if (requestedToOrFrom != null) {
            switch (requestedToOrFrom){
                case MainActivity.ADAPTER_FROM:
                    adapter = App.getInstance().getFromAdapter();
                    break;
                case MainActivity.ADAPTER_TO:
                    adapter = App.getInstance().getToAdapter();
                    break;
            }
        }
        return adapter;
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