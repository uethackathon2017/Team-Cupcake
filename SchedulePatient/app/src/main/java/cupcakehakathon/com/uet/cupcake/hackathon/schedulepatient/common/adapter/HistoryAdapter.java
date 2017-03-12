package cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.R;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.object.HistoryObject;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.object.HospitalObject;

/**
 * Created by Dat UET on 3/12/2017.
 */

public class HistoryAdapter  extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder>{

    Context context;
    ArrayList<HistoryObject> historyObjects;
    HistoryClickListener historyClickListener;

    public HistoryAdapter(Context context, ArrayList<HistoryObject> historyObjects, HistoryClickListener historyClickListener) {
        this.context = context;
        this.historyObjects = historyObjects;
        this.historyClickListener = historyClickListener;
    }

    class HistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvHospital;
        private TextView tvFaculty;
        private TextView tvTime;
        private TextView tvAccepted;
        private TextView btnAccept;

        public HistoryHolder(View itemView) {

            super(itemView);
            tvHospital = (TextView) itemView.findViewById(R.id.tvHospital);
            tvFaculty = (TextView) itemView.findViewById(R.id.tvFaculty);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvAccepted = (TextView) itemView.findViewById(R.id.tvAccepted);
            btnAccept = (Button) itemView.findViewById(R.id.btnAcept);
            itemView.setOnClickListener(this);
            btnAccept.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == itemView){
                historyClickListener.onHistoryClickListener(getAdapterPosition());
            } else {
                historyClickListener.onAcceptClickListener(getAdapterPosition());
            }
        }
    }
    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history,parent,false);
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
        HistoryObject historyObject = historyObjects.get(position);
        holder.tvHospital.setText(historyObject.getHospital());
        holder.tvFaculty.setText(historyObject.getNameFaculty());
        holder.tvTime.setText(historyObject.getRequestTime());
        if(historyObject.getChecked() == 1){
            holder.tvAccepted.setVisibility(View.VISIBLE);
            holder.btnAccept.setVisibility(View.GONE);
        } else {
            holder.btnAccept.setVisibility(View.VISIBLE);
            holder.tvAccepted.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return historyObjects.size();
    }

    public interface HistoryClickListener{
        void onHistoryClickListener(int id);
        void onAcceptClickListener(int id);
    }


}
