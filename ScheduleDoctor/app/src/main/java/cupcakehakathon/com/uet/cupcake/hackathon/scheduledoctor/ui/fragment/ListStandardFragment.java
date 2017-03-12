package cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.R;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.common.adapter.ListRequestAdapter;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.common.object.RequestObject;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.data.SQLController;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.data.SQLHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListStandardFragment extends BaseFragment {

    private ListRequestAdapter adapter;
    private RecyclerView recyclerView;
    private SQLController sqlController;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_list_standard;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rcvStandard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
//        requestObjects = new ArrayList<>();
        sqlController = new SQLController(getActivity());
//        requestObjects.addAll(sqlController.queryListRequest(SQLHelper.SQL_QUERY_ALL_REQUEST));
    }


}
