package cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.AppController;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.R;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.Util.Globals;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.object.ListFaculty;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.object.ListHospital;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.object.ResponseObject;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.data.SQLController;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.data.SQLHelper;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.data.SyncData;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.ui.activity.ListHistoryActivity;

/**
 * Created by NgocThai on 10/03/2017.
 */

public class PatientService extends Service {

    public static final String CONTROL_SERVICE = "CONTROL_SERVICE";
    public static final String VALUE_GET_LIST_HOSPITAL = "GET_LIST_HOSPITAL";
    public static final String VALUE_GET_FACULTY = "GET_LIST_FACULTY";
    public static final String VALUE_GET_RESPONSE_OF_DOCTOR = "GET_RESPONSE_DOCTOR";
    public static final String VALUE_ID_REQUEST = "ID_REQUEST";

    private NotificationManager myNotificationManager;
    private int notificationId = 111;

    private String TAG_REQ_ILLNESS = "ILLNESS", TAG_REQ_FACULTY = "FACULTY";


    public static final String TAG = "SERVICE";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getExtras() != null) {
            String action = intent.getStringExtra(PatientService.CONTROL_SERVICE);
            switch (action) {
                case VALUE_GET_LIST_HOSPITAL: {
                    reqListHospital(this);
                    reqListFaculty(this);
                    break;
                }
                case VALUE_GET_RESPONSE_OF_DOCTOR: {

                    break;
                }
            }
        }
        return START_STICKY;
    }

    public static final String TAG_REQ_HOSPITAL = "HOSPITAL";
    public static final String BROADCAST_EMPTY_LIST_HOSPITAL = "LIST_HOSPITAL_EMPTY";
    public static final String BROADCAST_UPDATE_HOSPITAL = "LIST_HOSPITAL_SHOW";
    public static final String BROADCAST_ERROR_REQ_HOSPITAL = "ERROR_REQ_HOSPITAL";
    public static final String BROADCAST_UPDATE_FACULTY = "UPDATE_FACULTY";
    public static final String BROADCAST_ERROR_REQ_FACULTY = "ERROR_REQ_FACULTY";
    public static final String BROADCAST_EMPTY_LIST_FACULTY = "EMPTY_LIST_FACULTY";
    public static final String URL_GET_ALL_HOSPITAL = "http://datuet.esy.es/cupcake/get_all_hospital.php";
    private static String URL_ALL_FACULTY_OF_HOSPITAL = "http://datuet.esy.es/cupcake/get_all_faculty.php";

    private void reqListHospital(final Context context) {
        StringRequest srtReq = new StringRequest(Request.Method.GET, URL_GET_ALL_HOSPITAL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String result = response.toString();
                Gson gson = new Gson();
                try {
                    ListHospital listHospital = gson.fromJson(result, ListHospital.class);
                    if (listHospital.getHospital().size() > 0) {
                        SQLController controller = new SQLController(context);
                        // insert to sqlite new data
                        controller.deleteAllData(SQLHelper.TABLE_NAME_HOSPITAL);
                        for (int i = 0; i < listHospital.getHospital().size(); i++) {
                            boolean insert = controller.insertHospital(listHospital.getHospital().get(i));
                            insert = false;
                        }
                        Intent i = new Intent();
                        i.setAction(PatientService.BROADCAST_UPDATE_HOSPITAL);
                        context.sendBroadcast(i);
                    } else {
                        Intent i = new Intent();
                        i.setAction(PatientService.BROADCAST_EMPTY_LIST_HOSPITAL);
                        context.sendBroadcast(i);
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent i = new Intent();
                i.setAction(PatientService.BROADCAST_ERROR_REQ_HOSPITAL);
                context.sendBroadcast(i);
            }
        });
        AppController.getInstance().addToRequestQueue(srtReq, TAG_REQ_HOSPITAL);
    }


    private void reqListFaculty(final Context context) {
        StringRequest strReq = new StringRequest(Request.Method.GET, URL_ALL_FACULTY_OF_HOSPITAL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String result = response.toString();
                Gson gson = new Gson();
                try {
                    ListFaculty listFaculty = gson.fromJson(result, ListFaculty.class);
                    if (listFaculty.getListFaculty().size() > 0) {
                        SQLController controller = new SQLController(context);
                        if (SyncData.checkFacultyChange(controller.queryFaculty(SQLHelper.SQL_SELECT_ALL_FACULTY), listFaculty.getListFaculty())) {
                            controller.deleteAllData(SQLHelper.TABLE_NAME_FACULTY);
                            for (int i = 0; i < listFaculty.getListFaculty().size(); i++) {
                                boolean insert = controller.insertFaculty(listFaculty.getListFaculty().get(i));
                                insert = false;
                            }
                            Intent i = new Intent();
                            i.setAction(PatientService.BROADCAST_UPDATE_FACULTY);
                            context.sendBroadcast(i);
                        }
                    } else {
                        Intent i = new Intent();
                        i.setAction(PatientService.BROADCAST_EMPTY_LIST_FACULTY);
                        context.sendBroadcast(i);
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent i = new Intent();
                i.setAction(PatientService.BROADCAST_ERROR_REQ_FACULTY);
                context.sendBroadcast(i);
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, TAG_REQ_FACULTY);
    }

    private void reqResponseDoctor(final Context context, final int id) {
        StringRequest strReq =
                new StringRequest(Request.Method.POST, URL_CHECK_RESPONSE_DOCTOR, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response.toString();
                        Log.i(TAG, "onResponse: " + result);
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            JSONObject js = jsonArray.getJSONObject(0);
                            String status = js.getString("message");
                            if (status.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                                JSONArray jsArr = js.getJSONArray("value");
                                JSONObject jsResponse = jsArr.getJSONObject(0);
                                ResponseObject responseObject = new ResponseObject(jsResponse.getString("appointmentTime"), jsResponse.getString("appointmentTimeEnd"),
                                        jsResponse.getString("doctorDesc"), jsResponse.getString("doctorName"), jsResponse.getInt("limitOneDay"), jsResponse.getString("responseDesc"),
                                        jsResponse.getString("roomName"));

                                // save history
                                SQLController controller = new SQLController(context);
                                boolean insert = controller.insertHistoryRes(responseObject.getAppointmentTime(), responseObject.getAppointmentTimeEnd(),
                                        responseObject.getDoctorDesc(), responseObject.getDoctorName(), responseObject.getResponseDesc(),
                                        responseObject.getRoomName(), Globals.idRequestResponse);
                                insert = false;
                                displayNotification("");


                            } else {
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        reqResponseDoctor(context, Globals.idRequestResponse);
                                    }
                                }, TIME_REQUEST_TO_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    /**
                     * @return
                     */
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(REQUEST_ID, id + "");
                        return params;
                    }

                    @Override
                    public Priority getPriority() {
                        return Priority.IMMEDIATE;
                    }
                };
        AppController.getInstance().addToRequestQueue(strReq, TAG_REQ_RESPONSE);
    }

    protected void displayNotification(String message) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());

        mBuilder.setContentTitle("You have a new message");

        mBuilder.setContentText(message);

        mBuilder.setTicker("Message from my doctor!");


        mBuilder.setSmallIcon(R.drawable.icon_app_schedule);

        // mBuilder.setNumber(++numMessagesOne);
        Intent resultIntent = new Intent(getApplicationContext(), ListHistoryActivity.class);
//
        resultIntent.putExtra("notificationId", notificationId);
        resultIntent.putExtra("message", message);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());

        stackBuilder.addParentStack(ListHistoryActivity.class);

        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =

                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        mBuilder.setContentIntent(resultPendingIntent);

        myNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        myNotificationManager.notify(notificationId, mBuilder.build());
    }

    private static final int TIME_REQUEST_TO_SERVER = 60 * 1000;
    private String TAG_REQ_RESPONSE = "REQ_RESPONSE";
    public static final String REQUEST_ID = "id";
    public static String RESPONSE_SUCCESS = "success";
    private static final String URL_CHECK_RESPONSE_DOCTOR = "http://cupcake96uet.hol.es/api/api_get_checked_request.php";


}
