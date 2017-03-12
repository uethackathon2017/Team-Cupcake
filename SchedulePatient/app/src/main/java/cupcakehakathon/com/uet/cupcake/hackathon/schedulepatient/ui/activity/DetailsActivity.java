package cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;

import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.R;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.Util.Constants;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.Util.DialogUtils;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.Util.Global;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.Util.PostDataUtils;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.Util.RecycleUtils;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.Util.ToastUtils;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.Utils;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.adapter.FacultyAdapter;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.listener.Listener;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.object.FacultyObject;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.object.HospitalObject;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.object.RequestObject;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.data.SQLController;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.data.SQLHelper;

/**
 * Created by Luong Tran on 3/11/2017.
 */

public class DetailsActivity
        extends BaseActivity
        implements
        Listener.requestStatus {

    private String TAG = "DETAILS";
    public static final int MAX_LINES = 3;
    public static final int TWO_SPACES = 2;

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imgDetails;
    private int idHospital;
    private HospitalObject hospitalObject;
    private ArrayList<FacultyObject> lsFacultySub, lsFaculty;
    private RecyclerView recyclerView;
    private FacultyAdapter adapter;
    private TextView txtAddress, txtPhone, txtRate, txtDescription;
    private RatingBar mRatingBar;
    private String targetTime = "";

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_details;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState) {
        toolbar = (Toolbar) findViewById(R.id.toolbarDetails);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        imgDetails = (ImageView) findViewById(R.id.imgDetailHospital);
        recyclerView = (RecyclerView) findViewById(R.id.rcvFaculty);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        receiveId();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SQLController controller = new SQLController(this);
        ArrayList<HospitalObject> ls =
                controller.queryListHospital(SQLHelper.SQL_SELECT_HOSPITAL_BY_ID + idHospital);
        hospitalObject = ls.get(0);
        lsFaculty = controller.queryFaculty(SQLHelper.SQL_SELECT_FACULTY_BY_HOSPITAL
                + idHospital
                + " AND "
                + SQLHelper.FAC_KIND
                + " = "
                + Constants.ID_FACULTY_KIND);
        lsFacultySub = controller.queryFaculty(SQLHelper.SQL_SELECT_FACULTY_BY_HOSPITAL
                + idHospital
                + " AND "
                + SQLHelper.FAC_KIND
                + " = "
                + Constants.ID_SUB_FACULTY_KIND);

        adapter = new FacultyAdapter(lsFaculty, this);
        RecycleUtils.showListRcv(recyclerView, adapter, new Listener.listenFaculty() {
            @Override
            public void onClick(int id) {
                showDialogRequest(DetailsActivity.this, "SEND REQUEST", "SEND", "CANCEL", new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });

            }
        }, this);


        try {
            ImageLoader.getInstance().displayImage(hospitalObject.getImage(), imgDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }

        collapsingToolbarLayout.setTitle(hospitalObject.getName());
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        txtAddress.setText(hospitalObject.getAddress());
        txtPhone.setText(hospitalObject.getPhone());
        //txtRate.setText(hospitalObject.getRate() + "");
        mRatingBar.setMax(5);
        txtDescription.setText(hospitalObject.getDesc());

        mRatingBar.setRating((float) hospitalObject.getRate());

        //seeMoreLine();

        //makeTextViewResizable(txtDescription, 3, hospitalObject.getDesc(), true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return true;
    }

    @Override
    public void requestSuccess(int id) {
        Global.idRequestResponse = id;
        Intent intent = new Intent();
        intent.putExtra(Constants.HISTORY_ID,id);
        Log.i(TAG, "requestSuccess: success " + id);
        finish();
        ToastUtils.quickToast(getBaseContext(), "Send request success");
    }

    @Override
    public void requestError() {

    }

    @Override
    public void requestErrorResponse() {

    }

    private void receiveId() {
        idHospital = getIntent().getIntExtra(Constants.PASS_ID_HOSPITAL, 0);
    }


    public void showDialogRequest(final Context context, String title, String positive, String negative,
                                  DialogInterface.OnDismissListener dismissListener) {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_request, null);

        builder.setTitle(title);

        final EditText date = (EditText) view.findViewById(R.id.edtDate);
        final EditText edtSymptom = (EditText) view.findViewById(R.id.edtSymptom);
        date.requestFocus();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                DialogUtils.dialogShowDate(DetailsActivity.this, "Choose date", new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        targetTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    }
                });
            }
        });
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean close = false;
                final String symptom = edtSymptom.getText().toString();
                if (targetTime.matches("") || symptom.matches("")) {
                    ToastUtils.quickToast(context, "Điền đầy đủ thông tin");
                    close = false;
                } else {
                    close = true;
                    String timeRequest = Utils.getCurrentTime(Utils.VALUES_DATE) + " " + Utils.getCurrentTime(Utils.VALUES_TIME);
                    PostDataUtils postDataUtils = new PostDataUtils();
                    postDataUtils.setRequestStatus(DetailsActivity.this);
                    String idPatient = Utils.getValueFromPreferences(Constants.PREFERENCES_LOGIN_ID, getBaseContext());
                    RequestObject requestObject = new RequestObject(Integer.parseInt(idPatient), symptom, timeRequest, 1, 0, targetTime);
                    postDataUtils.sendRequest(getBaseContext(), new RequestObject(Integer.parseInt(idPatient), symptom, timeRequest, 1, 0, targetTime));
                    SQLController sqlController = new SQLController(getBaseContext());
                    boolean insert = sqlController.insertRequest(requestObject);
                    insert = false;
                }
                if (close) {
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(date.getWindowToken(), 0);
                dialog.dismiss();
            }
        });
        builder.setOnDismissListener(dismissListener);
        builder.setCancelable(false);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }
}


