package pvtitov.timetable;

import android.app.Application;

import pvtitov.timetable.model.Model;

/**
 * Created by Павел on 30.10.2017.
 */

public class App extends Application{

    private static App singleton;

    public static App getInstance(){
        return singleton;
    }

    private Model mModel;


    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;


        mModel = new Model();
        ParseJson parseJson = new ParseJson(this, mModel);
        parseJson.execute();
    }


    public Model getModel() {return mModel;}
}
