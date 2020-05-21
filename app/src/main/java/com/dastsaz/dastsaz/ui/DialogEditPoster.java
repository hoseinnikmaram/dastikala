package com.dastsaz.dastsaz.ui;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.models.ErrorModel;
import com.dastsaz.dastsaz.models.PosterModel;
import com.dastsaz.dastsaz.network.FakeDataProvider;
import com.dastsaz.dastsaz.network.FakeDataService;
import com.dastsaz.dastsaz.ui.CreatePoster;
import com.dastsaz.dastsaz.ui.DetailPoster;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;
import com.dastsaz.dastsaz.utility.ErrorUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dastsaz.dastsaz.ui.CustomViewIconTextTabsActivity.count;

/**
 * Created by m.hosein on 2/19/2018.
 */

public class DialogEditPoster extends AppCompatActivity {
    private String IdPoster;

    private AppCompatEditText mETxtitle;


    private AppCompatEditText mETxdescription;
    // private ArrayList<Image> menuImageList;


    private AppCompatEditText mETxphone;
    private AppCompatEditText mETxsms;
    private AppCompatEditText mETxprice;
    private AppCompatButton btnedit;
    private FakeDataService mTService;


    @Override
    public void onResume() {
        count = 1;

        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
      //  dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View rootView = getLayoutInflater().inflate(R.layout.fragment_edit_poster, null);
        dialog.setContentView(rootView);
        // Inflate the layout for this fragment
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            IdPoster = bundle.getString("ID_Poster_forEdit");

        }

        mETxdescription = (AppCompatEditText) rootView.findViewById(R.id.etx_description_edit);
        mETxtitle = (AppCompatEditText) rootView.findViewById(R.id.etx_title_edit);
        mETxsms = (AppCompatEditText) rootView.findViewById(R.id.etx_sms_edit);
        mETxprice = (AppCompatEditText) rootView.findViewById(R.id.etx_price_edit);
        btnedit = (AppCompatButton) rootView.findViewById(R.id.btn_edit_poster);

        mETxphone = (AppCompatEditText) rootView.findViewById(R.id.etx_phone_edit);

        //first create new instant of FakeDataProvider
        FakeDataProvider provider = new FakeDataProvider();
        //get the FakeDataService interface to call API routes
        mTService = provider.getTService();

        Call<PosterModel> call = mTService.getPosterbyid(IdPoster);
        call.enqueue(new Callback<PosterModel>() {
            @Override
            public void onResponse(Call<PosterModel> call, Response<PosterModel> response) {
                if (response.isSuccessful()) {
                    //bind value to fields
                    mETxtitle.setText(response.body().title);
                    mETxdescription.setText(response.body().description);
                    mETxphone.setText(response.body().phone + "");
                    mETxsms.setText(response.body().sms + "");
                    mETxprice.setText(response.body().price + "");


                    //get code point of emoji

                } else {
                    ErrorModel errorModel = ErrorUtils.parseError(response);
                    Toast.makeText(getBaseContext(), "Error type is " + errorModel.type + " , description " + errorModel.description, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PosterModel> call, Throwable t) {
                //occur when fail to deserialize || no network connection || server unavailable
                Toast.makeText(getBaseContext(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PosterModel posterModel = new PosterModel();

                //bind value to fields
                posterModel.title = mETxtitle.getText().toString();
                posterModel.description = mETxdescription.getText().toString();
                posterModel.phone = mETxphone.getText().toString() +"";
                posterModel.sms = Integer.parseInt(mETxsms.getText().toString() + "");
                posterModel.price = Integer.parseInt(mETxprice.getText().toString() + "");
                Call<ErrorModel> call = mTService.editposterbyuser(IdPoster, posterModel);
                call.enqueue(new Callback<ErrorModel>() {
                    @Override
                    public void onResponse(Call<ErrorModel> call, Response<ErrorModel> response) {
                        if (response.isSuccessful()) {
                            //bind value to fields

                            Toast.makeText(getBaseContext(), response.body().type, Toast.LENGTH_SHORT).show();
                            setResult(-1);
                            finish();

                            //get code point of emoji

                        } else {
                            ErrorModel errorModel = ErrorUtils.parseError(response);
                            Toast.makeText(getBaseContext(), "Error type is " + errorModel.type + " , description " + errorModel.description, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ErrorModel> call, Throwable t) {
                        //occur when fail to deserialize || no network connection || server unavailable
                        Toast.makeText(getBaseContext(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });


        dialog.show();
    }
}


