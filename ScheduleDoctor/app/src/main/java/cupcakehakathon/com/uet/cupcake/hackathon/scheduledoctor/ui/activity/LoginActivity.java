package cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Window;
import android.view.WindowManager;

import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.R;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.listener.Listener;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.ui.fragment.LoginFragment;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils.Constants;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils.FragmentUtils;
import cupcakehakathon.com.uet.cupcake.hackathon.scheduledoctor.utils.Utils;

/**
 * Created by Luong Tran on 3/10/2017.
 */

public class LoginActivity extends BaseActivity implements Listener.listenerLogin {

    @Override
    protected int getLayoutResource() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_login;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState) {

    }

    @Override
    protected void initData(Bundle saveInstanceState) {


        // check login
        if (Utils.getValueFromPreferences(Constants.PREFERENCES_LOGIN, this) == null) {
            Utils.setValueToPreferences(Constants.PREFERENCES_LOGIN, Constants.LOGIN_FALSE, this);
        }
        if (Utils.getValueFromPreferences(Constants.PREFERENCES_LOGIN, this).equalsIgnoreCase(Constants.LOGIN_TRUE)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        // show fragment login
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setListenerLogin(this);
        FragmentUtils.addFragment(loginFragment, R.id.frmLogin, this);


    }

    @Override
    public void showLogin() {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setListenerLogin(this);
        FragmentUtils.addFragment(loginFragment, R.id.frmLogin, this);
    }

    @Override
    public void startMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
            startActivity(intent
                    , ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
        } else {
            startActivity(intent);
        }
        finish();
    }
}
