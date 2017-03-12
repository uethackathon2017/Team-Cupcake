package cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.Util;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.listener.Listener;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.object.PatientObject;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.object.RequestObject;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NgocThai on 10/03/2017.
 */

public class PostDataUtils {

    private String TAG = "POST DATA";

    private static String RESPONSE_SUCCESS = "success";
    private static String RESPONSE_LOGIN_ERROR = "ERROR";
    private static String RESPONSE_FAIL = "fail";

    private static String USERNAME = "userName";
    private static String PASSWORD = "passWord";

    private static String ID_PATIENT = "idPatient";
    private static String SYMPTOM = "symptom";
    private static String REQUEST_TIME = "requestTime";
    private static String ID_FACULTY = "idFaculty";
    private static String DAY_TARGET = "dayTarget";
    private static String CHECKED = "checked";
    private Listener.loginStatus loginStatus;
    public static final String URL_LOGIN = "http://datuet.esy.es/api/api_check_login_patient.php";
    private static String URL_SEND_REQUEST = "http://datuet.esy.es/api/api_create_request_patient.php";

    public void login(final Context context, final String username, final String password) {
        StringRequest stringRequest =
                new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response.toString();
                        Log.i(TAG, "onResponse: result " + result);
                        try {
                            JSONArray arr = new JSONArray(result);
                            JSONObject js = arr.getJSONObject(0);
                            String status = js.getString("message");
                            int id = js.getInt("id");
                            if (status.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                                loginStatus.loginSuccess(id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loginStatus.loginFail();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loginStatus.loginRequestError();
                    }
                }) {
                    /**
                     * @return
                     */
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(USERNAME, username);
                        params.put(PASSWORD, password);
                        return params;
                    }

                    @Override
                    public Priority getPriority() {
                        return Priority.IMMEDIATE;
                    }
                };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static final String URL_ACCEPT = "http://cupcake96uet.hol.es/api/api_accept_response.php";

    public void accept(final Context context, final int idRequest) {
        StringRequest stringRequest =
                new StringRequest(Request.Method.POST, URL_ACCEPT, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response.toString();
                        try {
                            JSONArray arr = new JSONArray(result);
                            JSONObject js = arr.getJSONObject(0);
                            String status = js.getString("message");
                            int id = js.getInt("id");
                            if (status.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                                ToastUtils.quickToast(context, "Đã gửi yêu cầu chấp nhận.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loginStatus.loginFail();
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
                        params.put("idRequest", idRequest + "");
                        params.put("value", 1 + "");
                        return params;
                    }

                    @Override
                    public Priority getPriority() {
                        return Priority.IMMEDIATE;
                    }
                };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    private Listener.registerStatus registerStatus;
    public static final String URL_REGISTER = "http://datuet.esy.es/api/api_create_patient.php";
    private static String BIRTHDAY = "birthday";
    private static String GENDER = "gender";
    private static String IDENTITY_NUMBER = "identityNumber";
    private static String INSURANCE_CODE = "insuranceCode";
    private static String ADDRESS = "address";
    private static String NAME = "name";

    public void register(final Context context, final PatientObject patientObject) {
        StringRequest stringRequest =
                new StringRequest(Request.Method.POST, URL_REGISTER, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response.toString();
                        try {
                            JSONArray arr = new JSONArray(result);
                            JSONObject js = arr.getJSONObject(0);
                            String status = js.getString("message");
                            int id = js.getInt("id");
                            if (status.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                                registerStatus.registerSuccess(id);
                            } else if (status.equalsIgnoreCase(RESPONSE_FAIL)) {
                                registerStatus.registerExist();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        registerStatus.registerFail();
                    }
                }) {
                    /**
                     * @return
                     */
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put(USERNAME, patientObject.getUserName());
                        params.put(PASSWORD, patientObject.getPassWord());
                        params.put(BIRTHDAY, patientObject.getBirthDay());
                        params.put(GENDER, patientObject.getGender() + "");
                        params.put(IDENTITY_NUMBER, patientObject.getIdentityNumber());
                        params.put(INSURANCE_CODE, patientObject.getInsuranceCode());
                        params.put(ADDRESS, patientObject.getAddress());
                        params.put(NAME, patientObject.getName());
                        return params;
                    }

                    @Override
                    public Priority getPriority() {
                        return Priority.IMMEDIATE;
                    }
                };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    public void setLoginStatus(Listener.loginStatus loginStatus) {
        this.loginStatus = loginStatus;
    }

    public void setRegisterStatus(Listener.registerStatus registerStatus) {
        this.registerStatus = registerStatus;
    }

    private Listener.requestStatus requestStatus;

    public void sendRequest(final Context context, final RequestObject requestObject) {
        StringRequest strReq =
                new StringRequest(Request.Method.POST, URL_SEND_REQUEST, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String result = response.toString();
//                            Log.i(TAG, "onResponse: result + " + result);
                            JSONArray arr = new JSONArray(result);
                            JSONObject js = arr.getJSONObject(0);
                            String status = js.getString("message");
                            int id = js.getInt("id");
                            if (status.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                                requestStatus.requestSuccess(id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestStatus.requestErrorResponse();
                    }
                }) {
                    /**
                     * @return
                     */
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(ID_PATIENT, requestObject.getIdPatient() + "");
                        params.put(SYMPTOM, requestObject.getSymptom());
                        params.put(REQUEST_TIME, requestObject.getRequestTime());
                        params.put(ID_FACULTY, requestObject.getIdFaculty() + "");
                        params.put(DAY_TARGET, requestObject.getDayTarget());
                        params.put(CHECKED, requestObject.getChecked() + "");
                        return params;
                    }

                    @Override
                    public Priority getPriority() {
                        return Priority.IMMEDIATE;
                    }
                };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(strReq);
    }

    public void setRequestStatus(Listener.requestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}
