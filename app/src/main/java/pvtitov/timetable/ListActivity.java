package pvtitov.timetable;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import pvtitov.timetable.model.City;

/**
 * Created by Павел on 12.11.2017.
 */

public class ListActivity extends AppCompatActivity implements StationsAdapter.OnItemClickListener<City>{

    private static final String EXTRA_TO_OR_FROM = "to_or_from";

    String mChooseToOrFrom = null;
    StationsAdapter<City> mAdapter;
    Intent mIntent;
    Bundle mBundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

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
        mChooseToOrFrom = mIntent.getStringExtra(MainActivity.TO_OR_FROM);
        mBundle = mIntent.getBundleExtra(MainActivity.BUNDLE);

        /*
        Проверяем у Intent поле action.
        ACTION_CHOOSER был присвоен Intent из MainActivity
         */
        if (Intent.ACTION_PICK.equals(mIntent.getAction())) {
            mAdapter = getAdapterFromApp();
        }

        /*
        Проверяем у Intent поле action.
        ACTION_SEARCH - действие Intent, посылаемого поисковым сервисом
         */
        if (Intent.ACTION_SEARCH.equals(mIntent.getAction())) {
            String query = mIntent.getStringExtra(SearchManager.QUERY);
            mChooseToOrFrom = mIntent.getStringExtra(EXTRA_TO_OR_FROM);
            mAdapter = filterAdapter(getAdapterFromApp(), query);
        }

        // Отфильтровываем Adapter на соответствие поисковому запросу


        recyclerView.setAdapter(mAdapter);
        if (mAdapter != null) {
            mAdapter.setOnItemClickListener(ListActivity.this);
        }
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
    private StationsAdapter<City> getAdapterFromApp() {
        StationsAdapter<City> adapter = null;
        if (mChooseToOrFrom != null) {
            switch (mChooseToOrFrom){
                case MainActivity.FROM:
                    adapter = App.getInstance().getFromAdapter();
                    break;
                case MainActivity.TO:
                    adapter = App.getInstance().getToAdapter();
                    break;
            }
        }
        return adapter;
    }

    @Override
    public void onClick(City cityItem) {
        Intent intent = new Intent(Intent.ACTION_ANSWER, null, ListActivity.this, MainActivity.class);
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
    }
}
