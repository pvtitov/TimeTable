package pvtitov.timetable;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
    import java.util.List;

import pvtitov.timetable.model.City;


public class StationsAdapter extends Adapter<StationsAdapter.ViewHolder>{
    private List<City> mCities = new ArrayList<>();
    private Context mContext;

    public StationsAdapter(Context context, List<City> cities){
        mCities = cities;
        mContext = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextViewCountry;
        TextView mTextViewCity;

        ViewHolder(View itemView) {
            super(itemView);
            mTextViewCity = itemView.findViewById(R.id.city);
            mTextViewCountry = itemView.findViewById(R.id.country);
        }

    }

    public void updateDataset(List<City> cities) {
        mCities = cities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        //TODO change View parameters here
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextViewCity.setText(mCities.get(position).getCity());
        holder.mTextViewCountry.setText(mCities.get(position).getCountry());
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }
}
