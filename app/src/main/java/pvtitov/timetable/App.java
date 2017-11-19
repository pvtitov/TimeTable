package pvtitov.timetable;

import android.app.Application;

import java.util.ArrayList;

import pvtitov.timetable.model.City;

/**
 * Created by Павел on 30.10.2017.
 */

public class App extends Application{

    private static App singleton;

    public static App getInstance(){
        return singleton;
    }

    private StationsAdapter<City> mFromAdapter;
    private StationsAdapter<City> mToAdapter;

    public StationsAdapter<City> getFromAdapter() {return mFromAdapter;}
    public StationsAdapter<City> getToAdapter() {return mToAdapter;}

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;


        mFromAdapter = new StationsAdapter<>(new ArrayList<City>());
        mToAdapter = new StationsAdapter<>(new ArrayList<City>());
        ParseJson mParseJson = new ParseJson(this, mFromAdapter, mToAdapter);
        mParseJson.execute();
    }
}
