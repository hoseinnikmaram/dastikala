package com.dastsaz.dastsaz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.dastsaz.dastsaz.fragments.*;
import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.utility.AppPreferenceTools;
import com.dastsaz.dastsaz.utility.Constants;
import android.support.v4.app.FragmentTransaction;
import com.dastsaz.dastsaz.helper.BottomNavigationBehavior;


public class CustomViewIconTextTabsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Fragment F2;
    private Fragment F3;
    private Fragment F4;
    public static int count=0;
    private static int a=0;
    private static int b=0;
    private static int c=0;
    private static int d=0;
    public static String IdCity="0";
    private AppPreferenceTools mAppPreferenceTools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view_icon_text_tabs);
        F3 = new ThreeFragment();

        F2 = new TwoFragment();
        F4 = new FourFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container, F4).commit();
        getSupportFragmentManager().beginTransaction().show(F4).commit();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAppPreferenceTools = new AppPreferenceTools(this);
        IdCity=mAppPreferenceTools.getidcity();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        // load the store fragment by default
        toolbar.setTitle("خانه");





    }

    @Override
    protected void onPause() {

         a=0;
         b=0;
         c=0;
         d=0;
        super.onPause();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    toolbar.setTitle("همه پیشنهادات");
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

                    toolbar.setTitle("گروه ها");

                    getSupportFragmentManager().beginTransaction().show(F3).commit();


                   // loadFragment(fragment);
                    return true;
                case R.id.navigation_search:
                    getSupportFragmentManager().beginTransaction().hide(F4).commit();
                    getSupportFragmentManager().beginTransaction().hide(F3).commit();
                    if (c==0){
                        getSupportFragmentManager().beginTransaction().add(R.id.frame_container, F2).commit();
                    }

                    toolbar.setTitle("جستجو");
                    getSupportFragmentManager().beginTransaction().show(F2).commit();

                    count=0;
                    c=1;

                    //   loadFragment(fragment);
                    return true;

            }

            return false;
        }
    };

    /**
     * loading fragment into FrameLayout
     *
     * @param fragment
     */
    private void loadFragment(Fragment fragment) {

        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentById(R.id.ly_groop);
        if (count != 1 ) {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } else {
            getFragmentManager().popBackStack();
            super.onBackPressed();
        }

    }
}

