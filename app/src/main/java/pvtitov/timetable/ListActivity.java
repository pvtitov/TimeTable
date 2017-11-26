package pvtitov.timetable;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
<<<<<<< HEAD
import android.support.v7.widget.SearchView;
=======
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
>>>>>>> develop2

import java.util.ArrayList;
import java.util.List;

import pvtitov.timetable.model.City;

/**
 * Created by Павел on 12.11.2017.
 */

public class ListActivity extends AppCompatActivity implements StationsAdapter.OnItemClickListener<City>{

<<<<<<< HEAD
    String mChooseToOrFrom;
    StationsAdapter<City> mAdapter;
    Intent mIntent;
    Bundle mBundle;
=======
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    String mRequestedToOrFrom = null;
    StationsAdapter<City> mAdapter;

>>>>>>> develop2

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

<<<<<<< HEAD
        /*
        Поиск
         */
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);

        mAdapter = new StationsAdapter<>(null);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_list_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Ссылка на любой (из двух) Intent, запустивший ListActivity
        mIntent = getIntent();
        //Получаем данные, передаваемые с любым Intent


        /*
        Проверяем у Intent поле action.
        ACTION_SEARCH - действие Intent, посылаемого поисковым сервисом
         */
        // Intent из MainActivity
        if (! Intent.ACTION_SEARCH.equals(mIntent.getAction())) {
            mChooseToOrFrom = mIntent.getStringExtra(MainActivity.TO_OR_FROM);
            mBundle = mIntent.getBundleExtra(MainActivity.BUNDLE);
            mAdapter = getAdapterFromApp();
        }

        // Intent-поиск
        if (Intent.ACTION_SEARCH.equals(mIntent.getAction())) {
            mChooseToOrFrom = mIntent.getStringExtra(MainActivity.TO_OR_FROM);
            mBundle = mIntent.getBundleExtra(MainActivity.BUNDLE);
            String query = mIntent.getStringExtra(SearchManager.QUERY);
            mAdapter = filterAdapter(getAdapterFromApp(), query);
        }

        // Отфильтровываем Adapter на соответствие поисковому запросу


        recyclerView.setAdapter(mAdapter);
        if (mAdapter != null) {
            mAdapter.setOnItemClickListener(ListActivity.this);
        }
=======
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
                mAdapter = filterAdapter(mAdapter, query);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(ListActivity.this);
            }
        });
>>>>>>> develop2
    }

    private StationsAdapter<City> filterAdapter(StationsAdapter<City> adapter, String query) {
        List<City> oldList = adapter.getDataList();
        List<City> newList = new ArrayList<>();
        for (City city: oldList) {
            if (city.getCity().substring(0, query.length()).equalsIgnoreCase(query)) newList.add(city);
        }
        return new StationsAdapter<>(newList);
    }

    // Загружаем список станций отправления, либо станций прибытия
<<<<<<< HEAD
    private StationsAdapter<City> getAdapterFromApp() {
        StationsAdapter<City> adapter = null;
        if (mChooseToOrFrom != null) {
            switch (mChooseToOrFrom){
                case MainActivity.FROM:
                    adapter = App.getInstance().getFromAdapter();
                    break;
                case MainActivity.TO:
=======
    private StationsAdapter<City> getProperAdapter(String requestedToOrFrom) {
        StationsAdapter<City> adapter = null;
        if (requestedToOrFrom != null) {
            switch (requestedToOrFrom){
                case MainActivity.ADAPTER_FROM:
                    adapter = App.getInstance().getFromAdapter();
                    break;
                case MainActivity.ADAPTER_TO:
>>>>>>> develop2
                    adapter = App.getInstance().getToAdapter();
                    break;
            }
        }
        return adapter;
    }

    @Override
    public void onClick(City cityItem) {
<<<<<<< HEAD
        Intent intent = new Intent(ListActivity.this, MainActivity.class);
        intent.putExtra(MainActivity.CITY, cityItem.getCity());
        intent.putExtra(MainActivity.TO_OR_FROM, mChooseToOrFrom);
        intent.putExtra(MainActivity.BUNDLE, mBundle);
        startActivity(intent);
    }


    @Override
    public void startActivity(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            intent.putExtra(MainActivity.TO_OR_FROM, mChooseToOrFrom);
            intent.putExtra(MainActivity.BUNDLE, mBundle);
        }

        super.startActivity(intent);
=======
        Intent intent = new Intent();
        intent.putExtra(MainActivity.EXTRA_PASS_CITY, cityItem.getCity());
        intent.putExtra(MainActivity.EXTRA_TO_OR_FROM, mRequestedToOrFrom);
        setResult(Activity.RESULT_OK, intent);
        finish();
>>>>>>> develop2
    }
}
