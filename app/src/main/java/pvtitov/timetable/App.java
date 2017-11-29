package pvtitov.timetable;

import android.app.Application;

import java.util.ArrayList;


import pvtitov.timetable.model.Station;

/**
 * Created by Павел on 30.10.2017.
 */

public class App extends Application{

    private static App singleton;

    public static App getInstance(){
        return singleton;
    }

    private StationsAdapter<Station> mFromAdapter;
    private StationsAdapter<Station> mToAdapter;

    public StationsAdapter<Station> getFromAdapter() {return mFromAdapter;}
    public StationsAdapter<Station> getToAdapter() {return mToAdapter;}

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;


        mFromAdapter = new StationsAdapter<>(new ArrayList<Station>());
        mToAdapter = new StationsAdapter<>(new ArrayList<Station>());
        ParseJson mParseJson = new ParseJson(this, mFromAdapter, mToAdapter);
        mParseJson.execute();
    }
}
