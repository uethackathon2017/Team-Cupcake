package cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.R;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.Util.Constants;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.Util.PostDataUtils;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.Util.ToastUtils;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.Utils;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.listener.Listener;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.object.PatientObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment
        extends BaseFragment
        implements Listener.registerStatus {

    private AppCompatEditText edtRegisterIdentity;
    private AppCompatEditText edtRegisterInsurance;
    private AppCompatEditText edtRegisterAddress;
    private RadioGroup mRadioGroup;
    private TextView btnRegister;
    private ProgressDialog progressDialog;





    private ImageView imgBack;


    private String name, userName, pass, birthday, address, identityNumber, insuranceCode;
    private int gender;

    private Listener.listenerLogin listenerLogin;

    public InformationFragment(String name, String userName, String pass, String birthday) {
        this.name = name;
        this.userName = userName;
        this.pass = pass;
        this.birthday = birthday;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_information;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, View rootView) {
        findViews(rootView);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Waiting...");
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                insuranceCode = edtRegisterInsurance.getText().toString();
                identityNumber = edtRegisterIdentity.getText().toString();
                address = edtRegisterAddress.getText().toString();
                int id = mRadioGroup.getCheckedRadioButtonId();
                if (id == R.id.rbMale) {
                    gender = 1;
                } else {
                    gender = 0;
                }
                if (insuranceCode.matches("")
                        || identityNumber.matches("")
                        || address.matches("")) {
                    ToastUtils.quickToast(getActivity(), "Please input missing");
                }
                PostDataUtils postDataUtils = new PostDataUtils();
                postDataUtils.setRegisterStatus(InformationFragment.this);
                postDataUtils.register(getActivity(),
                        new PatientObject(name,
                                gender,
                                identityNumber,
                                insuranceCode,
                                address,
                                userName,
                                birthday,
                                pass));
            }
        });
    }

    private void findViews(View rootView) {
        edtRegisterIdentity = (AppCompatEditText) rootView.findViewById(R.id.edtRegisterIdentity);
        edtRegisterInsurance = (AppCompatEditText) rootView.findViewById(R.id.edtRegisterInsurance);
        edtRegisterAddress = (AppCompatEditText) rootView.findViewById(R.id.edtRegisterAddress);
        mRadioGroup = (RadioGroup) rootView.findViewById(R.id.rdGender);

        imgBack = (ImageView) rootView.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerLogin.showRegister();
            }
        });
        //inputRegisterGender = (TextInputLayout) rootView.findViewById(R.id.inputRegisterGender);
        //edtRegisterGender = (AppCompatEditText) rootView.findViewById(R.id.edtRegisterGender);

        btnRegister = (TextView) rootView.findViewById(R.id.btnRegister);
    }

    @Override
    public void registerSuccess(int id) {
        progressDialog.hide();
        Utils.setValueToPreferences(Constants.PREFERENCES_LOGIN_ID, id + "", getActivity());
        Utils.setValueToPreferences(Constants.PREFERENCES_LOGIN, Constants.LOGIN_TRUE, getActivity());
        listenerLogin.startMain();
    }

    @Override
    public void registerExist() {
        progressDialog.hide();
    }

    @Override
    public void registerFail() {
        progressDialog.hide();
    }

    public void setListenerLogin(Listener.listenerLogin listenerLogin) {
        this.listenerLogin = listenerLogin;
    }
}
