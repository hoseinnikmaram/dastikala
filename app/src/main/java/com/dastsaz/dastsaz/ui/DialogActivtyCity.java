package com.dastsaz.dastsaz.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.models.CityModel;
import com.dastsaz.dastsaz.models.ErrorModel;
import com.dastsaz.dastsaz.network.FakeDataProvider;
import com.dastsaz.dastsaz.network.FakeDataService;
import com.dastsaz.dastsaz.adapter.DataAdaterCity;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;
import com.dastsaz.dastsaz.utility.ErrorUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by m.hosein on 1/12/2018.
 */

public class DialogActivtyCity extends AppCompatActivity {

    private DataAdaterCity mAdapter;
    private FakeDataService mTService;
    private RecyclerView mRylist;
    private Toolbar toolbar;
    private Button btncancel;
    private AppPreferenceTools mAppPreferenceTools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        final Dialog customdialog = new Dialog(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_main_dialog, null);
        customdialog.setContentView(dialogView);

        mAppPreferenceTools = new AppPreferenceTools(this);

        customdialog.setCancelable(true);


        toolbar = (Toolbar) dialogView.findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FakeDataProvider provider = new FakeDataProvider();
        mTService = provider.getTService();
        //config recycler view
        mRylist = (RecyclerView) dialogView.findViewById(R.id.ry_list);
        mRylist.setLayoutManager(new LinearLayoutManager(this));
        //btncancel =(Button) dialogView.findViewById(R.id.btn_cancel);

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customdialog.dismiss();
                finish();
            }
        });

        mAdapter = new DataAdaterCity(this, new DataAdaterCity.DataEventHandler() {

            @Override
            public void onDetailData(String CityId, int position) {

                if (getIntent().getExtras().get("ID_City") != null) {

           //     mAppPreferenceTools.saveidcity(CityId);
                    CustomViewIconTextTabsActivity.IdCity=mAppPreferenceTools.getidcity();

                }
                else {
                    CityModel.idcity = Integer.parseInt(CityId);
                }
                customdialog.dismiss();
                finish();
            }
        });



        mRylist.setAdapter(mAdapter);
        //get tweets in load
        getCityFromServer();

        customdialog.show();

    }

    /**
     * get tweets from server
     */
    private void getCityFromServer() {
        Call<List<CityModel>> call = mTService.getCitys();
        call.enqueue(new Callback<List<CityModel>>() {
            @Override
            public void onResponse(Call<List<CityModel>> call, Response<List<CityModel>> response) {

                if (response.isSuccessful()) {
                    //update the adapter data
                    mAdapter.updateAdapterData(response.body());
                    mAdapter.notifyDataSetChanged();
                } else {
                    ErrorModel errorModel = ErrorUtils.parseError(response);
                    Toast.makeText(getBaseContext(), "Error type is " + errorModel.type + " , description " + errorModel.description, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CityModel>> call, Throwable t) {
                //occur when fail to deserialize || no network connection || server unavailable
                Toast.makeText(getBaseContext(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }







}
