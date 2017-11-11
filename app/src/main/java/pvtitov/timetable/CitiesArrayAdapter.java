package pvtitov.timetable;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pvtitov.timetable.model.City;


class CitiesArrayAdapter extends ArrayAdapter {
    private List<City> mCities = new ArrayList<>();
    private Context mContext;

    CitiesArrayAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<City> objects) {
        super(context, resource, textViewResourceId, objects);
        mCities = objects;
        mContext = context;
    }

    CitiesArrayAdapter(Context context, List<City> objects) {
        super(context, 0, objects);
        mCities = objects;
        mContext = context;
    }


    @Override
    public int getCount() {
        return mCities.size();
    }

    public void updateDataset(List<City> objects) {
        mCities = objects;
        Log.d("happy", "getView() - Австрия: " + mCities.get(0).getCountry());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View itemView = convertView;

        if (itemView == null)
            itemView = LayoutInflater.from(mContext).inflate(R.layout.spinner_layout, parent, false);

        TextView countryTextView = itemView.findViewById(R.id.country);
        countryTextView.setText(mCities.get(position).getCountry());

        TextView cityTextView = itemView.findViewById(R.id.city);
        cityTextView.setText(mCities.get(position).getCity());

        return itemView;
    }
}
