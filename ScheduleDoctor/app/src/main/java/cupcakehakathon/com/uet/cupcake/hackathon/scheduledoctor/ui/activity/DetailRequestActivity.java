package cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.R;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.adapter.ListRoomAdapter;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.common.object.RequestObject;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.common.object.ResponseObject;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.common.object.RoomObject;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.data.SQLController;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.data.SQLHelper;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.service.DoctorService;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils.Constants;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils.DialogUtils;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils.Global;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils.ToastUtils;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils.Utils;

public class DetailRequestActivity extends BaseActivity
        implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private ImageView imgBack;
    private TextView tvTitle;
    private TextView tvName;
    private TextView tvBirthday;
    private TextView tvGender;
    private TextView tvAddress;
    private TextView tvIdentityNumber;
    private TextView tvInsuranceCode;
    private TextView tvDayTarget;
    private TextView tvSymptom;
    private RequestObject requestObject;
    private ListRoomAdapter listRoomAdapter;
    private Button btnDone;
    private RecyclerView recyclerView;
    private SQLController sqlController;
    ArrayList<RoomObject> roomObjects;
    private int selectedPosition = 0;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RelativeLayout layoutTimeEnd;
    private DialogUtils du;
    private String time;
    private TextView tvApppointmentTime;
    private TextView tvApppointmentTimeEnd;
    private EditText edtDescription;
    private int positionMenu;


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_detail_request;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvName = (TextView) findViewById(R.id.tvName);
        tvBirthday = (TextView) findViewById(R.id.tvBirthday);
        tvGender = (TextView) findViewById(R.id.tvGender);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvIdentityNumber = (TextView) findViewById(R.id.tvIdentityNumber);
        tvInsuranceCode = (TextView) findViewById(R.id.tvInsuranceCode);
        tvDayTarget = (TextView) findViewById(R.id.tvDayTarget);
        tvSymptom = (TextView) findViewById(R.id.tvSymptom);
        layoutTimeEnd = (RelativeLayout) findViewById(R.id.layoutTimeEnd);
        tvApppointmentTime = (TextView) findViewById(R.id.tvApppointmentTime);
        tvApppointmentTimeEnd = (TextView) findViewById(R.id.tvApppointmentTimeEnd);
        edtDescription = (EditText) findViewById(R.id.edtDescription);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {


        imgBack.setOnClickListener(this);
        btnDone = (Button) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);
        layoutTimeEnd.setOnClickListener(this);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitle("Xứ lý yêu cầu");

        Intent intent = getIntent();
        requestObject = (RequestObject) intent.getSerializableExtra(Constants.REQUEST_OBJECT);
        Global.dateTarger = requestObject.getDayTarget();

        if (Utils.checkNetwork(this)) {
            Intent i = new Intent(DetailRequestActivity.this, DoctorService.class);
            i.putExtra(DoctorService.CONTROL_SERVICE, DoctorService.VALUE_GET_ALL_ROOM);
            startService(i);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        sqlController = new SQLController(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        roomObjects = new ArrayList<>();
        listRoomAdapter = new ListRoomAdapter(getApplicationContext(), roomObjects
                , new ListRoomAdapter.OnRoomClickListener() {
            @Override
            public void onClickListener(View v, int position) {
                tvApppointmentTime.setText(roomObjects.get(position).getTimeAvailable());
                changeSelectedPosition(position);
            }

            @Override
            public void onOptionClickListener(View v, int position) {
                positionMenu = position;
                showPopupMenu(v);
            }
        });

        tvTitle.setText("Xử lý yêu cầu");
        tvName.setText(requestObject.getName());
        tvBirthday.setText(requestObject.getBirthday());
        tvAddress.setText(requestObject.getAddress());
        tvIdentityNumber.setText(requestObject.getIdentityNumber());
        tvInsuranceCode.setText(requestObject.getInsuranceCode());
        tvDayTarget.setText(requestObject.getDayTarget());
        tvSymptom.setText(requestObject.getSymptom());
        tvGender.setText(requestObject.getGender());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        roomObjects.addAll(sqlController.queryListRoom(SQLHelper.SQL_QUERY_ALL_ROOM));
        recyclerView.setAdapter(listRoomAdapter);
        listRoomAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.btnDone:
                if (time == null) {
                    ToastUtils.quickToast(getApplicationContext()
                            , "Bạn phải nhập thời gian kết thúc khám bệnh");
                } else {
                    ResponseObject responseObject = new ResponseObject();

                    //set id doctor
                    responseObject.setIdDoctor(Integer
                            .parseInt(Utils.getValueFromPreferences
                                    (Constants.PREFERENCES_LOGIN_ID, getApplicationContext())));
                    //set id room
                    responseObject.setIdRoom(selectedPosition);
                    //set id request
                    responseObject.setIdRequest(requestObject.getId());

                    //set time start
                    responseObject.setAppointmentTime(roomObjects.get(selectedPosition).getTimeAvailable());

                    //desc
                    responseObject.setDescription(edtDescription.getText().toString().trim());
                    //set time end
                    responseObject.setApppointmentTimeEnd(time);
                    //get current date
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    responseObject.setResponseDate(date);
                    Intent intent = new Intent(getApplicationContext(), DoctorService.class);
                    intent.putExtra(DoctorService.CONTROL_SERVICE, DoctorService.VALUE_POST_RESPONSE);
                    intent.putExtra(Constants.RESPONSE_OBJECT, responseObject);
                    startService(intent);
                }

                break;
            case R.id.layoutTimeEnd:
                du = new DialogUtils();

                du.dialogShowTime(DetailRequestActivity.this, "Choose Time"
                        , new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                                String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                                String minuteString = minute < 10 ? "0" + minute : "" + minute;
                                String secondString = second < 10 ? "0" + second : "" + second;
                                time = hourString + ":" + minuteString + ":" + secondString;
                                tvApppointmentTimeEnd.setText(time);
                            }
                        });

                break;
        }
    }

    public void changeSelectedPosition(int index) {
        listRoomAdapter.notifyItemChanged(listRoomAdapter.getSelectedPosition());
        selectedPosition = index;
        listRoomAdapter.setSelectedPosition(selectedPosition);
        listRoomAdapter.notifyItemChanged(selectedPosition);
    }

    public void showPopupMenu(View view) {
        Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.MyPopupMenu);
        PopupMenu popupMenu = new PopupMenu(wrapper, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.album_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_detail) {
            startActivity(new Intent(getApplicationContext(), DetailRoomActivity.class));
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver, new IntentFilter(DoctorService.BROAD_CAST_UPDATE_ROOM));
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
                case DoctorService.BROAD_CAST_UPDATE_ROOM: {
                    SQLController controller = new SQLController(DetailRequestActivity.this);
                    listRoomAdapter.setRoomObjects(controller.queryListRoom(SQLHelper.SQL_QUERY_ALL_ROOM));
                    recyclerView.setAdapter(listRoomAdapter);
                    listRoomAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    };
}
