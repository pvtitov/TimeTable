package pvtitov.timetable;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pvtitov.timetable.model.Station;


public class StationsAdapter extends Adapter<StationsAdapter.ViewHolder>{
    private List<Station> mStations = new ArrayList<>();
    private OnStationClickListener mListener;

    interface OnStationClickListener{
        void onClick(Station station);
        void onLongClick(Station station);
    }

    StationsAdapter(){}


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

    void setOnItemClickListener(OnStationClickListener listener){
        mListener = listener;
    }

    void updateDataset(List<Station> stations) {
        mStations = stations;
    }

    void addStation(Station station){
        mStations.add(station);
    }

    void addStations(List<Station> stations){
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

        // Здесь происходит группирование городов по странам

        if (position > 0) {
            Station prevStation = mStations.get(position - 1);
            String prevCountry = prevStation.getCountry();
            String prevCity = prevStation.getCity();

            int countryVisibility;
            int cityVisibility;

            if (country.equals(prevCountry)) countryVisibility = View.GONE;
            else countryVisibility = View.VISIBLE;

            if (city.equals(prevCity)) cityVisibility = View.GONE;
            else cityVisibility = View.VISIBLE;

            holder.mCountryTextView.setVisibility(countryVisibility);
            holder.mCityTextView.setVisibility(cityVisibility);
        }


        holder.mStationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStations != null)
                    mListener.onClick(mStations.get(holder.getAdapterPosition()));
            }
        });

        holder.mStationTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mStations != null)
                    mListener.onLongClick(mStations.get(holder.getAdapterPosition()));
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStations.size();
    }
}