package cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.R;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.common.object.RequestObject;

/**
 * Created by NgocThai on 11/03/2017.
 */

public class ListRequestAdapter extends RecyclerView.Adapter<RequestHolder> {


    private Context context;
    private ArrayList<RequestObject> requestObjects;

    public ListRequestAdapter(Context context, ArrayList<RequestObject> requestObjects) {
        this.context = context;
        this.requestObjects = requestObjects;
    }

    @Override
    public RequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_item, parent, false);
        RequestHolder requestHolder = new RequestHolder(view);
        requestHolder.setContext(context);
        requestHolder.setRequestObjects(requestObjects);
        return requestHolder;
    }

    @Override
    public void onBindViewHolder(RequestHolder holder, int position) {
        RequestObject requestObject = requestObjects.get(position);
        holder.getTvName().setText(requestObject.getName());
        holder.getTvAddress().setText(requestObject.getAddress());
        holder.getTvBirthday().setText(requestObject.getDayTarget());
    }

    @Override
    public int getItemCount() {
        return requestObjects.size();
    }

    public void setRequestObjects(ArrayList<RequestObject> requestObjects) {
        this.requestObjects = requestObjects;
    }
}
