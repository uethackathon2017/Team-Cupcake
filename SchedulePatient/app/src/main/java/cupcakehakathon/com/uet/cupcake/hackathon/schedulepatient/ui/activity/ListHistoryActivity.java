package cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.R;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.Util.PostDataUtils;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.adapter.HistoryAdapter;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.object.HistoryObject;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.data.SQLController;

public class ListHistoryActivity extends BaseActivity {
    private Toolbar toolbar;
    private RecyclerView listHistory;
    private HistoryAdapter historyAdapter;
    private ArrayList<HistoryObject> historyObjects;
    private SQLController sqlController;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_list_history;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listHistory = (RecyclerView) findViewById(R.id.rcvHistory);

    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        setSupportActionBar(toolbar);

        sqlController = new SQLController(getApplicationContext());

        historyObjects = new ArrayList<>();
        historyAdapter = new HistoryAdapter(getApplicationContext(), historyObjects, new HistoryAdapter.HistoryClickListener() {
            @Override
            public void onHistoryClickListener(int id) {

            }

            @Override
            public void onAcceptClickListener(int id) {
                PostDataUtils postDataUtils = new PostDataUtils();
                postDataUtils.accept(ListHistoryActivity.this, historyObjects.get(id).getIdRequest());
            }
        });
    }


}
