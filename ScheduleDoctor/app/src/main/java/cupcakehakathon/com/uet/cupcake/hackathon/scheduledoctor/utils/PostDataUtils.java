package cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.listener.Listener;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Luong Tran on 3/10/2017.
 */

public class PostDataUtils {
    private String TAG = "POST DATA";

    private static String RESPONSE_SUCCESS = "success";
    private static String RESPONSE_LOGIN_ERROR = "ERROR";
    private static String RESPONSE_FAIL = "fail";


    private Listener.loginStatus loginStatus;

    private static String USERNAME = "username";
    private static String PASSWORD = "password";
    private static String URL_LOGIN = "http://datuet.esy.es/api/api_check_login_doctor.php";

    public void login(final Context context, final String username, final String password) {
        StringRequest stringRequest =
                new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response.toString();
                        Log.i(TAG, "onResponse: login" + result);
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            JSONObject jsOb = jsonArray.getJSONObject(0);
                            String status = jsOb.getString("message");
                            int id = jsOb.getInt("id");
                            int idFaculty = jsOb.getInt("idFaculty");
                            if (status.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                                loginStatus.loginSuccess(id, idFaculty);
                            } else {
                                loginStatus.loginFail();
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

    public void setLoginStatus(Listener.loginStatus loginStatus) {
        this.loginStatus = loginStatus;
    }
}
