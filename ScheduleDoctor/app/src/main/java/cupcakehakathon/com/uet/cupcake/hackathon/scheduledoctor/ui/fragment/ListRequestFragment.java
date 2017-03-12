package cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.ui.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.R;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.common.adapter.ListRequestAdapter;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.common.object.RequestObject;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.data.SQLController;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.data.SQLHelper;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.service.DoctorService;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.ui.activity.MainActivity;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils.ToastUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListRequestFragment extends BaseFragment {

    private ListRequestAdapter adapter;
    private RecyclerView recyclerView;
    private SQLController sqlController;
    ArrayList<RequestObject> requestObjects;

    public ListRequestFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_list_request;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        requestObjects = new ArrayList<>();
        sqlController = new SQLController(getActivity());
        requestObjects.addAll(sqlController.queryListRequest(SQLHelper.SQL_QUERY_ALL_REQUEST));
        adapter = new ListRequestAdapter(getActivity(), requestObjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(DoctorService.BROADCAST_ERROR_REQ_REQUEST));
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(DoctorService.BROADCAST_EMPTY_LIST_REQUEST));
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(DoctorService.BROADCAST_UPDATE_REQUEST));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (broadcastReceiver != null) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case DoctorService.BROADCAST_UPDATE_REQUEST: {
                    SQLController controller = new SQLController(getActivity());
                    ArrayList<RequestObject> ls = controller.queryListRequest(SQLHelper.SQL_QUERY_ALL_REQUEST);
                    adapter.setRequestObjects(ls);
                    requestObjects.addAll(sqlController.queryListRequest(SQLHelper.SQL_QUERY_ALL_REQUEST));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                }
                case DoctorService.BROADCAST_EMPTY_LIST_REQUEST: {

                    break;
                }
                case DoctorService.BROADCAST_ERROR_REQ_REQUEST: {

                    break;
                }
            }
        }
    };

}
