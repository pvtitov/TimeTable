package pvtitov.timetable;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pvtitov.timetable.contracts.Station;


public class StationsAdapter extends Adapter<StationsAdapter.ViewHolder>{

    public StationsAdapter(){}


    private List<Station> mStations = new ArrayList<>();
    private StationsInteractCallback mCallbackActivity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mCountryTextView;
        TextView mCityTextView;
        TextView mStationTextView;

        ViewHolder(View itemView) {
            super(itemView);
            mCityTextView = itemView.findViewById(R.id.city);
            mCountryTextView = itemView.findViewById(R.id.country);
            mStationTextView = itemView.findViewById(R.id.station);
        }

    }

    public interface StationsInteractCallback{
        void pickStation(Station s);
        void showStationDetails(Station s);
    }

    public void setCallback(StationsInteractCallback callback) {mCallbackActivity = callback;}

    public void updateDataset(List<Station> stations) {
        mStations = stations;
    }

    public void addStation(Station station){
        mStations.add(station);
    }

    public void addStations(List<Station> stations){
        mStations.addAll(stations);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Station station = mStations.get(position);
        String country = station.getCountry();
        String city = station.getCity();

        holder.mCountryTextView.setText(country);
        holder.mCityTextView.setText(city);
        holder.mStationTextView.setText(station.getStation());

        int countryVisibility = View.VISIBLE;
        int cityVisibility = View.VISIBLE;

        if (position > 0) {
            Station prevStation = mStations.get(position - 1);
            String prevCountry = prevStation.getCountry();
            String prevCity = prevStation.getCity();

            if (country.equals(prevCountry)) countryVisibility = View.GONE;
            if (city.equals(prevCity)) cityVisibility = View.GONE;
        }

        holder.mCountryTextView.setVisibility(countryVisibility);
        holder.mCityTextView.setVisibility(cityVisibility);


        holder.mStationTextView.setOnClickListener(view -> mCallbackActivity.pickStation(station));

        holder.mStationTextView.setOnLongClickListener(v -> {
            mCallbackActivity.showStationDetails(station);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mStations.size();
    }
}