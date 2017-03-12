package cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.Util.Constants;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.Util.Globals;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.Util.RecycleUtils;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.Util.ToastUtils;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.Utils;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.adapter.HospitalAdapter;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.listener.Listener;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.object.HospitalObject;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.data.SQLController;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.data.SQLHelper;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.service.PatientService;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.ui.activity.BaseActivity;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.ui.activity.DetailsActivity;

import java.util.ArrayList;

import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.ui.activity.ListHistoryActivity;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private HospitalAdapter adapter;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState) {
        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        recyclerView = (RecyclerView) findViewById(R.id.rcvHospitalMain);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        setSupportActionBar(toolbar);

        if (Utils.checkNetwork(this)) {
            // if have connection. get list hospital
            Intent intent = new Intent(MainActivity.this, PatientService.class);
            intent.putExtra(PatientService.CONTROL_SERVICE, PatientService.VALUE_GET_LIST_HOSPITAL);
            startService(intent);
        }
        Globals.idRequestResponse = 13;


        SQLController controller = new SQLController(this);
        ArrayList<HospitalObject> ls = controller.queryListHospital(SQLHelper.SQL_SELECT_ALL_HOSPITAL);
        adapter = new HospitalAdapter(ls, this);
        RecycleUtils.showListRcv(recyclerView, adapter, new Listener.listenHospital() {
            @Override
            public void onClick(int id) {
                Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                i.putExtra(Constants.PASS_ID_HOSPITAL, id + 1);
                startActivity(i);
            }
        }, MainActivity.this);

        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        alphaAdapter.setDuration(1000);
        alphaAdapter.setInterpolator(new OvershootInterpolator());
        alphaAdapter.setFirstOnly(true);

        alphaAdapter = new AlphaInAnimationAdapter(adapter);
        recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));

        recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));

        recyclerView.getItemAnimator().setAddDuration(500);
        recyclerView.getItemAnimator().setRemoveDuration(500);
        recyclerView.getItemAnimator().setMoveDuration(500);
        recyclerView.getItemAnimator().setChangeDuration(500);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if(id == R.id.nav_history){
            startActivity(new Intent(new Intent(getApplicationContext(),ListHistoryActivity.class)));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver, new IntentFilter(PatientService.BROADCAST_EMPTY_LIST_HOSPITAL));
        registerReceiver(broadcastReceiver, new IntentFilter(PatientService.BROADCAST_ERROR_REQ_HOSPITAL));
        registerReceiver(broadcastReceiver, new IntentFilter(PatientService.BROADCAST_UPDATE_HOSPITAL));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case PatientService.BROADCAST_UPDATE_HOSPITAL: {
                    // update list when have change to service
                    SQLController controller = new SQLController(MainActivity.this);
                    ArrayList<HospitalObject> ls = controller.queryListHospital(SQLHelper.SQL_SELECT_ALL_HOSPITAL);
                    adapter.setLsHospital(ls);
                    RecycleUtils.showListRcv(recyclerView, adapter, new Listener.listenHospital() {
                        @Override
                        public void onClick(int id) {
                            Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                            i.putExtra(Constants.PASS_ID_HOSPITAL, id + 1);
                            startActivity(i);
                        }
                    }, MainActivity.this);
                    break;
                }
                case PatientService.BROADCAST_ERROR_REQ_HOSPITAL: {
                    ToastUtils.quickToast(MainActivity.this, "ERROR REQUEST TO SERVER");
                    break;
                }
                case PatientService.BROADCAST_EMPTY_LIST_HOSPITAL: {
                    ToastUtils.quickToast(MainActivity.this, "No data available");
                    break;
                }
            }
        }
    };

}
