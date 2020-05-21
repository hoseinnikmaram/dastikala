package com.dastsaz.dastsaz.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.fragments.OneFragment;
import com.dastsaz.dastsaz.fragments.viewFragment;
import com.dastsaz.dastsaz.helper.CustomViewPager;
import com.dastsaz.dastsaz.helper.ViewManager;
import com.dastsaz.dastsaz.models.ErrorModel;
import com.dastsaz.dastsaz.models.PosterModel;
import com.dastsaz.dastsaz.network.FakeDataProvider;
import com.dastsaz.dastsaz.network.FakeDataService;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;
import com.dastsaz.dastsaz.utility.ErrorUtils;
import com.dastsaz.dastsaz.utility.slider;

import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.TextSliderView;
import com.glide.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by m.hosein on 1/12/2018.
 */

public class DetailPoster extends AppCompatActivity implements ViewManager.CustomView {

    private TextView mTxtitle;
    private String Call="";
    private TextView mTxdescription;
    private TextView mTxloction;
    private TextView mTxphone;
    private TextView mTxsms;
    private TextView mTxprice;
private TextView mTxDate;
    private TextView mTxdaste;
    private TextView mTxSub_daste;
    private TextView mTxcity;
    private String date;
    private String IdInEditMode;
    private FakeDataService mTService;
    CollapsingToolbarLayout collapsing;
    SliderLayout slideshow;
    private String title="جزئیات آگهی";
 private TextView tx_view_phon;
    private String u="null"; ;
    private String[] m={""};
    private RequestOptions options;
    private TextView tx_view_namedaste;
    private TextView tx_view_descriptiion;
    private Bundle args;
    private TextView tx_view_sms;
    private TextView tx_view_price;
    private RelativeLayout share;
    private String phone="";
    private String sms="";
    private ImageView imgnomark;
    private ImageView imgmark;
    private AppPreferenceTools mAppPreferenceTools;
    private PosterModel posterModel;
    public ViewManager mViewManager;

    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_poster);
        //MainActivity.a=1;MainActivity.b=1;MainActivity.c=1;MainActivity.d=1;
        mAppPreferenceTools = new AppPreferenceTools(this);
        args = getIntent().getExtras();
        if (args != null) {

            IdInEditMode = args.getString("ID_KEY");
            date = args.getString("Date","");

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.Poster_create_toolbar);
        setSupportActionBar(toolbar);


        imgnomark=(ImageView) findViewById(R.id.nomark);
        imgmark=(ImageView) findViewById(R.id.mark);
///////////////
        collapsing=(CollapsingToolbarLayout)findViewById(R.id.collapsing);
        slideshow=(SliderLayout)findViewById(R.id.slider);
        share = (RelativeLayout) findViewById(R.id.share);
////////////
        viewPager = (CustomViewPager) findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
/////////////////
        mViewManager = new ViewManager(this);
        mViewManager.setLoading(findViewById(R.id.loading));
        mViewManager.setNoLoading(findViewById(R.id.detail_poster));
        mViewManager.setError(findViewById(R.id.error));
        mViewManager.setEmpty(findViewById(R.id.empty));
        mViewManager.setCustumizeView(this);




        //first create new instant of FakeDataProvider
        FakeDataProvider provider = new FakeDataProvider();
        //get the FakeDataService interface to call API routes
        mTService = provider.getTService();


        getPosterDetail();



        collapsing.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        collapsing.setCollapsedTitleTextColor(Color.rgb(255,255,255));
        collapsing.setCollapsedTitleGravity(View.TEXT_ALIGNMENT_GRAVITY);
        collapsing.setTitle(title);

        if(Call!=null){
            phone=  Call;
        }

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String q="";

                q+=posterModel.title.toString()+"\n";
                q+=posterModel.description.toString()+"\n";
                q+=posterModel.cityname.toString()+"-"+posterModel.location_name.toString()+"\n";
                q+=phone+"\n";
                q+=sms+"\n";


             //   q+="http://test.ictplus.ir/posterbyid/"+IdInEditMode+"\n";
                q+="اپلیکیشن دستی کالا";

                Intent share_intent=new Intent(Intent.ACTION_SEND);
                share_intent.setType("text/plain");
                share_intent.putExtra(Intent.EXTRA_TEXT,q);
                startActivity(Intent.createChooser(share_intent,"اشتراک گذاری"));

            }
        });
        if (mAppPreferenceTools.EXISTFavorite(IdInEditMode)){
            imgnomark.setVisibility(View.GONE);
            imgmark.setVisibility(View.VISIBLE);
        }
    }

