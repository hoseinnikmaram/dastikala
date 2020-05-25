package com.dastsaz.dastsaz.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.adapter.DataAdapterPoster;
import com.dastsaz.dastsaz.models.ErrorModel;
import com.dastsaz.dastsaz.models.PosterModel;
import com.dastsaz.dastsaz.network.FakeDataProvider;
import com.dastsaz.dastsaz.network.FakeDataService;
import com.dastsaz.dastsaz.ui.CreatePoster;
import com.dastsaz.dastsaz.ui.DetailPoster;
import com.dastsaz.dastsaz.ui.MainActivity;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;
import com.dastsaz.dastsaz.utility.Constants;
import com.dastsaz.dastsaz.utility.ErrorUtils;
import com.dastsaz.dastsaz.utility.myClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.dastsaz.dastsaz.ui.CustomViewIconTextTabsActivity.count;


public class dasteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private DataAdapterPoster mAdapter;
    private FakeDataService mTService;
    private RecyclerView mRylist;
    private Toolbar toolbar;
    private FakeDataProvider provider;
    private Bundle args;
    private AppCompatButton btn_mobile;
    private AppCompatButton btn_laptop;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView title;
    private TextView city;
    private String IdInEditMode;
    private String NameGroup;

    public dasteFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_daste, container, false);
        // Inflate the layout for this fragment

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiprefreshdaste);
        title = (TextView) rootView.findViewById(R.id.tx_group);
        city = (TextView) rootView.findViewById(R.id.txt_city);
        AppPreferenceTools mAppPreferenceTools = new AppPreferenceTools(getActivity().getBaseContext());
        city.setText(mAppPreferenceTools.gettxtcity());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            IdInEditMode = bundle.getString("ID_Group");
            NameGroup = bundle.getString("Name_Group");

        }
        title.setText(NameGroup);
        mRylist = (RecyclerView) rootView.findViewById(R.id.list_recycle_group);
        mRylist.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mAdapter = new DataAdapterPoster(mRylist, getActivity().getBaseContext(), new DataAdapterPoster.DataEventHandler() {
            @Override
            public void onDetailData(String PosterId, String date, int position) {
                Intent DetailIntent = new Intent(getActivity(), DetailPoster.class);
                DetailIntent.putExtra("Date", date);

                DetailIntent.putExtra("ID_KEY", PosterId);
                startActivityForResult(DetailIntent, 7);
            }


        });

        btn_mobile = (AppCompatButton) rootView.findViewById(R.id.btn_mobile);
        btn_laptop = (AppCompatButton) rootView.findViewById(R.id.btn_laptop);


        if (IdInEditMode.equals("1")){


        btn_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SubDasteFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ID_Group", "1");
                bundle.putString("Name_Group", "دیجیتال");
                bundle.putString("Sub_Group", "1");
                bundle.putString("Sub_Name", "موبایل");

                String backStateName = fragment.getClass().getName();

                fragment.setArguments(bundle);
                boolean fragmentPopped = getActivity().getSupportFragmentManager().popBackStackImmediate(backStateName, 0);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                if (!fragmentPopped) { //fragment not in back stack, create it.
                    ft.replace(R.id.ly_daste, fragment);
                    ft.addToBackStack(backStateName);
                    ft.commit();
                }

            }
        });


        btn_laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SubDasteFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ID_Group", "1");
                bundle.putString("Name_Group", "دیجتال");
                bundle.putString("Sub_Group", "2");
                bundle.putString("Sub_Name", "لپ تاپ");

                String backStateName = fragment.getClass().getName();

                fragment.setArguments(bundle);
                boolean fragmentPopped = getActivity().getSupportFragmentManager().popBackStackImmediate(backStateName, 0);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                if (!fragmentPopped) { //fragment not in back stack, create it.
                    ft.replace(R.id.ly_daste, fragment);
                    ft.addToBackStack(backStateName);
                    ft.commit();
                }

            }
        });


        //config recycler view




    }


        else if (IdInEditMode.equals("2")){
            btn_mobile.setText("تلوزیون");
            btn_laptop.setText("جاروبرقی");


            btn_mobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new SubDasteFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("ID_Group", "2");
                    bundle.putString("Name_Group", "لوازم خانگی");
                    bundle.putString("Sub_Group", "3");
                    bundle.putString("Sub_Name", "تلوزیون");

                    String backStateName = fragment.getClass().getName();

                    fragment.setArguments(bundle);
                    boolean fragmentPopped = getActivity().getSupportFragmentManager().popBackStackImmediate(backStateName, 0);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    if (!fragmentPopped) { //fragment not in back stack, create it.
                        ft.replace(R.id.ly_daste, fragment);
                        ft.addToBackStack(backStateName);
                        ft.commit();
                    }

                }
            });


            btn_laptop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new SubDasteFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("ID_Group", "2");
                    bundle.putString("Name_Group", "لوازم خانگی");
                    bundle.putString("Sub_Group", "4");
                    bundle.putString("Sub_Name", "جاروبرقی");

                    String backStateName = fragment.getClass().getName();

                    fragment.setArguments(bundle);
                    boolean fragmentPopped = getActivity().getSupportFragmentManager().popBackStackImmediate(backStateName, 0);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    if (!fragmentPopped) { //fragment not in back stack, create it.
                        ft.replace(R.id.ly_daste, fragment);
                        ft.addToBackStack(backStateName);
                        ft.commit();
                    }

                }
            });

        }

        mRylist.setAdapter(mAdapter);
        //get data in load
        provider = new FakeDataProvider();

        mTService = provider.getTService();


        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        getPosterFromServer();

                                    }
                                }
        );

        myClass.textview_face(getActivity().getBaseContext(), "IRANSans", title, city);

        return rootView;

    }

    @Override
    public void onRefresh() {

        getPosterFromServer();

    }

    private void getPosterFromServer() {

        Call<List<PosterModel>> call = mTService.getPosterbyGroupid(IdInEditMode, MainActivity.IdCity);
        call.enqueue(new Callback<List<PosterModel>>() {
            @Override
            public void onResponse(Call<List<PosterModel>> call, Response<List<PosterModel>> response) {

                if (response.isSuccessful()) {
                    //update the adapter data
                    mAdapter.updateAdapterData(response.body());
                    mAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);

                } else {
                    swipeRefreshLayout.setRefreshing(false);

                    ErrorModel errorModel = ErrorUtils.parseError(response);
                    //  Toast.makeText(getActivity(), "Error type is " + errorModel.type + " , description " + errorModel.description, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PosterModel>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);

                //occur when fail to deserialize || no network connection || server unavailable
                // Toast.makeText(getActivity(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }



}




