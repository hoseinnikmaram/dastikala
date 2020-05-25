package com.dastsaz.dastsaz.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dastsaz.dastsaz.R;

import android.widget.LinearLayout;



public class ListFragment extends Fragment{
    private LinearLayout mLyAction;
    private LinearLayout mLyLavazem;
    public ListFragment() {
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
         View rootView= inflater.inflate(R.layout.fragment_list, container, false);

        mLyAction = (LinearLayout) rootView.findViewById(R.id.ly_digital);
        mLyAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment= new dasteFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ID_Group", "1");
                bundle.putString("Name_Group", "دیجیتال");
                String backStateName = fragment.getClass().getName();

                fragment.setArguments(bundle);
                boolean fragmentPopped = getActivity().getSupportFragmentManager().popBackStackImmediate (backStateName, 0);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                if (!fragmentPopped){ //fragment not in back stack, create it.
                    ft.replace(R.id.ly_groop, fragment);
                    ft.addToBackStack(backStateName);
                    ft.commit();
                }



            }
        });



        mLyLavazem = (LinearLayout) rootView.findViewById(R.id.ly_lavazem);
        mLyLavazem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new dasteFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ID_Group", "2");
                bundle.putString("Name_Group", "لوازم خانگی");
                String backStateName = fragment.getClass().getName();

                fragment.setArguments(bundle);
                boolean fragmentPopped = getActivity().getSupportFragmentManager().popBackStackImmediate(backStateName, 0);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                if (!fragmentPopped) { //fragment not in back stack, create it.
                    ft.replace(R.id.ly_groop, fragment);
                    ft.addToBackStack(backStateName);
                    ft.commit();
                }
            }
            });


        return rootView;
    }






}
