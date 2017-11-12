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

    private StationsAdapter mFromAdapter;
    private StationsAdapter mToAdapter;

    public StationsAdapter getFromAdapter() {return mFromAdapter;}
    public StationsAdapter getToAdapter() {return mToAdapter;}

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;


        mFromAdapter = new StationsAdapter(this, new ArrayList<City>());
        mToAdapter = new StationsAdapter(this, new ArrayList<City>());
        ParseJson mParseJson = new ParseJson(this, mFromAdapter, mToAdapter);
        mParseJson.execute();
    }
}
