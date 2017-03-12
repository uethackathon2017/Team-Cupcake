package cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.R;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.common.object.RequestObject;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.data.SQLController;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.data.SQLHelper;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.service.DoctorService;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.ui.fragment.ListRequestFragment;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.ui.fragment.ListRoomFragment;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.ui.fragment.ListStandardFragment;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils.Constants;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils.ToastUtils;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils.Utils;

import java.util.ArrayList;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {

        if (Utils.checkNetwork(this)) {
            Intent intent = new Intent(MainActivity.this, DoctorService.class);
            intent.putExtra(DoctorService.CONTROL_SERVICE, DoctorService.VALUE_GET_ALL_REQUEST_BY_FACULTY);
            startService(intent);
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager, false);

        SQLController controller = new SQLController(this);
        ArrayList<RequestObject> ls = controller.queryListRequest(SQLHelper.SQL_QUERY_ALL_REQUEST);

        TabLayout.Tab upcoming = tabLayout.getTabAt(0);
        upcoming.setText("LIST REQUEST");
        TabLayout.Tab allMyRides = tabLayout.getTabAt(1);
        allMyRides.setText("LIST ROOM");

        //set text color
        tabLayout.setTabTextColors(ContextCompat
                .getColorStateList(this, R.color.text_pressed_black_tab));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat
                .getColor(this, android.R.color.white));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        setSupportActionBar(toolbar);
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

        } else if (id == R.id.nav_logout) {
            Utils.deleteValueFromPreferences(Constants.PREFERENCES_LOGIN, getApplicationContext());
            Utils.deleteValueFromPreferences(Constants.PREFERENCES_LOGIN_ID, getApplicationContext());
            Utils.deleteValueFromPreferences(Constants.PREFERENCES_ID_FACULTY, getApplicationContext());
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ListRequestFragment();
                case 1:
                    return new ListRoomFragment();
            }
            return new ListRequestFragment();
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}
