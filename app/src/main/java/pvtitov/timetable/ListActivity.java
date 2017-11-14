package pvtitov.timetable;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import pvtitov.timetable.model.City;

/**
 * Created by Павел on 12.11.2017.
 */

public class ListActivity extends AppCompatActivity implements StationsAdapter.OnItemClickListener{

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    StationsAdapter mAdapter;
    String mButtonPressedToOrFrom = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if (getIntent().getExtras() != null)
            mButtonPressedToOrFrom = getIntent().getStringExtra(MainActivity.EXTRA_CHOOSE_ADAPTER);

        switch (mButtonPressedToOrFrom){
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
        mAdapter.setOnItemClickListener(ListActivity.this);
    }

    @Override
    public void onClick(City cityItem) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.EXTRA_PASS_CITY, cityItem.getCity());
        intent.putExtra(MainActivity.EXTRA_TO_OR_FROM, mButtonPressedToOrFrom);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
