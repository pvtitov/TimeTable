package pvtitov.timetable;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;



/**
 * Created by Павел on 29.10.2017.
 */

class CustomArrayAdapter<T> extends android.widget.ArrayAdapter {
    private List<T> mObjects;
    private Context mContext;


    CustomArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
        mObjects = objects;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return mObjects.get(position);
    }

    public void updateDataset(List<T> objects) {
        mObjects = objects;
    }
}
