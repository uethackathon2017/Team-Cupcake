package cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.Map;

import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.AppController;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.common.object.ListRequest;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.common.object.ListRoomObject;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.common.object.ResponseObject;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.data.SQLController;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.data.SQLHelper;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils.Constants;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils.Global;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils.Utils;

/**
 * Created by Luong Tran on 3/10/2017.
 */

public class DoctorService extends Service {

    private String TAG = "SERVICE";

    public static final String BROADCAST_UPDATE_REQUEST = "UPDATE_REQUEST";
    public static final String BROADCAST_EMPTY_LIST_REQUEST = "EMPTY_REQUEST";
    public static final String BROADCAST_ERROR_REQ_REQUEST = "ERROR_REQUEST";

    public static final String BROAD_CAST_UPDATE_ROOM = "UPDATE_ROOM";

    public static final String CONTROL_SERVICE = "CONTROL_SERVICE";
    public static final String VALUE_GET_ALL_REQUEST_BY_FACULTY = "GET_ALL_REQ_BY_FACULTY";
    public static final String VALUE_POST_RESPONSE = "POST_RESPONSE";
    public static final String VALUE_GET_ALL_ROOM = "GET_ALL_ROOM";

    public static final String PASS_DATA_ID_REQUEST = "ID_REQUEST";

    private static String URL_CREATE_RESPONSE = "http://datuet.esy.es/api/api_create_response.php";

    // response
    public static final String RESPONSE_APPOINTMENT_TIME = "appointmentTime";
    public static final String RESPONSE_DESC = "description";
    public static final String RESPONSE_ID_DOCTOR = "idDoctor";
    public static final String RESPONSE_ID_ROOM = "idRoom";
    public static final String RESPONSE_ID_REQUEST = "idRequest";
    public static final String RESPONSE_APPOINTMENT_TIME_END = "appointmentTimeEnd";
    public static final String RESPONSE_DATE = "dateResponse";

    // room
    public static final String ID_FACULTY = "idFaculty";

