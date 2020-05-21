package com.dastsaz.dastsaz.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.adapter.DataAdapterPoster;
import com.dastsaz.dastsaz.helper.ViewManager;
import com.dastsaz.dastsaz.models.DatePosterModel;
import com.dastsaz.dastsaz.models.ErrorModel;
import com.dastsaz.dastsaz.models.NowDateModel;
import com.dastsaz.dastsaz.models.PosterModel;
import com.dastsaz.dastsaz.network.FakeDataProvider;
import com.dastsaz.dastsaz.network.FakeDataService;
import com.dastsaz.dastsaz.ui.CreatePoster;
import com.dastsaz.dastsaz.ui.CustomViewIconTextTabsActivity;
import com.dastsaz.dastsaz.ui.DetailPoster;
import com.dastsaz.dastsaz.ui.DialogActivtyCity;
import com.dastsaz.dastsaz.ui.MainActivity;
import com.dastsaz.dastsaz.ui.MarkActivity;
import com.dastsaz.dastsaz.ui.OnLoadMoreListener;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;
import com.dastsaz.dastsaz.utility.Constants;
import com.dastsaz.dastsaz.utility.ErrorUtils;
import com.dastsaz.dastsaz.utility.myClass;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FourFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ViewManager.CustomView{
    private DataAdapterPoster mAdapter;
    private FakeDataService mTService;
    private RecyclerView mRylist;
    private  Toolbar toolbar;
    private FakeDataProvider provider;
    public static SwipeRefreshLayout swipeRefreshLayoutFour;
    private AppPreferenceTools mAppPreferenceTools;
    private TextView title;
    private TextView city;
    private MaterialDialog builder;
    int last,page=0;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager;
    private List<PosterModel> mData = Collections.emptyList();
    public ViewManager mViewManager;
    private boolean stop=false;
    public FourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_four, container, false);
        // Inflate the layout for this fragment

        swipeRefreshLayoutFour = (SwipeRefreshLayout) rootView.findViewById(R.id.main_page);
        title = (TextView) rootView.findViewById(R.id.tx_home);
        city = (TextView) rootView.findViewById(R.id.txt_city);
        mAppPreferenceTools = new AppPreferenceTools(getActivity().getBaseContext());
        city.setText(mAppPreferenceTools.gettxtcity());
        myClass.textview_face(getActivity().getBaseContext(),"IRANSans",title,city);

        mViewManager = new ViewManager(getActivity());
        mViewManager.setLoading(rootView.findViewById(R.id.loading));
        mViewManager.setNoLoading(rootView.findViewById(R.id.main_page));
        mViewManager.setError(rootView.findViewById(R.id.error));
        mViewManager.setEmpty(rootView.findViewById(R.id.empty));
        mViewManager.setCustumizeView(this);




        //config recycler view
        mRylist = (RecyclerView) rootView.findViewById(R.id.list_recycle);
        mLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        mRylist.setLayoutManager(mLayoutManager);
        mAdapter = new DataAdapterPoster(mRylist,getActivity().getBaseContext(), new DataAdapterPoster.DataEventHandler() {
            @Override
            public void onDetailData(String PosterId,String date, int position) {
                Intent DetailIntent = new Intent(getActivity(), DetailPoster.class);
                DetailIntent.putExtra("Date", date);
                DetailIntent.putExtra("ID_KEY", PosterId);
                startActivityForResult(DetailIntent,7);
                getActivity().overridePendingTransition(R.anim.splash, R.anim.splesh_out);
            }


        });




        city.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        builder = new MaterialDialog.Builder(getActivity())
                                                .title(R.string.titlecity)
                                                .itemsGravity(GravityEnum.END)

                                                //  .customView(R.layout.dialog_row, true)
                                                .buttonsGravity(GravityEnum.CENTER)
                                                .items(R.array.preference_defult)
                                                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                                    @Override
                                                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                                        /**
                                                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                                         * returning false here won't allow the newly selected radio button to actually be selected.
                                                         **/
                                                        if (which==0){
                                                            mAppPreferenceTools.saveidcity("0",text.toString());
                                                        }
                                                        if (which==1){

                                                           mAppPreferenceTools.saveidcity(mAppPreferenceTools.getCityDefultId(),text.toString());

                                                        }


                                                        Toast.makeText(getActivity().getBaseContext(), text.toString(), Toast.LENGTH_LONG).show();
                                                       // MainActivity main= new MainActivity();
                                                       // main.defultidcity();
                                                        Intent refresh = new Intent(getActivity().getBaseContext(), MainActivity.class);
                                                        startActivity(refresh);
                                                        getFragmentManager().popBackStack();

                                                        return true;
                                                    }
                                                })


                                                .positiveText("انتخاب")
                                                .negativeText("بی خیال")
                                                .show();

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
                                                getPosterFromServer();

                                            }
                                        }
            );





        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
    if (stop && page>0){
        Toast.makeText(getActivity(),"انتهای لیست",Toast.LENGTH_SHORT).show();

    }
    else {
        getPosterFromServer();

    }

            }
        });

        return rootView;

    }

    @Override
    public void onRefresh() {
        mAdapter.clearDataPoster();
            page=0;
            getPosterFromServer();



    }



    private void getPosterFromServer() {
        swipeRefreshLayoutFour.setRefreshing(true);

        if (mAdapter.getItemCount() == 0)
            mViewManager.loading();

        DatePosterModel dapmodel=new DatePosterModel();
        dapmodel.id_city=MainActivity.IdCity;
        dapmodel.page=page;
        Call<DatePosterModel> call = mTService.getPoster(dapmodel);
        call.enqueue(new Callback<DatePosterModel>() {
            @Override
            public void onResponse(Call<DatePosterModel> call, Response<DatePosterModel> response) {

                if (response.isSuccessful()) {

                    if (response.body().p==null && page>0){
                        stop=true;
                        return;
                    }
                    if (page == 0  && response.body().current==null) {
                        mViewManager.empty();
                        return;
                    }

                    mViewManager.showResult();

                    //update the adapter data
                    mData=response.body().p;
                    NowDateModel.current = response.body().current.toString();
                    mAdapter.updateAdapterData(mData);
                    mData.add(null);

                        mAdapter.notifyItemInserted(mData.size() - 1);

                            mData.remove(mData.size()-1);

                            mAdapter.notifyItemRemoved(mData.size());
                            mAdapter.notifyDataSetChanged();
                            mAdapter.setLoaded();

                            page = page + 1;
                            swipeRefreshLayoutFour.setRefreshing(false);

                } else {
                    ErrorModel errorModel = ErrorUtils.parseError(response);
                  //  Toast.makeText(getActivity(), "Error type is " + errorModel.type + " , description " + errorModel.description, Toast.LENGTH_SHORT).show();
                    swipeRefreshLayoutFour.setRefreshing(false);
                    Toast.makeText(getActivity(),"اتصال به اینترنت بررسی شود3",Toast.LENGTH_LONG).show();

                        mViewManager.error();
                }
            }

            @Override
            public void onFailure(Call<DatePosterModel> call, Throwable t) {
                //occur when fail to deserialize || no network connection || server unavailable
               // Toast.makeText(getActivity(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayoutFour.setRefreshing(false);
                Toast.makeText(getActivity(),"اتصال به اینترنت بررسی شود4",Toast.LENGTH_LONG).show();
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
                getPosterFromServer();

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

                getPosterFromServer();

            }
        });
    }
}
