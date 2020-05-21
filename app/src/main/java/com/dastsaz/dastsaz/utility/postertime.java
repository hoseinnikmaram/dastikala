package com.dastsaz.dastsaz.utility;

import android.widget.Toast;

import com.dastsaz.dastsaz.models.ErrorModel;
import com.dastsaz.dastsaz.models.NowDateModel;
import com.dastsaz.dastsaz.models.PosterModel;
import com.dastsaz.dastsaz.network.FakeDataProvider;
import com.dastsaz.dastsaz.network.FakeDataService;
import com.dastsaz.dastsaz.ui.DetailPoster;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by m.hosein on 1/11/2018.
 */

public class postertime {
    private static FakeDataService mTService;
    private static String datetime="";
    public static String timer(String Year,String month,String Day,String h,String m,String s){
        String paswand;
      //  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      //  Calendar cal = Calendar.getInstance();
     // String t = dateFormat.format(cal.getTime());

      String t=NowDateModel.current;
        String ti= (String) t.subSequence(11,19);
        String[] time = ti.split(":");

        String d= (String) t.subSequence(0,10);
        String[] date = d.split("-");

        String a=date[0]+date[1]+date[2]+" time"+time[0]+time[1]+time[2];

        if (Integer.parseInt(date[0])-Integer.parseInt(Year) != 0)
        {

            paswand =String.valueOf((Integer.parseInt(date[0]))-(Integer.parseInt(Year)));

            return paswand+" سال پیش";
        }
        else if ((Integer.parseInt(date[1]))-Integer.parseInt(month) != 0){
            paswand =String.valueOf((Integer.parseInt(date[1]))-(Integer.parseInt(month)));

            return paswand+" ماه پیش";
        }

        else if ((Integer.parseInt(date[2]))-Integer.parseInt(Day) >1){
            paswand =String.valueOf((Integer.parseInt(date[2]))-(Integer.parseInt(Day)));

            return paswand+" روز پیش";
        }

        else if ((Integer.parseInt(date[2]))-Integer.parseInt(Day) >0){
            if (( ((Integer.parseInt(time[0])+24)-Integer.parseInt(h)) <= 1 )){

                if (((Integer.parseInt(time[1])+60)-Integer.parseInt(m) > 29)){

                    return "نیم ساعت پیش";
                }

                else if (((Integer.parseInt(time[1])+60)-Integer.parseInt(m) > 13)){

                    return "یک ربع پیش";
                }

                else if (((Integer.parseInt(time[1])+60)-Integer.parseInt(m) > 0)){

                    return "فوری";
                }

            }

            else if (( ((Integer.parseInt(time[0])+24)-Integer.parseInt(h)) <23 )){
                paswand =String.valueOf((Integer.parseInt(time[0])+24)-(Integer.parseInt(h)));

                return paswand+" ساعت پیش";
            }

            else {
            return "دیروز";}
        }

        else if ((Integer.parseInt(time[0])-Integer.parseInt(h) > 1)){
            paswand =String.valueOf((Integer.parseInt(time[0]))-(Integer.parseInt(h)));

            return paswand+" ساعت پیش";
        }

        else if ((Integer.parseInt(time[0])-Integer.parseInt(h) > 0)){
            if (((Integer.parseInt(time[1])+60)-Integer.parseInt(m) > 35)){

                return "نیم ساعت پیش";
            }
            else if (((Integer.parseInt(time[1])+60)-Integer.parseInt(m) > 13)){

                return "یک ربع پیش";
            }

            else if (((Integer.parseInt(time[1])+60) >= 0)){

                return "فوری";
            }
            else {
                paswand = String.valueOf((Integer.parseInt(time[0])) - (Integer.parseInt(h)));

                return paswand + " ساعت پیش";
            }
        }


        else if ((Integer.parseInt(time[1])-Integer.parseInt(m) > 35)){

            return "نیم ساعت پیش";
        }

        else if ((Integer.parseInt(time[1])-Integer.parseInt(m) > 13)){

            return "یک ربع پیش";
        }

        else if ((Integer.parseInt(time[1])-Integer.parseInt(m) >= 0)){

            return "فوری";
        }
         return "نامشخص";

    }





}


