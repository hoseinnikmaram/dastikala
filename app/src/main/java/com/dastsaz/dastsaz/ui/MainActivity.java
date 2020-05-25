package com.dastsaz.dastsaz.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.fragments.HomeFragment;
import com.dastsaz.dastsaz.fragments.ListFragment;
import com.dastsaz.dastsaz.fragments.SearchFragment;
import com.dastsaz.dastsaz.helper.BottomNavigationBehavior;
import com.dastsaz.dastsaz.helper.CustomTypefaceSpan;
import com.dastsaz.dastsaz.models.CityModel;
import com.dastsaz.dastsaz.models.ErrorModel;
import com.dastsaz.dastsaz.network.FakeDataProvider;
import com.dastsaz.dastsaz.network.FakeDataService;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;
import com.dastsaz.dastsaz.utility.Constants;
import com.dastsaz.dastsaz.utility.ErrorUtils;
import com.dastsaz.dastsaz.utility.myClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private Fragment F2;
    private Fragment F3;
    private Fragment F4;
    private LinearLayout addposter;
    public static int count=0;
    public static int a=0;
    public static int b=0;
    public static int c=0;
    public static int d=0;
    public static String IdCity="0";
    private TextView title;
    private TextView add;
    private MaterialDialog builder;
    String[] citys;
    FakeDataService mTService;
    private AppPreferenceTools mAppPreferenceTools;
    private String[] citys_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        F3 = new ListFragment();

        F2 = new SearchFragment();
        F4 = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container, F4).commit();
        getSupportFragmentManager().beginTransaction().show(F4).commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addposter = (LinearLayout) toolbar.findViewById(R.id.add_poster);
        add = (TextView) toolbar.findViewById(R.id.txtadd);
        title = (TextView) toolbar.findViewById(R.id.txtTitle);

        mAppPreferenceTools = new AppPreferenceTools(this);
        IdCity = mAppPreferenceTools.getidcity();

        //first create new instant of FakeDataProvider
        FakeDataProvider provider = new FakeDataProvider();
        //get the FakeDataService interface to call API routes
        mTService = provider.getTService();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ImageView imgDrawer = (ImageView) toolbar.findViewById(R.id.imgDrawer);
        imgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        addposter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postNewTweetIntent = new Intent(getBaseContext(), CreatePoster.class);
                postNewTweetIntent.putExtra(Constants.ACTION_TO_DO_KEY, Constants.NEW_TWEET);
                startActivityForResult(postNewTweetIntent, Constants.CREATE_OR_EDIT_TWEET_REQUEST_CODE);

            }
        });
        myClass.textview_face(getBaseContext(),"IRANSans",add,title);
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
    }
    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font=Typeface.createFromAsset(getAssets(),"IRANSans"+".ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    protected void onPause() {

     //   a=0;
     //   b=0;
      ///  c=0;
      //  d=0;
        super.onPause();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    //toolbar.setTitle("");
                    getSupportFragmentManager().beginTransaction().hide(F3).commit();
                    getSupportFragmentManager().beginTransaction().hide(F2).commit();
                    getSupportFragmentManager().beginTransaction().show(F4).commit();

                    count=0;
                    // loadFragment(fragment);
                    return true;
                case R.id.navigation_list:
                    getSupportFragmentManager().beginTransaction().hide(F4).commit();
                    getSupportFragmentManager().beginTransaction().hide(F2).commit();
                    if (b==0){
                        getSupportFragmentManager().beginTransaction().add(R.id.frame_container, F3).commit();
                    }
                    b=1;

                  //  toolbar.setTitle("گروه ها");

                    getSupportFragmentManager().beginTransaction().show(F3).commit();

                    count=1;
                    // loadFragment(fragment);
                    return true;
                case R.id.navigation_search:
                    getSupportFragmentManager().beginTransaction().hide(F4).commit();
                    getSupportFragmentManager().beginTransaction().hide(F3).commit();
                    if (c==0){
                        getSupportFragmentManager().beginTransaction().add(R.id.frame_container, F2).commit();
                    }

                  //  toolbar.setTitle("جستجو");
                    getSupportFragmentManager().beginTransaction().show(F2).commit();

                    count=0;
                    c=1;

                    //   loadFragment(fragment);
                    return true;

            }

            return false;
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        }
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentById(R.id.ly_groop);
        if (count != 1 ) {
         //   moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            //moveTaskToBack(true);
         //   getFragmentManager().popBackStack();
            super.onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_myposter) {
            Intent myposter = new Intent(getBaseContext(), MyPoster.class);

            String ui=mAppPreferenceTools.getUserId();
            myposter.putExtra("ID_User", ui);
            startActivity(myposter);

        } else if (id == R.id.nav_city) {
            getCityFromServer();

        } else if (id == R.id.nav_mark) {
            Intent markposter = new Intent(getBaseContext(), MarkActivity.class);
            startActivity(markposter);



        } else if (id == R.id.nav_signin) {
            startActivity(new Intent(this, ActivityAccount.class));



        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(Gravity.RIGHT);
        return true;
    }

    private void getCityFromServer() {
        Call<List<CityModel>> call = mTService.getCitys();
        call.enqueue(new Callback<List<CityModel>>() {
            @Override
            public void onResponse(Call<List<CityModel>> call, Response<List<CityModel>> response) {


                if (response.isSuccessful()) {
                    //update the adapter data
                    citys = new String[response.body().size()];
                    citys_id=new String[response.body().size()];


                    for (int i=0 ; i<response.body().size() ; i++){

                        citys[i]=response.body().get(i).cityname;
                        citys_id[i]= String.valueOf(response.body().get(i).id_city);

                    }
                    load_dialog_city();

                }

                else {
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

    private void load_dialog_city(){

        builder = new MaterialDialog.Builder(this)
                .title(R.string.titlecity)
                .itemsGravity(GravityEnum.END)

                .buttonsGravity(GravityEnum.CENTER)
                .items(citys)

                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/



                        mAppPreferenceTools.saveidcity(citys_id[which],text.toString());

                        Toast.makeText(getBaseContext(), text.toString(), Toast.LENGTH_LONG).show();

                        Intent refresh = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(refresh);
                        finish();


                        return true;
                    }
                })


                .positiveText("انتخاب")
                .negativeText("بی خیال")
                .show();

    }

}
