package com.dastsaz.dastsaz.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.adapter.DataAdapterPoster;
import com.dastsaz.dastsaz.models.DatePosterModel;
import com.dastsaz.dastsaz.models.ErrorModel;
import com.dastsaz.dastsaz.models.NowDateModel;
import com.dastsaz.dastsaz.models.PosterModel;
import com.dastsaz.dastsaz.network.FakeDataProvider;
import com.dastsaz.dastsaz.network.FakeDataService;
import com.dastsaz.dastsaz.ui.DetailPoster;
import com.dastsaz.dastsaz.ui.MainActivity;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;
import com.dastsaz.dastsaz.utility.ErrorUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private DataAdapterPoster mAdapter;
    private FakeDataService mTService;
    private RecyclerView mRylist;
    private Toolbar toolbar;
    private FakeDataProvider provider;
    private AppCompatEditText tx_search;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AppPreferenceTools mAppPreferenceTools;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_search, container, false);
        // Inflate the layout for this fragment
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        tx_search = (AppCompatEditText) rootView.findViewById(R.id.tx_search);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiprefreshsearch);

        //config recycler view
        mRylist = (RecyclerView) rootView.findViewById(R.id.list_recycle_search);
        mRylist.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mAdapter = new DataAdapterPoster(mRylist,getActivity().getBaseContext(), new DataAdapterPoster.DataEventHandler() {
            @Override
            public void onDetailData(String PosterId,String date ,int position) {
                Intent DetailIntent = new Intent(getActivity(), DetailPoster.class);
                DetailIntent.putExtra("Date", date);
                DetailIntent.putExtra("ID_KEY", PosterId);
                startActivityForResult(DetailIntent,7);
            }


        });
        mRylist.setAdapter(mAdapter);
        //get data in load
        provider = new FakeDataProvider();

        mTService = provider.getTService();


        getPosterFromServer();


        tx_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                getSearchPosterFromServer(tx_search.getText().toString());
                if(s.length() == 0){

                }
            }
        });

        return rootView;
    }

    @Override
    public void onRefresh() {
       // getPosterFromServer();

    }



    private void getSearchPosterFromServer(String s) {
        mAdapter.clearDataPoster();
        Call<List<PosterModel>> call = mTService.getPosterBytitle(s);
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
                  //  Toast.makeText(getActivity(), "Error type is " + errorModel.type + " , description " + errorModel.description, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PosterModel>> call, Throwable t) {
                //occur when fail to deserialize || no network connection || server unavailable
              //  Toast.makeText(getActivity(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }


    private void getPosterFromServer() {
        Call<DatePosterModel> call = mTService.getPosters(MainActivity.IdCity);
        call.enqueue(new Callback<DatePosterModel>() {
            @Override
            public void onResponse(Call<DatePosterModel> call, Response<DatePosterModel> response) {

                if (response.isSuccessful()) {
                    //update the adapter data
                    mAdapter.updateAdapterData(response.body().p);
                    mAdapter.notifyDataSetChanged();
                    NowDateModel.current=response.body().current.toString();
                    swipeRefreshLayout.setRefreshing(false);

                } else {
                    ErrorModel errorModel = ErrorUtils.parseError(response);
                 //   Toast.makeText(getActivity(), "Error type is " + errorModel.type + " , description " + errorModel.description, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DatePosterModel> call, Throwable t) {
                //occur when fail to deserialize || no network connection || server unavailable
              //  Toast.makeText(getActivity(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



}
