package com.dastsaz.dastsaz.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.adapter.DataAdapterMyPoster;
import com.dastsaz.dastsaz.models.ErrorModel;
import com.dastsaz.dastsaz.models.PosterModel;
import com.dastsaz.dastsaz.network.FakeDataProvider;
import com.dastsaz.dastsaz.network.FakeDataService;
import com.dastsaz.dastsaz.utility.ErrorUtils;
import com.dastsaz.dastsaz.utility.myClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by m.hosein on 4/18/2018.
 */

public class MyPoster extends AppCompatActivity {
    private Bundle args;
    private DataAdapterMyPoster mAdapter;
    private FakeDataService mTService;
    private RecyclerView mRylist;
    private FakeDataProvider provider;
    private String IdUser;
    private TextView tx;
    private SwipeRefreshLayout swipeRefreshLayout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_myposter);
        //get argument and check is in edit mode
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiprefreshmyopster);

        args = getIntent().getExtras();
        if (args != null) {

            IdUser = args.getString("ID_User","null");

        }

        if(IdUser.equals("null"))
        {
            Toast.makeText(getBaseContext(),"شما ابتدا باید وارد حساب کاربری شوید", Toast.LENGTH_SHORT).show();

            Intent signinIntent = new Intent(getBaseContext(), SignInActivity.class);

            startActivity(signinIntent);
            finish();
        }
         tx = (TextView) findViewById(R.id.txt_empty);

         mRylist = (RecyclerView) findViewById(R.id.list_recycle_myposter);
        mRylist.setLayoutManager(new LinearLayoutManager(getBaseContext()));
         mAdapter = new DataAdapterMyPoster(false,getBaseContext(), new DataAdapterMyPoster.DataEventHandler() {
            @Override
            public void onDetailData(String PosterId, int position) {
                Intent DetailIntent = new Intent(getBaseContext(), DetailPoster.class);

                DetailIntent.putExtra("ID_KEY", PosterId);
                startActivityForResult(DetailIntent, 7);
            }

            @Override
            public void onEdit(String tweetId, int position, AppCompatImageButton mImEdit) {

                Intent EditIntent = new Intent(getBaseContext(), DialogEditPoster.class);

                EditIntent.putExtra("ID_Poster_forEdit", tweetId);
                startActivityForResult(EditIntent, 7);


            }

            @Override
            public void onDelete(String Id, int position) {
                mesg_box_exit(Id);

            }


        });

        myClass.textview_face(this,"IRANSans",tx);


        mRylist.setAdapter(mAdapter);
        //get data in load
         provider = new FakeDataProvider();

        mTService = provider.getTService();
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        getPosterFromServer();

                                    }
                                }
        );

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 7 && resultCode == -1) {

            getPosterFromServer();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }





    public void mesg_box_exit(final String Id)
    {
        AlertDialog.Builder Alert_close = new AlertDialog.Builder(MyPoster.this);
        Alert_close.setTitle("هشدار")
                .setMessage("آیا قصد حذف دارید؟")
                .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<ErrorModel> call = mTService.deleteposterbyuser(Id);
                        //async request

                        call.enqueue(new Callback<ErrorModel>() {
                            @Override
                            public void onResponse(Call<ErrorModel> call, Response<ErrorModel> response) {
                                if (response.isSuccessful()) {
                                    //get call from server just for test
                                    Toast.makeText(getBaseContext(), response.body().type, Toast.LENGTH_SHORT).show();
                                    getPosterFromServer();
                                } else {
                                    ErrorModel errorModel = ErrorUtils.parseError(response);
                                    Toast.makeText(getBaseContext(),  errorModel.type +  errorModel.description, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ErrorModel> call, Throwable t) {
                                //occur when fail to deserialize || no network connection || server unavailable
                                Toast.makeText(getBaseContext(), "خطا در برقراری ارتباط با سرور" , Toast.LENGTH_LONG).show();
                            }
                        });                    }
                })
                .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }



    private void getPosterFromServer() {
        Call<List<PosterModel>> call = mTService.getPosterbyUserid(IdUser);
        call.enqueue(new Callback<List<PosterModel>>() {
            @Override
            public void onResponse(Call<List<PosterModel>> call, Response<List<PosterModel>> response) {

                if (response.isSuccessful()) {
                    //update the adapter data
                    mAdapter.updateAdapterData(response.body());
                    mAdapter.notifyDataSetChanged();
                    if (mAdapter.getItemCount()==0){
                        tx.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    else {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                } else {
                    //ErrorModel errorModel = ErrorUtils.parseError(response);
                    Toast.makeText(getBaseContext(),  "مشکل در نشان دادن آگهی ها", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PosterModel>> call, Throwable t) {
                //occur when fail to deserialize || no network connection || server unavailable
                Toast.makeText(getBaseContext(), "خطا در برقراری ارتباط با سرور" , Toast.LENGTH_LONG).show();
            }
        });
    }
}