public void getPosterDetail(){

    if ( !IdInEditMode.equals("")) {

        mViewManager.loading();

        //get tweet by id from server
        Call<PosterModel> call = mTService.getPosterbyid(IdInEditMode);
        call.enqueue(new Callback<PosterModel>() {
            @Override
            public void onResponse(Call<PosterModel> call, Response<PosterModel> response) {
                if (response.isSuccessful()) {
                    //bind value to fields
                    if ( response.body().title==null) {
                        mViewManager.empty();
                        return;
                    }

                    mViewManager.showResult();


                    posterModel=response.body();
                    setupViewPager(viewPager);
                    tabLayout.setupWithViewPager(viewPager);
                    viewPager.setCurrentItem(1);

                    Call=response.body().phone;

                    sms=convert_number(response.body().sms+"");
                    u=response.body().src_pic;

                    if(u=="null" || u==null)
                    {
                        slideshow.setVisibility(View.GONE);
                    }
                    else
                    {
                        try {
                            m = u.split(",");
                            slider slider = new slider();
                            slider.slider(DetailPoster.this,slideshow,m);
                        }
                        catch (Error e){
                            slideshow.setVisibility(View.GONE);
                        }

                    }



                } else {
                    ErrorModel errorModel = ErrorUtils.parseError(response);
                    // Toast.makeText(getBaseContext(), "Error type is " + errorModel.type + " , description " + errorModel.description, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getBaseContext(),"اتصال به اینترنت بررسی شود3",Toast.LENGTH_LONG).show();

                    mViewManager.error();
                }
            }

            @Override
            public void onFailure(Call<PosterModel> call, Throwable t) {
                //occur when fail to deserialize || no network connection || server unavailable
                //  Toast.makeText(getBaseContext(), "Fail it >> " + t.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(),"اتصال به اینترنت بررسی شود4",Toast.LENGTH_LONG).show();

                mViewManager.error();
            }
        });
    }

    else {
        mViewManager.empty();
        return;
    }

}



    public void OnClicknomark(View v){
            mAppPreferenceTools.addFavorite(posterModel);
            imgnomark.setVisibility(View.GONE);
            imgmark.setVisibility(View.VISIBLE);
            Toast.makeText(getBaseContext(), "آگهی نشان شد" , Toast.LENGTH_SHORT).show();


    }
    public void OnClickmark(View v){
            mAppPreferenceTools.removeFavorite(IdInEditMode);
            imgnomark.setVisibility(View.VISIBLE);
            imgmark.setVisibility(View.GONE);
        setResult(-1);

        //       Toast.makeText(getBaseContext(), "نشان آگهی حذف شد" , Toast.LENGTH_SHORT).show();



    }

    public String convert_number(String number)
    {

        return number.replace("1","١").replace("2","۲").replace("3","۳")
                .replace("6","۶").replace("7","۷").replace("8","۸")
                .replace("9","۹").replace("4","۴").replace("5","۵");



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //back to main activity
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new viewFragment(IdInEditMode), "نظرات");
        adapter.addFragment(new OneFragment(posterModel,date), "جزئیات");

        viewPager.setAdapter(adapter);
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public void customErrorView(View v) {
        Button retry = (Button) v.findViewById(R.id.btn);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewManager.loading();
                getPosterDetail();

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

                getPosterDetail();

            }
        });
    }
}
