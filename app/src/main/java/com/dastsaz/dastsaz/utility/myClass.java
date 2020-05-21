package com.dastsaz.dastsaz.utility;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.dastsaz.dastsaz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class myClass {

    public  static  void json_hash(String response, ArrayList<HashMap<String,String>> hashMaps){

        try {
            JSONArray jsonArray=new JSONArray(response);
            String key="";
            for (int i=0;i<jsonArray.length();i++){
                HashMap<String,String> hashMap=new HashMap<String,String>();
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                Iterator<String> iterator=jsonObject.keys();
                while (iterator.hasNext()){
                    key=iterator.next();
                    hashMap.put(key,jsonObject.getString(key));
                }
                hashMaps.add(hashMap);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean check_net(Context context){

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public static void alert_try(Activity activity,final  Runnable runnable){

        AlertDialog.Builder alert=new AlertDialog.Builder(activity);
        alert.setMessage("خطا در برقراری اتصال به سرور.لطفا اینترنت خود را بررسی نمایید");
        alert.setPositiveButton("سعی مجدد", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Handler().post(runnable);
            }
        });
        alert.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.create().show();

    }

    public static void toast_msg(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    public static void textview_face(Context context, String font, TextView...pTxt){

        Typeface typeface=Typeface.createFromAsset(context.getAssets(),font+".ttf");
        for(TextView txt : pTxt){
            txt.setTypeface(typeface);

        }

    }
}
