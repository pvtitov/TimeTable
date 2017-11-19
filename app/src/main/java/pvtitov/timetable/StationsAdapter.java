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
    private String mQuery;

    interface OnItemClickListener<Item>{
        void onClick(Item item);
    }

    public interface ItemsGroupedByHeader{
        String getItem();
        String getHeader();
    }

    StationsAdapter(List<Item> items){
        mItems = items;
    }


    List<Item> getDataList(){return mItems;}


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mHeaderTextViewCountry;
        TextView mTextViewCity;

        ViewHolder(View itemView) {
            super(itemView);
            mTextViewCity = itemView.findViewById(R.id.city);
            mHeaderTextViewCountry = itemView.findViewById(R.id.country);
        }

    }

    void setOnItemClickListener(OnItemClickListener<Item> listener){
        mListener = listener;
    }

    /*
    Метод вызывается, чтобы обновить данные в адаптере, когда завершится разбор файла со станциями.
     */
    void updateDataset(List<Item> items) {
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
            holder.mTextViewCity.setText(item.getItem());
            holder.mHeaderTextViewCountry.setText(item.getHeader());
            /*
            Здесь происходит группирование городов по странам
            */
            holder.mHeaderTextViewCountry.setVisibility(View.GONE);
            if (position == 0) {
                holder.mHeaderTextViewCountry.setVisibility(View.VISIBLE);
            }
            if (position > 0) {
                ItemsGroupedByHeader previousItem = (ItemsGroupedByHeader) mItems.get(position - 1);
                if (!(item.getHeader().equals(previousItem.getHeader()))){
                    holder.mHeaderTextViewCountry.setVisibility(View.VISIBLE);
                }
            }
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }


        holder.mTextViewCity.setOnClickListener(new View.OnClickListener() {
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
