package com.dastsaz.dastsaz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.adapter.OperationResultModel;
import com.dastsaz.dastsaz.models.ErrorModel;
import com.dastsaz.dastsaz.models.SmsModel;
import com.dastsaz.dastsaz.models.UserModel;
import com.dastsaz.dastsaz.network.FakeDataProvider;
import com.dastsaz.dastsaz.network.FakeDataService;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;
import com.dastsaz.dastsaz.utility.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAccount extends AppCompatActivity {
    TextView txname;
    TextView txphone;
    TextView txemail;
    AppPreferenceTools mAppPreferenceTools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txemail=(TextView)findViewById(R.id.txt_email);
        txphone=(TextView)findViewById(R.id.txt_phone);
        txname=(TextView)findViewById(R.id.txt_name);
        mAppPreferenceTools =new AppPreferenceTools(this);
        if (mAppPreferenceTools.isAuthorized() && mAppPreferenceTools.getsmsverification().equals("ok")) {
        FakeDataProvider provider = new FakeDataProvider();
        FakeDataService tService = provider.getTService();
        //make call
        Call<UserModel> call = tService.showuser("Bearer " + mAppPreferenceTools.getAccessToken());
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    txemail.setText(response.body().email);
                    txname.setText(response.body().name);
                    txphone.setText(response.body().phone);
                  //  Toast.makeText(getBaseContext(), response.body().description, Toast.LENGTH_SHORT).show();
                  //  mAppPreferenceTools.removeAllPrefs();
                } else {
                    Toast.makeText(getBaseContext(), response.body().description, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                //occur when fail to deserialize || no network connection || server unavailable
                Toast.makeText(getBaseContext(), "خطا در برقراری ارتباط" , Toast.LENGTH_LONG).show();

            }
        });

        }

        else {
            //the user is not logged in so should navigate to sing in activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }

    }
public void OnClickExit(View V){
    FakeDataProvider provider = new FakeDataProvider();
    FakeDataService tService = provider.getTService();
    //make call
    Call<OperationResultModel> call = tService.terminateApp("Bearer " + mAppPreferenceTools.getAccessToken());
    call.enqueue(new Callback<OperationResultModel>() {
        @Override
        public void onResponse(Call<OperationResultModel> call, Response<OperationResultModel> response) {
            if (response.isSuccessful()) {

                Toast.makeText(getBaseContext(), response.body().description, Toast.LENGTH_SHORT).show();
                mAppPreferenceTools.removeAllPrefs();
                finish();
            } else {
                Toast.makeText(getBaseContext(), response.body().description, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<OperationResultModel> call, Throwable t) {
            //occur when fail to deserialize || no network connection || server unavailable
            Toast.makeText(getBaseContext(), "خطا در برقراری ارتباط" , Toast.LENGTH_LONG).show();

        }
    });

}
}