    private String TAG_REQ_REQUEST = "REQ_REQUEST", TAG_REQ_RESPONSE = "REQ_RESPONSE";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getExtras() != null) {
            String action = intent.getStringExtra(DoctorService.CONTROL_SERVICE);
            ResponseObject responseObject = (ResponseObject) intent.getSerializableExtra(Constants.RESPONSE_OBJECT);
            switch (action) {
                case VALUE_GET_ALL_REQUEST_BY_FACULTY: {
                    getAllRequest(this);
                    break;
                }
                case VALUE_POST_RESPONSE: {
                    postResponse(getApplicationContext(), responseObject);
                    break;
                }
                case VALUE_GET_ALL_ROOM: {
                    int idFaculty = Integer.parseInt(Utils.getValueFromPreferences(Constants.PREFERENCES_ID_FACULTY, this));
                    getAllRoomFacultyWithDay(this, idFaculty, Utils.getCurrentTime(Utils.VALUES_DATE));
                    break;
                }
            }
        }
        return START_STICKY;
    }

    private static String ID_ROOM_FACULTY = "idFaculty";
    private static String DATE_ROOM = "dateTarget";
    private static String TAG_REQ_ROOM = "REQ_ROOM";

    private void postResponse(final Context context, final ResponseObject responseObject) {
        StringRequest strReq =
                new StringRequest(Request.Method.POST, URL_CREATE_RESPONSE, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response.toString();
                        Log.i(TAG, "onResponse: result post " + result);
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
                        params.put(RESPONSE_APPOINTMENT_TIME, responseObject.getAppointmentTime());
                        params.put(RESPONSE_DESC, responseObject.getDescription());
                        params.put(RESPONSE_ID_DOCTOR, responseObject.getIdDoctor() + "");
                        params.put(RESPONSE_ID_REQUEST, responseObject.getIdRequest() + "");
                        params.put(RESPONSE_ID_ROOM, responseObject.getIdRoom() + "");
                        params.put(RESPONSE_DATE, responseObject.getResponseDate());
                        params.put(RESPONSE_APPOINTMENT_TIME_END, responseObject.getApppointmentTimeEnd());

                        return params;
                    }

                    @Override
                    public Priority getPriority() {
                        return Priority.IMMEDIATE;
                    }
                };
        AppController.getInstance().addToRequestQueue(strReq, TAG_REQ_RESPONSE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static final int TIME_REQUEST_TO_SERVER = 20 * 1000;
    private static String URL_GET_ALL_REQUEST = "http://datuet.esy.es/cupcake/get_all_request_folow_faculty.php?id=";

    private void getAllRequest(final Context context) {
        int idFaculty = Integer.parseInt(Utils.getValueFromPreferences(Constants.PREFERENCES_ID_FACULTY, context));
        StringRequest strReq = new StringRequest(Request.Method.GET, URL_GET_ALL_REQUEST + idFaculty, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String result = response.toString();
                Gson gson = new Gson();
                try {
                    // request to server every 2 minutes
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            getAllRequest(context);
                        }
                    }, TIME_REQUEST_TO_SERVER);
                    ListRequest listRequest = gson.fromJson(result, ListRequest.class);
                    if (listRequest.getRequest().size() > 0) {
                        // insert data to database and update ui
                        SQLController controller = new SQLController(context);
                        boolean delete = controller.deleteAllData(SQLHelper.TABLE_NAME_REQUEST);
                        for (int i = 0; i < listRequest.getRequest().size(); i++) {
                            boolean insert = controller.insertRequest(listRequest.getRequest().get(i));
                            insert = false;
                        }
                        Intent i = new Intent();
                        i.setAction(DoctorService.BROADCAST_UPDATE_REQUEST);
                        context.sendBroadcast(i);
                    } else {
                        Intent i = new Intent();
                        i.setAction(DoctorService.BROADCAST_EMPTY_LIST_REQUEST);
                        context.sendBroadcast(i);
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent i = new Intent();
                i.setAction(DoctorService.BROADCAST_ERROR_REQ_REQUEST);
                context.sendBroadcast(i);
            }
        });
        AppController.getInstance().

                addToRequestQueue(strReq, TAG_REQ_REQUEST);
    }

    private static String URL_GET_ALL_ROOM_OF_FACULTY = "http://datuet.esy.es/api/api_get_room_with_day.php";

    private void getAllRoomFacultyWithDay(final Context context, final int id, final String dateTarget) {
        StringRequest strReq =
                new StringRequest(Request.Method.POST, URL_GET_ALL_ROOM_OF_FACULTY, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response.toString();
                        Gson gson = new Gson();
                        try {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    int idFaculty = Integer.parseInt(Utils.getValueFromPreferences(Constants.PREFERENCES_ID_FACULTY, context));
                                    getAllRoomFacultyWithDay(context, idFaculty, Global.dateTarger);
                                }
                            }, TIME_REQUEST_TO_SERVER);
                            SQLController controller = new SQLController(context);
                            ListRoomObject listRoomObject = gson.fromJson(result, ListRoomObject.class);
                            controller.deleteAllData(SQLHelper.TABLE_NAME_ROOM);
                            for (int i = 0; i < listRoomObject.getListRoom().size(); i++) {
                                boolean insert = controller.insertRoom(listRoomObject.getListRoom().get(i));
                                insert = false;
                            }
                            Intent i = new Intent();
                            i.setAction(DoctorService.BROAD_CAST_UPDATE_ROOM);
                            context.sendBroadcast(i);
                        } catch (JsonSyntaxException e) {
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
                        params.put(ID_ROOM_FACULTY, id + "");
                        params.put(DATE_ROOM, dateTarget);
                        return params;
                    }

                    @Override
                    public Priority getPriority() {
                        return Priority.IMMEDIATE;
                    }
                };
        AppController.getInstance().addToRequestQueue(strReq, TAG_REQ_ROOM);
    }
}
