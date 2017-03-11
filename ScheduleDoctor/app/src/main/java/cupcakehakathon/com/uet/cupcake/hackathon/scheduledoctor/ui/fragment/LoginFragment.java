package cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.R;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.listener.Listener;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils.Constants;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils.PostDataUtils;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils.ToastUtils;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils.Utils;

/**
 * Created by Luong Tran on 3/10/2017.
 */

public class LoginFragment
        extends BaseFragment
        implements Listener.loginStatus {

    private TextInputLayout inputUsername;
    private AppCompatEditText edtUsername;
    private TextInputLayout inputPassword;
    private AppCompatEditText edtPassword;
    private TextView btnLogin;
    private TextView btnSignUp;
    private ImageView imgAvatar;


    private Listener.listenerLogin listenerLogin;

    @Override
    protected int getLayoutResource() {
        return R.layout.frament_login;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, View rootView) {
        findViews(rootView);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        imgAvatar.setImageResource(R.drawable.schedule_doctor);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLogin();
            }
        });
    }

    public void clickLogin() {
        String name = edtUsername.getText().toString();
        String pass = edtPassword.getText().toString();
        if (name.matches("")) {
            ToastUtils.quickToast(getActivity(), "Enter your name");
        } else if (pass.matches("")) {
            ToastUtils.quickToast(getActivity(), "Enter pass");
        } else {
            PostDataUtils postDataUtils = new PostDataUtils();
            postDataUtils.setLoginStatus(this);
            postDataUtils.login(getActivity(), name, pass);
            ToastUtils.quickToast(getActivity(), "success");
        }
    }

    private void findViews(View rootView) {
        inputUsername = (TextInputLayout) rootView.findViewById(R.id.inputUsername);
        edtUsername = (AppCompatEditText) rootView.findViewById(R.id.edtUsername);
        inputPassword = (TextInputLayout) rootView.findViewById(R.id.inputPassword);
        edtPassword = (AppCompatEditText) rootView.findViewById(R.id.edtPassword);
        btnLogin = (TextView) rootView.findViewById(R.id.btnLogin);
        btnSignUp = (TextView) rootView.findViewById(R.id.btnSignUp);
        imgAvatar = (ImageView) rootView.findViewById(R.id.imgAvatar);
    }

    @Override
    public void loginSuccess(int id, int idFaculty) {
        Utils.setValueToPreferences(Constants.PREFERENCES_LOGIN,
                Constants.LOGIN_TRUE,
                getActivity());
        Utils.setValueToPreferences(Constants.PREFERENCES_LOGIN_ID, id + "", getActivity());
        Utils.setValueToPreferences(Constants.PREFERENCES_ID_FACULTY,
                idFaculty + "",
                getActivity());
        listenerLogin.startMain();
    }

    @Override
    public void loginFail() {
        ToastUtils.quickToast(getActivity(), "Some thing wrong. Please try again");
    }

    private void forgotPassword() {
        // do something
    }

    public void clickForgetPass() {
        ToastUtils.quickToast(getActivity(), "Fixing ...");
    }

    public void setListenerLogin(Listener.listenerLogin listenerLogin) {
        this.listenerLogin = listenerLogin;
    }
}
