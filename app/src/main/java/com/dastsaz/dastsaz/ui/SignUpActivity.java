package com.dastsaz.dastsaz.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.models.AuthenticationResponseModel;
import com.dastsaz.dastsaz.models.ErrorModel;
import com.dastsaz.dastsaz.models.SignUpRequestModel;
import com.dastsaz.dastsaz.network.FakeDataProvider;
import com.dastsaz.dastsaz.network.FakeDataService;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;
import com.dastsaz.dastsaz.utility.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private AppCompatEditText mETxName;
    private AppCompatEditText mETxEmail;
    private AppCompatEditText mETxPassword;
    private AppCompatEditText mETxphone;
    private MaterialDialog builder;
    private AppPreferenceTools mAppPreferenceTools;
    private ProgressDialog mProgressDialoginsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mETxName = (AppCompatEditText) findViewById(R.id.etx_name);
        mETxEmail = (AppCompatEditText) findViewById(R.id.etx_emailsignup);
        mETxPassword = (AppCompatEditText) findViewById(R.id.etx_password2);
        mETxphone = (AppCompatEditText) findViewById(R.id.etx_phon);
        mProgressDialoginsert = new ProgressDialog(this);
        mProgressDialoginsert.setCancelable(false);
        mProgressDialoginsert.setCanceledOnTouchOutside(false);
        mAppPreferenceTools = new AppPreferenceTools(this);

        AppCompatButton btnSignUp = (AppCompatButton) findViewById(R.id.btn_sign_up);
        if (btnSignUp != null) {
            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //check fields not empty
                    if (mETxName.getText().toString().trim().length() > 1 ) {
                        if (mETxEmail.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                            if(mETxphone.getText().toString().trim().matches("(\\+98|0)?9\\d{9}")) {
                                if ( mETxPassword.getText().toString().trim().length() > 3) {
                                    //sign up the new user
                                    mProgressDialoginsert.setMessage("منتظر بمانید...");
                                    mProgressDialoginsert.show();
                                    SignUpRequestModel requestModel = new SignUpRequestModel();
                                    requestModel.email = mETxEmail.getText().toString();
                                    requestModel.name = mETxName.getText().toString();
                                    requestModel.password = mETxPassword.getText().toString();
                                    requestModel.phone = mETxphone.getText().toString().trim() + "";
                                    //provide service
                                    FakeDataProvider provider = new FakeDataProvider();
                                    FakeDataService tService = provider.getTService();
                                    //make call
                                    Call<AuthenticationResponseModel> call = tService.signUp(requestModel);
                                    call.enqueue(new Callback<AuthenticationResponseModel>() {
                                        @Override
                                        public void onResponse(Call<AuthenticationResponseModel> call, Response<AuthenticationResponseModel> response) {
                                            if (response.isSuccessful()&&response.body().description==null ) {
                                                //save the authorization information into pref
                                                if (mProgressDialoginsert.isShowing()) {
                                                    mProgressDialoginsert.dismiss();
                                                }
                                                AppPreferenceTools appPreferenceTools = new AppPreferenceTools(getBaseContext());
                                                appPreferenceTools.saveUserAuthenticationInfo(response.body());
                                                //navigate to main activity
                                                startActivity(new Intent(getBaseContext(), SmsVerification.class));
                                                //finish this activity
                                                finish();
                                            } else {
                                                if (mProgressDialoginsert.isShowing()) {
                                                    mProgressDialoginsert.dismiss();
                                                }
                                                Toast.makeText(getBaseContext(),   response.body().description, Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<AuthenticationResponseModel> call, Throwable t) {
                                            if (mProgressDialoginsert.isShowing()) {
                                                mProgressDialoginsert.dismiss();
                                            }
                                            //occur when fail to deserialize || no network connection || server unavailable
                                            Toast.makeText(getBaseContext(), "خطا در برقراری ارتباط", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(getBaseContext(), "رمز عبور کمتر از 4 حرف نباشد", Toast.LENGTH_LONG).show();}

                            }
                            else {
                            Toast.makeText(getBaseContext(), "شماره موبایل نامعتبر هست", Toast.LENGTH_LONG).show();}
                        }
                        else {
                            Toast.makeText(getBaseContext(), "ایمیل معتبر هست.", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getBaseContext(), "فیلد نام را وارد نمایید", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        AppCompatButton btnGoToSignIn = (AppCompatButton) findViewById(R.id.btn_go_to_sign_in);
        if (btnGoToSignIn != null) {
            btnGoToSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //navigate to sign up activity
                    startActivity(new Intent(getBaseContext(),SignInActivity.class));
                    //finish this activity
                    finish();
                }
            });
        }


    }




}
