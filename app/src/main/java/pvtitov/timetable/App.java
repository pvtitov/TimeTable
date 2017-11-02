package pvtitov.timetable;

import android.app.Application;

import java.util.ArrayList;

import pvtitov.timetable.model.City;

/**
 * Created by Павел on 30.10.2017.
 */

public class App extends Application {

    private static App singleton;

    public static App getInstance(){
        return singleton;
    }

    private CustomArrayAdapter<City> mFromAdapter;
    private CustomArrayAdapter<City> mToAdapter;

    public CustomArrayAdapter<City> getFromAdapter() {return mFromAdapter;}
    public CustomArrayAdapter<City> getToAdapter() {return mToAdapter;}

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        mFromAdapter = new CustomArrayAdapter<>(this, R.layout.spinner_item, R.id.spinner_item, new ArrayList<City>());
        mToAdapter = new CustomArrayAdapter<>(this, R.layout.spinner_item, R.id.spinner_item, new ArrayList<City>());
        ParseJson mParseJson = new ParseJson(this, mFromAdapter, mToAdapter);
        mParseJson.execute();
    }
}
