package com.dastsaz.dastsaz.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.ui.DetailPoster;
import com.dastsaz.dastsaz.ui.DialogActivtyDaste;
import android.support.v4.app.FragmentManager;
import android.widget.LinearLayout;



public class ThreeFragment extends Fragment{
    private LinearLayout mLyAction;

    public ThreeFragment() {
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
         View rootView= inflater.inflate(R.layout.fragment_three, container, false);

        mLyAction = (LinearLayout) rootView.findViewById(R.id.ly_food);
        mLyAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment= new dasteFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ID_Group", "1");
                bundle.putString("Name_Group", "خوراکی ها");
                String backStateName = fragment.getClass().getName();

                fragment.setArguments(bundle);
                boolean fragmentPopped = getActivity().getSupportFragmentManager().popBackStackImmediate (backStateName, 0);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                if (!fragmentPopped){ //fragment not in back stack, create it.
                    ft.replace(R.id.ly_groop, fragment);
                    ft.addToBackStack(backStateName);
                    ft.commit();
                }



               // getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ly_groop, fragment).addToBackStack(backStateName).commit();

//                startActivity(new Intent(getActivity().getBaseContext(), fragment.getClass()));
          //      getActivity().getSupportFragmentManager().beginTransaction().(fragment).commit();






            }
        });

        return rootView;
    }





}
