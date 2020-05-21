package com.dastsaz.dastsaz.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.models.AuthenticationResponseModel;
import com.dastsaz.dastsaz.models.ErrorModel;
import com.dastsaz.dastsaz.models.SignInRequestModel;
import com.dastsaz.dastsaz.network.FakeDataProvider;
import com.dastsaz.dastsaz.network.FakeDataService;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;
import com.dastsaz.dastsaz.utility.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private AppCompatEditText mETxphone;
    private AppCompatEditText mETxPassword;
    private AppPreferenceTools mAppPreferenceTools;
    private ProgressDialog mProgressDialoginsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mAppPreferenceTools = new AppPreferenceTools(this);
        mETxphone = (AppCompatEditText) findViewById(R.id.etx_phone_sign_in);
        mETxPassword = (AppCompatEditText) findViewById(R.id.etx_password);
        mProgressDialoginsert = new ProgressDialog(this);
        mProgressDialoginsert.setCancelable(false);
        mProgressDialoginsert.setCanceledOnTouchOutside(false);
        AppCompatButton btnSignIn = (AppCompatButton) findViewById(R.id.btn_sign_in);
        if (btnSignIn != null) {
            btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //check field are not empty
                    if (mETxphone.getText().toString().trim().matches("(\\+98|0)?9\\d{9}")) {
                        if (mETxPassword.getText().toString().trim().length() > 3) {
                            //send information to sign in
                            mProgressDialoginsert.setMessage("منتظر بمانید...");
                            mProgressDialoginsert.show();
                            SignInRequestModel signInRequestModel = new SignInRequestModel();
                            signInRequestModel.phone = mETxphone.getText().toString().trim()+ "";;
                            signInRequestModel.password = mETxPassword.getText().toString();
                            //provide service
                            FakeDataProvider provider = new FakeDataProvider();
                            FakeDataService tService = provider.getTService();
                            //make call
                            Call<AuthenticationResponseModel> call = tService.signIn(signInRequestModel);
                            call.enqueue(new Callback<AuthenticationResponseModel>() {
                                @Override
                                public void onResponse(Call<AuthenticationResponseModel> call, Response<AuthenticationResponseModel> response) {
                                    if (response.isSuccessful() &&response.body().description==null) {
                                        if (mProgressDialoginsert.isShowing()) {
                                            mProgressDialoginsert.dismiss();
                                        }
                                        AppPreferenceTools appPreferenceTools = new AppPreferenceTools(getBaseContext());
                                        appPreferenceTools.saveUserAuthenticationInfo(response.body());
                                        //navigate to main activity
                                        if (mAppPreferenceTools.getsmsverification().equals("")) {
                                            startActivity(new Intent(getBaseContext(), SmsVerification.class));
                                        } else {
                                            startActivity(new Intent(getBaseContext(), CreatePoster.class));
                                        }
                                        //finish this activity
                                        finish();
                                    } else {
                                        if (mProgressDialoginsert.isShowing()) {
                                            mProgressDialoginsert.dismiss();
                                        }
                                        Toast.makeText(getBaseContext(),    "حساب کاربری یافت نشد", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<AuthenticationResponseModel> call, Throwable t) {

                                    if (mProgressDialoginsert.isShowing()) {
                                        mProgressDialoginsert.dismiss();
                                    }
                                    //occur when fail to deserialize || no network connection || server unavailable
                                    Toast.makeText(getBaseContext(), "خطا در برقراری ارتباط" , Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else {
                            Toast.makeText(getBaseContext(), "پسورد صحیح نیست", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getBaseContext(), "شماره موبایل صحیح نیست", Toast.LENGTH_LONG).show();
                    }
                }
            });
            AppCompatButton btnGoToSignUp = (AppCompatButton) findViewById(R.id.btn_go_to_sign_up);
            if (btnGoToSignUp != null) {
                btnGoToSignUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //navigate to sign up activity
                        startActivity(new Intent(getBaseContext(),SignUpActivity.class));
                        //finish this activity
                        finish();
                    }
                });
            }
        }
    }
}
