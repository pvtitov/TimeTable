package pvtitov.timetable;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class StationsAdapter<Item> extends Adapter<StationsAdapter.ViewHolder>{
    private List<Item> mItems = new ArrayList<>();
    private OnItemClickListener<Item> mListener;

    interface OnItemClickListener<Item>{
        void onClick(Item item);
    }

    public interface ItemsGroupedByHeader{
        String getSmallHeader();
        String getBigHeader();
        String getItem();
    }

    StationsAdapter(List<Item> items){
        mItems = items;
    }

    List<Item> getDataList(){return mItems;}

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mHeaderBig;
        TextView mHeaderSmall;
        TextView mItem;

        ViewHolder(View itemView) {
            super(itemView);
            mHeaderSmall = itemView.findViewById(R.id.city);
            mHeaderBig = itemView.findViewById(R.id.country);
            mItem = itemView.findViewById(R.id.station);
        }

    }

    public void setOnItemClickListener(OnItemClickListener<Item> listener){
        mListener = listener;
    }

    public void updateDataset(List<Item> items) {
        mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        //TODO change View parameters here
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        try{
            ItemsGroupedByHeader item = (ItemsGroupedByHeader) mItems.get(position);
            holder.mHeaderBig.setText(item.getBigHeader());
            holder.mHeaderSmall.setText(item.getSmallHeader());
            holder.mItem.setText(item.getItem());
            /*
            Здесь происходит группирование городов по странам
            */
            holder.mHeaderBig.setVisibility(View.GONE);
            holder.mHeaderSmall.setVisibility(View.GONE);
            if (position == 0) {
                holder.mHeaderBig.setVisibility(View.VISIBLE);
                holder.mHeaderSmall.setVisibility(View.VISIBLE);
            }
            if (position > 0) {
                ItemsGroupedByHeader previousItem = (ItemsGroupedByHeader) mItems.get(position - 1);
                if (!(item.getBigHeader().equals(previousItem.getBigHeader()))){
                    holder.mHeaderBig.setVisibility(View.VISIBLE);
                }
                if (!(item.getSmallHeader().equals(previousItem.getSmallHeader()))){
                    holder.mHeaderSmall.setVisibility(View.VISIBLE);
                }
            }
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }


        holder.mHeaderSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(mItems.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}