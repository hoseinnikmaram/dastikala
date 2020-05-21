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

import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.adapter.DataAdapterMyPoster;
import com.dastsaz.dastsaz.models.PosterModel;
import com.dastsaz.dastsaz.network.FakeDataProvider;
import com.dastsaz.dastsaz.network.FakeDataService;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;
import com.dastsaz.dastsaz.utility.myClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by m.hosein on 4/22/2018.
 */

public class MarkActivity extends AppCompatActivity {
    private Bundle args;
    private DataAdapterMyPoster mAdapter;
    private FakeDataService mTService;
    private RecyclerView mRylist;
    private FakeDataProvider provider;
    private String IdUser;
    private TextView tx;
    private AppPreferenceTools mAppPreferenceTools;
    private List<PosterModel> posterModel = Collections.emptyList();
    private AppCompatImageButton mImEdit;

    private SwipeRefreshLayout swipeRefreshLayout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_myposter);
        //get argument and check is in edit mode
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiprefreshmyopster);
        mAppPreferenceTools = new AppPreferenceTools(this);


        args = getIntent().getExtras();

        tx = (TextView) findViewById(R.id.txt_empty);

        mRylist = (RecyclerView) findViewById(R.id.list_recycle_myposter);
        mRylist.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mAdapter = new DataAdapterMyPoster(true,getBaseContext(), new DataAdapterMyPoster.DataEventHandler() {
            @Override
            public void onDetailData(String PosterId, int position) {
                Intent DetailIntent = new Intent(getBaseContext(), DetailPoster.class);

                DetailIntent.putExtra("ID_KEY", PosterId);
                startActivityForResult(DetailIntent, 7);
            }

            @Override
            public void onEdit(String tweetId, int position, AppCompatImageButton mImEdit) {

                mImEdit.setVisibility(View.GONE);

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
        AlertDialog.Builder Alert_close = new AlertDialog.Builder(MarkActivity.this);
        Alert_close.setTitle("هشدار")
                .setMessage("آیا قصد حذف دارید؟")
                .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mAppPreferenceTools.removeFavorite(Id);
                        getPosterFromServer();

                    }
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

        posterModel = new ArrayList<PosterModel>();
        posterModel=mAppPreferenceTools.getFavorites();
        if(posterModel==null){
            tx.setVisibility(View.VISIBLE);
            tx.setText("هیچ آگهی نشان نشده است");
            swipeRefreshLayout.setRefreshing(false);
        }

        else if (posterModel.size()==0){
            tx.setVisibility(View.VISIBLE);
            tx.setText("هیچ آگهی نشان نشده است");
            mAdapter.updateAdapterData(posterModel);
            mAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }


        else {
            mAdapter.updateAdapterData(posterModel);
            mAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
