package pvtitov.timetable;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

/**
 * Created by Павел on 12.11.2017.
 */

public class ListActivity extends AppCompatActivity{

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        String chooseAdapter = null;
        if (getIntent().getExtras() != null)
            chooseAdapter = getIntent().getStringExtra(MainActivity.EXTRA_CHOOSE_ADAPTER);

        switch (chooseAdapter){
            case MainActivity.ADAPTER_FROM:
                mAdapter = App.getInstance().getFromAdapter();
                break;
            case MainActivity.ADAPTER_TO:
                mAdapter = App.getInstance().getToAdapter();
                break;
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_list_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



    }
}
