package com.dastsaz.dastsaz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dastsaz.dastsaz.models.AuthenticationResponseModel;
import com.dastsaz.dastsaz.models.ErrorModel;
import com.dastsaz.dastsaz.models.SignInRequestModel;
import com.dastsaz.dastsaz.models.SmsModel;
import com.dastsaz.dastsaz.network.FakeDataProvider;
import com.dastsaz.dastsaz.network.FakeDataService;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;
import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.utility.ErrorUtils;
import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by m.hosein on 4/6/2018.
 */

public class SmsVerification extends AppCompatActivity {

    private EditText eTxverification;
    private CardView send;
    private String phonenumber;
    private String code="0";
    private int random=001;
    private AppPreferenceTools ap;
    private AppPreferenceTools mAppPreferenceTools;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_sms_vetification);
        Random rand = new Random();

        random = rand.nextInt(999) + 100;
       Toast.makeText(getBaseContext(), String.valueOf(random), Toast.LENGTH_LONG).show();

        eTxverification=(EditText) findViewById(R.id.etx_verification);
        send=(CardView) findViewById(R.id.card_send_verification);
        mAppPreferenceTools = new AppPreferenceTools(this);
        ap=new AppPreferenceTools(this);
       phonenumber = ap.getphonenumber();

        getVerfication();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check field are not empty

                    if (eTxverification.length() > 0 && phonenumber.length() > 0) {
                        //send information to sign in
                        code=eTxverification.getText()+"";
                        if (Integer.parseInt(code)==random) {
                            mAppPreferenceTools.savesmsverification("ok");
                            startActivity(new Intent(getBaseContext(), CreatePoster.class));
                            //finish this activity
                            finish();

                        }

                        else {

                            Toast.makeText(getBaseContext(), "کد تایید اشتباه است", Toast.LENGTH_LONG).show();

                        }


                    } else {
                        Toast.makeText(getBaseContext(), "کد تائید را وارد کنید", Toast.LENGTH_LONG).show();
                    }

            }
        });




    }

 private void getVerfication(){


         SmsModel smsModel = new SmsModel();
         smsModel.smsBody =String.valueOf(random) ;
         smsModel.to = phonenumber;
         smsModel.user_id=mAppPreferenceTools.getUserId();

   //  Toast.makeText(getBaseContext(), "ra"+String.valueOf(random)+"oo"+phonenumber, Toast.LENGTH_LONG).show();

     //provide service
         FakeDataProvider provider = new FakeDataProvider();
         FakeDataService tService = provider.getTService();
         //make call
         Call<SmsModel> call = tService.smsverfication("Bearer " + mAppPreferenceTools.getAccessToken(),smsModel);
         call.enqueue(new Callback<SmsModel>() {
             @Override
             public void onResponse(Call<SmsModel> call, Response<SmsModel> response) {
                 if (response.isSuccessful()) {

                     Toast.makeText(getBaseContext(), response.body().descrip, Toast.LENGTH_SHORT).show();

                 } else {
                     ErrorModel errorModel = ErrorUtils.parseError(response);
                     Toast.makeText(getBaseContext(), "Error type is " + errorModel.type + " , description " + errorModel.description, Toast.LENGTH_SHORT).show();
                 }
             }

             @Override
             public void onFailure(Call<SmsModel> call, Throwable t) {
                 //occur when fail to deserialize || no network connection || server unavailable
                Toast.makeText(getBaseContext(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();

             }
         });





    }
}
