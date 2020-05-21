package com.dastsaz.dastsaz.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.adapter.DataAdapterMyPoster;
import com.dastsaz.dastsaz.models.ErrorModel;
import com.dastsaz.dastsaz.models.PosterModel;
import com.dastsaz.dastsaz.network.FakeDataProvider;
import com.dastsaz.dastsaz.network.FakeDataService;
import com.dastsaz.dastsaz.ui.DetailPoster;
import com.dastsaz.dastsaz.utility.ErrorUtils;
import com.dastsaz.dastsaz.ui.DialogEditPoster;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dastsaz.dastsaz.ui.CustomViewIconTextTabsActivity.count;

/**
 * Created by m.hosein on 2/17/2018.
 */

public class MyPosterFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private DataAdapterMyPoster mAdapter;
    private FakeDataService mTService;
    private RecyclerView mRylist;
    private FakeDataProvider provider;
    private String IdUser;
    private SwipeRefreshLayout swipeRefreshLayout;

    public MyPosterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        count = 1;

        super.onResume();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myposter, container, false);
        // Inflate the layout for this fragment

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiprefreshmyopster);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            IdUser = bundle.getString("ID_User");

        }


        mRylist = (RecyclerView) rootView.findViewById(R.id.list_recycle_myposter);
        mRylist.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mAdapter = new DataAdapterMyPoster(false,getActivity().getBaseContext(), new DataAdapterMyPoster.DataEventHandler() {
            @Override
            public void onDetailData(String PosterId, int position) {
                Intent DetailIntent = new Intent(getActivity(), DetailPoster.class);

                DetailIntent.putExtra("ID_KEY", PosterId);
                startActivityForResult(DetailIntent, 7);
            }

            @Override
            public void onEdit(String tweetId, int position, AppCompatImageButton mImEdit) {

                Intent EditIntent = new Intent(getActivity(), DialogEditPoster.class);

                EditIntent.putExtra("ID_Poster_forEdit", tweetId);
                startActivityForResult(EditIntent,7);



            }

            @Override
            public void onDelete(String Id, int position) {
                mesg_box_exit(Id);

            }


        });



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
        );        return rootView;
    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 7 && resultCode == -1) {

            getPosterFromServer();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRefresh() {
        getPosterFromServer();
        swipeRefreshLayout.setRefreshing(false);

    }


    public void mesg_box_exit(final String Id)
    {
        AlertDialog.Builder Alert_close = new AlertDialog.Builder(getActivity());
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
                                   Toast.makeText(getActivity().getBaseContext(), response.body().type, Toast.LENGTH_SHORT).show();
                                    getPosterFromServer();
                                } else {
                                    ErrorModel errorModel = ErrorUtils.parseError(response);
                                    Toast.makeText(getActivity().getBaseContext(), "Error type is " + errorModel.type + " description " + errorModel.description, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ErrorModel> call, Throwable t) {
                                //occur when fail to deserialize || no network connection || server unavailable
                                Toast.makeText(getActivity().getBaseContext(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
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
                    swipeRefreshLayout.setRefreshing(false);

                } else {
                    ErrorModel errorModel = ErrorUtils.parseError(response);
                      Toast.makeText(getActivity(), "Error type is " + errorModel.type + " , description " + errorModel.description, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PosterModel>> call, Throwable t) {
                //occur when fail to deserialize || no network connection || server unavailable
                 Toast.makeText(getActivity(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }


}
