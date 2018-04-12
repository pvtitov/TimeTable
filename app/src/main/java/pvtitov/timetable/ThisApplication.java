package pvtitov.timetable;

import android.app.Application;
import android.content.Intent;

public class ThisApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Intent intent = new Intent(getApplicationContext(), ParseService.class);
        startService(intent);
    }
}
