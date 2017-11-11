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

    private CitiesArrayAdapter mFromAdapter;
    private CitiesArrayAdapter mToAdapter;

    public CitiesArrayAdapter getFromAdapter() {return mFromAdapter;}
    public CitiesArrayAdapter getToAdapter() {return mToAdapter;}

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;


        mFromAdapter = new CitiesArrayAdapter(this, new ArrayList<City>());
        mToAdapter = new CitiesArrayAdapter(this, R.layout.spinner_layout, R.id.city, new ArrayList<City>());
        ParseJson mParseJson = new ParseJson(this, mFromAdapter, mToAdapter);
        mParseJson.execute();
    }
}
