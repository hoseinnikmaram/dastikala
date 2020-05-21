package com.dastsaz.dastsaz.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.adapter.DataAdapterPoster;
import com.dastsaz.dastsaz.adapter.DataAdaterViews;
import com.dastsaz.dastsaz.helper.ViewManager;
import com.dastsaz.dastsaz.models.DatePosterModel;
import com.dastsaz.dastsaz.models.ErrorModel;
import com.dastsaz.dastsaz.models.NowDateModel;
import com.dastsaz.dastsaz.models.PosterModel;
import com.dastsaz.dastsaz.models.ViewModel;
import com.dastsaz.dastsaz.network.FakeDataProvider;
import com.dastsaz.dastsaz.network.FakeDataService;
import com.dastsaz.dastsaz.ui.MainActivity;
import com.dastsaz.dastsaz.ui.OnLoadMoreListener;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;
import com.dastsaz.dastsaz.utility.ErrorUtils;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressLint("ValidFragment")
public class viewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ViewManager.CustomView{
    private DataAdaterViews mAdapter;
    private FakeDataService mTService;
    private RecyclerView mRylist;
    private  Toolbar toolbar;
    private FakeDataProvider provider;
    public static SwipeRefreshLayout swipeRefreshLayoutFour;
    private AppPreferenceTools mAppPreferenceTools;

    int last,page=0;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager;
    private List<ViewModel> mData = Collections.emptyList();
    public ViewManager mViewManager;
    private boolean stop=false;
    private String idposter;
    @SuppressLint("ValidFragment")
    public viewFragment(String idInEditMode) {
        idposter=idInEditMode;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_view, container, false);
        // Inflate the layout for this fragment

        swipeRefreshLayoutFour = (SwipeRefreshLayout) rootView.findViewById(R.id.main_page);

        mAppPreferenceTools = new AppPreferenceTools(getActivity().getBaseContext());


        mViewManager = new ViewManager(getActivity());
        mViewManager.setLoading(rootView.findViewById(R.id.loading));
        mViewManager.setNoLoading(rootView.findViewById(R.id.main_page));
        mViewManager.setError(rootView.findViewById(R.id.error));
        mViewManager.setEmpty(rootView.findViewById(R.id.empty));
        mViewManager.setCustumizeView(this);

        mAppPreferenceTools = new AppPreferenceTools(getActivity().getBaseContext());



        //config recycler view
        mRylist = (RecyclerView) rootView.findViewById(R.id.list_recycle);
        mLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        mRylist.setLayoutManager(mLayoutManager);
        mAdapter = new DataAdaterViews(getActivity().getBaseContext(), new DataAdaterViews.DataEventHandler() {

            @Override
            public void onDetailData(String CityId, int position) {

            }



        });





        swipeRefreshLayoutFour.setOnRefreshListener(this);
        swipeRefreshLayoutFour.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary
        );

        mRylist.setAdapter(mAdapter);
        provider = new FakeDataProvider();

        mTService = provider.getTService();


            mRylist.setHasFixedSize(false);


            swipeRefreshLayoutFour.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                getViewFromServer();

                                            }
                                        }
            );


        mRylist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();


                    if( totalItemCount >=5){
                        final int lastItem=pastVisiblesItems+visibleItemCount;
                        if(lastItem == totalItemCount){
                            if(last!=lastItem){

                                last=lastItem;
                                page = page +1;
                                getViewFromServer();
                            }
                        }
                    }

                }
            }
        });







        return rootView;

    }

    @Override
    public void onRefresh() {

    }



    private void getViewFromServer() {
        swipeRefreshLayoutFour.setRefreshing(true);

        if (mAdapter.getItemCount() == 0)
            mViewManager.loading();

        ViewModel viewmodel=new ViewModel();
        viewmodel.poster_id=idposter;
        viewmodel.page=page;

        Call<List<ViewModel>> call = mTService.getviews(viewmodel);
        call.enqueue(new Callback<List<ViewModel>>() {
            @Override
            public void onResponse(Call<List<ViewModel>> call, Response<List<ViewModel>> response) {

                if (response.isSuccessful()) {


                    if (page == 0  && response.body().size()==0) {
                        mViewManager.empty();
                        return;
                    }

                    mViewManager.showResult();

                    //update the adapter data
                    mData=response.body();
                    mAdapter.updateAdapterData(mData);

                    mAdapter.notifyDataSetChanged();

                    swipeRefreshLayoutFour.setRefreshing(false);

                } else {
                    ErrorModel errorModel = ErrorUtils.parseError(response);
             //       Toast.makeText(getActivity(), "Error type is " + errorModel.type + " , description " + errorModel.description, Toast.LENGTH_SHORT).show();
                    swipeRefreshLayoutFour.setRefreshing(false);
                   Toast.makeText(getActivity().getBaseContext(),"اتصال به اینترنت بررسی شود3",Toast.LENGTH_LONG).show();

                        mViewManager.error();
                }
            }

            @Override
            public void onFailure(Call<List<ViewModel>> call, Throwable t) {
                //occur when fail to deserialize || no network connection || server unavailable
               // Toast.makeText(getActivity(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayoutFour.setRefreshing(false);
                Toast.makeText(getActivity().getBaseContext(),"اتصال به اینترنت بررسی شود4",Toast.LENGTH_LONG).show();
                    mViewManager.error();
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }


    @Override
    public void customErrorView(View v) {
        Button retry = (Button) v.findViewById(R.id.btn);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewManager.loading();
                getViewFromServer();

            }
        });

    }

    @Override
    public void customLoadingView(View v) {

    }

    @Override
    public void customEmptyView(View v) {
        Button btn = (Button) v.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewManager.loading();

                getViewFromServer();

            }
        });
    }
}
