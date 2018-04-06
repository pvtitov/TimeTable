package pvtitov.timetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import pvtitov.timetable.BuildConfig;
import pvtitov.timetable.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        String appInfo = getResources().getString(R.string.app_info, BuildConfig.VERSION_NAME);

        TextView textView = findViewById(R.id.textview_info);
        textView.setText(appInfo);
    }
}
