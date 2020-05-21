package com.dastsaz.dastsaz.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dastsaz.dastsaz.R;
import com.dastsaz.dastsaz.models.PosterModel;
import com.dastsaz.dastsaz.utility.myClass;


@SuppressLint("ValidFragment")
public class OneFragment extends Fragment{

    private TextView mTxtitle;
    private TextView mTxdescription;
    private TextView mTxloction;
    private TextView mTxphone;
    private TextView mTxsms;
    private TextView mTxprice;
    private TextView mTxDate;
    private TextView mTxdaste;
    private TextView mTxSub_daste;
    private TextView mTxcity;
    private PosterModel posterModel;
    private String date="";
    private TextView tx_view_phon;
    private TextView tx_view_namedaste;
    private TextView tx_view_descriptiion;
    private TextView tx_view_sms;
    private TextView tx_view_price;
    private LinearLayout onclicksms;
    private LinearLayout onclickphone;

    private String sms;
    @SuppressLint("ValidFragment")
    public OneFragment(PosterModel poster,String Datetime) {
        posterModel=poster;
        date=Datetime;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);

        mTxdescription = (TextView) rootView.findViewById(R.id.tx_description);
        mTxloction = (TextView) rootView.findViewById(R.id.tx_loction);
        mTxtitle = (TextView) rootView.findViewById(R.id.tx_title);
        mTxsms = (TextView) rootView.findViewById(R.id.tx_sms);
        mTxprice = (TextView) rootView.findViewById(R.id.tx_price);
        mTxphone = (TextView) rootView.findViewById(R.id.tx_phone);
        mTxcity = (TextView) rootView.findViewById(R.id.tx_city);
        mTxdaste = (TextView) rootView.findViewById(R.id.tx_daste);
        mTxSub_daste = (TextView) rootView.findViewById(R.id.tx_Sub_daste);
        tx_view_namedaste= (TextView) rootView.findViewById(R.id.tx_view_namedaste);
        tx_view_descriptiion = (TextView) rootView.findViewById(R.id.tx_view_descriptiion);
        tx_view_phon=(TextView) rootView.findViewById(R.id.tx_view_phon);
        tx_view_sms=(TextView) rootView.findViewById(R.id.tx_view_sms);
        tx_view_price=(TextView) rootView.findViewById(R.id.tx_view_price);
        mTxDate = (TextView) rootView.findViewById(R.id.tx_date);

        onclickphone=(LinearLayout) rootView.findViewById(R.id.oncall);
        onclicksms = (LinearLayout) rootView.findViewById(R.id.onsms);

        onclicksms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sms == null || sms.equals("")) {
                    Toast.makeText(getActivity(), "شماره پیامک ثبت نشده است", Toast.LENGTH_SHORT).show();

                }
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                        + sms)));
            }
        });


        onclickphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (posterModel.phone == null || posterModel.phone.equals("")) {
                    Toast.makeText(getActivity(), "شماره تماس ثبت نشده است", Toast.LENGTH_SHORT).show();

                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", posterModel.phone, null));
                    startActivity(intent);
                }
            }
        });

        mTxcity.setText(posterModel.cityname);
        mTxdaste.setText(posterModel.groupname);
        mTxtitle.setText(posterModel.title);
        mTxdescription.setText(posterModel.description);
        mTxloction.setText(posterModel.location_name);
        mTxSub_daste.setText(posterModel.subname);
        mTxsms.setText(convert_number(posterModel.sms+""));
        mTxphone.setText(posterModel.phone);
        mTxprice.setText(convert_number(posterModel.price+""));
        mTxDate.setText(convert_number(date));

        sms=convert_number(posterModel.sms+"");



        myClass.textview_face(this.getActivity(),"IRANSans",tx_view_price,tx_view_sms,tx_view_phon,tx_view_namedaste,tx_view_descriptiion,mTxtitle,mTxcity,mTxdescription,mTxloction,mTxSub_daste,mTxphone,mTxsms,mTxDate,mTxprice,mTxdaste);

        return rootView;
    }




    public String convert_number(String number)
    {

        return number.replace("1","١").replace("2","۲").replace("3","۳")
                .replace("6","۶").replace("7","۷").replace("8","۸")
                .replace("9","۹").replace("4","۴").replace("5","۵");



    }


}
