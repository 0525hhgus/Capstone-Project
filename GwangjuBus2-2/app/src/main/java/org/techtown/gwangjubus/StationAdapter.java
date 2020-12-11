package org.techtown.gwangjubus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.StationViewHolder>
        implements OnStationClickListener{

    private ArrayList<StationList> mList;
    private LayoutInflater mInflate;
    private Context mContext;

    OnStationClickListener listener;

    public StationAdapter(Context context, ArrayList<StationList> itmes) {
        this.mList = itmes;
        this.mInflate = LayoutInflater.from(context);
        this.mContext = context;
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.busstop_imformation, parent, false);
        StationViewHolder viewHolder = new StationViewHolder(view, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {
        //binding
        // holder.busId.setText(mList.get(position).getBusstopId());
        holder.busstopName.setText(mList.get(position).getBusstopName());

        //Click event
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    @Override
    public void onItemClick(StationAdapter.StationViewHolder holder, View view, int position) {
        if(listener != null){ listener.onItemClick(holder,view,position); }
    }

    public void setOnItemClicklistener(OnStationClickListener listener){
        this.listener = listener;
    }


    //ViewHolder
    public static class StationViewHolder extends RecyclerView.ViewHolder {

        public TextView busstopName;

        public StationViewHolder(View itemView, final OnStationClickListener listener) {
            super(itemView);

            busstopName = itemView.findViewById(R.id.busstopsearchname);

            // recylerview 클릭 시
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(StationAdapter.StationViewHolder.this, v, position);
                    }

                }
            });
        }

    }
    public StationList getItem(int position){
        return mList.get(position);
    }

    public void addItem(StationList item){ mList.add(item); }

    public void setItems(ArrayList<StationList> items){ this.mList = items; }


    public void setItem(int position, StationList item) {
        mList.set(position, item);
    }
}
