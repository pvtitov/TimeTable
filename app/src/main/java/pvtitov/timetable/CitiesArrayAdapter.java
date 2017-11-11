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


public class CitiesArrayAdapter extends ArrayAdapter {
    private List<City> mCities = new ArrayList<>();
    private Context mContext;

    public CitiesArrayAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<City> objects) {
        super(context, resource, textViewResourceId, objects);
        mCities = objects;
        mContext = context;
    }

    public CitiesArrayAdapter(Context context, List<City> objects) {
        super(context, 0, objects);
        mCities = objects;
        mContext = context;
    }


    @Override
    public int getCount() {
        return mCities.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        Object item = null;
        if (mCities.get(position) != null) item = mCities.get(position);
        return item;
    }

    public void updateDataset(List<City> objects) {
        mCities = objects;
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View itemView = convertView;

        if (itemView == null)
            itemView = LayoutInflater.from(mContext).inflate(R.layout.spinner_layout, parent, false);

        TextView countryTextView = itemView.findViewById(R.id.country);
        countryTextView.setText(mCities.get(position).getCountry());

        TextView cityTextView = itemView.findViewById(R.id.city);
        cityTextView.setText(mCities.get(position).getCity());

        return itemView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;

        if (itemView == null)
            itemView = LayoutInflater.from(mContext).inflate(R.layout.spinner_layout, parent, false);

        TextView cityTextView = itemView.findViewById(R.id.city);
        cityTextView.setText(mCities.get(position).getCity());

        return itemView;
    }
}
