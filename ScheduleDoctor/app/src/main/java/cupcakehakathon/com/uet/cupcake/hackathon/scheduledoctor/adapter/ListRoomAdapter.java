package cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.R;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.common.object.RoomObject;

/**
 * Created by Dat UET on 3/11/2017.
 */

public class ListRoomAdapter extends RecyclerView.Adapter<ListRoomAdapter.RoomHolder> {

    private Context context;
    private ArrayList<RoomObject> roomObjects;
    private OnRoomClickListener onRoomClickListener;
    private int selectedPosition;

    public ListRoomAdapter(Context context, ArrayList<RoomObject> roomObjects, OnRoomClickListener onRoomClickListener) {
        this.context = context;
        this.roomObjects = roomObjects;
        this.onRoomClickListener = onRoomClickListener;
    }

    @Override
    public RoomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.room_item, parent, false);
        RoomHolder roomHolder = new RoomHolder(view);
        return roomHolder;
    }

    @Override
    public void onBindViewHolder(RoomHolder holder, int position) {
        RoomObject roomObject = roomObjects.get(position);
        if(position == selectedPosition){
            holder.tvName.setTextColor(ContextCompat.getColor(context,android.R.color.holo_red_light));
            holder.tvTime.setTextColor(ContextCompat.getColor(context,android.R.color.holo_red_light));
        } else {
            holder.tvName.setTextColor(ContextCompat.getColor(context,android.R.color.white));
            holder.tvTime.setTextColor(ContextCompat.getColor(context,android.R.color.white));
        }
        holder.tvName.setText(roomObject.getName());
        holder.tvTime.setText(roomObject.getTimeAvailable());
    }

    @Override
    public int getItemCount() {
        return roomObjects.size();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public class RoomHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTime;
        private TextView tvName;
        private ImageView imgOption;

        public RoomHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            imgOption = (ImageView) itemView.findViewById(R.id.imgOption);
            itemView.setOnClickListener(this);
            imgOption.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                onRoomClickListener.onClickListener(v, getAdapterPosition());
            } else {
                onRoomClickListener.onOptionClickListener(v, getAdapterPosition());
            }
        }

    }

    public interface OnRoomClickListener {
        void onClickListener(View v, int position);

        void onOptionClickListener(View v, int position);
    }

    public void setRoomObjects(ArrayList<RoomObject> roomObjects) {
        this.roomObjects = roomObjects;
    }
}
