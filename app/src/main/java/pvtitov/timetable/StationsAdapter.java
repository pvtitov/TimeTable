package pvtitov.timetable;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
    import java.util.List;

import pvtitov.timetable.model.City;


public class StationsAdapter extends Adapter<StationsAdapter.ViewHolder>{
    private List<City> mCities = new ArrayList<>();
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onClick(City cityItem);
    }

    public StationsAdapter(List<City> cities){
        mCities = cities;
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

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public void updateDataset(List<City> cities) {
        mCities = cities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        //TODO change View parameters here
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mTextViewCity.setText(mCities.get(position).getCity());
        holder.mTextViewCountry.setText(mCities.get(position).getCountry());
        holder.mTextViewCountry.setVisibility(View.GONE);
        if (position == 0) {
            holder.mTextViewCountry.setVisibility(View.VISIBLE);
        }
        if (position > 0) {
            if (!(mCities.get(position).getCountry()
                    .equals(mCities.get(position-1).getCountry()))){
                holder.mTextViewCountry.setVisibility(View.VISIBLE);
            }
        }
        holder.mTextViewCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(mCities.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }
}
