package com.dastsaz.dastsaz.utility;

import java.util.Arrays;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by m.hosein on 11/9/2017.
 */

public class ChangeDate {

    public boolean gLeapYear(int year) {
        if ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0)))
            return true;
        else
            return false;
    }

    public boolean sLeapYear(int year) {
        int[] ary = {1, 5, 9, 13, 17, 22, 26, 30};
        boolean result = false;
        int b = year % 33;
        if (Arrays.asList(ary).contains(b))
            result = true;
        return result;
    }


    public String shamsiDate2(int gyear, int gmonth, int gday) {
        int[] _gl = {0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335};
        int[] _g = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};

        int deydiffjan = 10;
        int gd = 0;
        int sd = 0;
        int sm = 0;
        int gmod = 0;
        int sy = 0;

        if (gLeapYear(gyear - 1))
            deydiffjan = 11;
        if (gLeapYear(gyear))
            gd = _gl[gmonth - 1] + gday;
        else
            gd = _g[gmonth - 1] + gday;
        if (gd > 79) {
            sy = gyear - 621;
            gd = gd - 79;
            if (gd <= 186) {
                gmod = gd % 31;
                switch (gmod) {
                    case 0:
                        sd = 31;
                        sm = (int) (gd / 31);
                        break;
                    default:
                        sd = gmod;
                        sm = (int) (gd / 31) + 1;
                }
            } else {
                gd = gd - 186;
                gmod = gd % 30;
                switch (gmod) {
                    case 0:
                        sd = 30;
                        sm = (int) (gd / 30) + 6;
                        break;
                    default:
                        sd = gmod;
                        sm = (int) (gd / 30) + 7;
                }
            }
        } else {
            sy = gyear - 622;
            gd = gd + deydiffjan;
            gmod = gd % 30;
            switch (gmod) {
                case 0:
                    sd = 30;
                    sm = (int) (gd / 30) + 9;
                    break;
                default:
                    sd = gmod;
                    sm = (int) (gd / 30) + 10;
            }
        }
        if (String.valueOf(sm).length() == 1) {
            return String.valueOf(sy) +"0" +String.valueOf(sm) + String.valueOf(sd);

        } else {
            return String.valueOf(sy) + String.valueOf(sm) + String.valueOf(sd);
        }
    }


    public String shamsiDate(int gyear, int gmonth, int gday) {
        int[] _gl = {0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335};
        int[] _g = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};

        int deydiffjan = 10;
        int gd = 0;
        int sd = 0;
        int sm = 0;
        int gmod = 0;
        int sy = 0;

        if (gLeapYear(gyear - 1))
            deydiffjan = 11;
        if (gLeapYear(gyear))
            gd = _gl[gmonth - 1] + gday;
        else
            gd = _g[gmonth - 1] + gday;
        if (gd > 79) {
            sy = gyear - 621;
            gd = gd - 79;
            if (gd <= 186) {
                gmod = gd % 31;
                switch (gmod) {
                    case 0:
                        sd = 31;
                        sm = (int) (gd / 31);
                        break;
                    default:
                        sd = gmod;
                        sm = (int) (gd / 31) + 1;
                }
            } else {
                gd = gd - 186;
                gmod = gd % 30;
                switch (gmod) {
                    case 0:
                        sd = 30;
                        sm = (int) (gd / 30) + 6;
                        break;
                    default:
                        sd = gmod;
                        sm = (int) (gd / 30) + 7;
                }
            }
        } else {
            sy = gyear - 622;
            gd = gd + deydiffjan;
            gmod = gd % 30;
            switch (gmod) {
                case 0:
                    sd = 30;
                    sm = (int) (gd / 30) + 9;
                    break;
                default:
                    sd = gmod;
                    sm = (int) (gd / 30) + 10;
            }
        }

        return String.valueOf(sy) + '/' + String.valueOf(sm) + '/' + String.valueOf(sd);
    }





    public static String getCurrentDate()
    {
        FDate curDate = new FDate(System.currentTimeMillis());
        return curDate.toString();
    }

    public static String getCurrentTime()
    {
        FDate curDate = new FDate(System.currentTimeMillis());
//    String time = curDate.getHour() + ":"+curDate.getMinute()+":"+curDate.getSecond();
        return getCompleteTimeString_persiancoders(curDate);
    }

    public static String getCompleteTimeString_persiancoders(FDate fdate)
    {
        StringBuffer b = new StringBuffer();
        b.append((fdate.getHour() < 10) ? "0" + (fdate.getHour()) :
                String.valueOf(fdate.getHour()));
        b.append(":");
        b.append((fdate.getMinute() < 10) ? "0" + (fdate.getMinute()) :
                String.valueOf(fdate.getMinute()));
        b.append(":");
        b.append((fdate.getSecond() < 10) ? "0" + (fdate.getSecond()) :
                String.valueOf(fdate.getSecond()));
        return b.toString();
    }



    public static int getCurrentYear()
    {
        FDate curDate = new FDate(System.currentTimeMillis());
        return curDate.getYear();
    }

    public static int getCurrentMonth()
    {
        FDate curDate = new FDate(System.currentTimeMillis());
        return curDate.getMonth();
    }

    public static int getCurrentDay()
    {
        FDate curDate = new FDate(System.currentTimeMillis());
        return curDate.getDate();
    }


    public static Date toDate(String formattedDate)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        ParsePosition pos = new ParsePosition(0);
        Date d = dateFormat.parse(formattedDate,pos);
        return d;
    }


    public static String invertDate(String fdate)
    {
        String yyyy = null;
        String mm = null;
        String dd = null;

        if (fdate==null || fdate.length()==0)
            return "";
        StringTokenizer strTokenizer = new StringTokenizer(fdate,"/");
        if (strTokenizer.hasMoreTokens())
        {
            yyyy = strTokenizer.nextToken();
            if (strTokenizer.hasMoreTokens())
            {
                mm = strTokenizer.nextToken();
                if (strTokenizer.hasMoreTokens())
                {
                    dd = strTokenizer.nextToken();
                    return dd+"/"+mm+"/"+yyyy;
                }
            }
        }
        return fdate;
    }



    public static String changeFarsiToMiladi(String farsiDate)
    {
        Date miladiDate = ShamsiCalendar.shamsiToMiladi_persiancoders(farsiDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(miladiDate);
    }

    public static String changeMiladiToFarsi(String miladiDate)
    {
        return ShamsiCalendar.miladiToShamsi_persiancoders_com(toDate(miladiDate));
    }


    public static String getDayMounthYear(){
        return ShamsiCalendar.weekDayName(ShamsiCalendar.dayOfWeek(ChangeDate.getCurrentDate()))+ " "+
                ShamsiCalendar.monthDayName(ChangeDate.getCurrentDay())+
                ShamsiCalendar.monthName(ChangeDate.getCurrentMonth()) +" Ù…Ø§Ù‡ "  +
                String.valueOf(ChangeDate.getCurrentYear());
    }

    public static String decreaseYear (String CurrDate , int cnt){
        String year = CurrDate.substring(0,4);
        int ny = Integer.decode(year) - cnt;
        return String.valueOf(ny) + CurrDate.substring(4);
    }

    public static String decreaseCurrentYear (int cnt){
        String cur = getCurrentDate();
        String year = cur.substring(0,4);
        int ny = Integer.decode(year) - cnt;
        return String.valueOf(ny) + cur.substring(4);
    }

    public static String increaseYear (String tavalodDate , int cnt){
        String year = tavalodDate.substring(0,4);
        int ny = Integer.decode(year) + cnt;
        return String.valueOf(ny) + tavalodDate.substring(4);
    }

    public static String increaseCurrentYear (int cnt){
        String cur = getCurrentDate();
        String year = cur.substring(0,4);
        int ny = Integer.decode(year) + cnt;
        return String.valueOf(ny) + cur.substring(4);
    }
    public static String getCurrentDateTimeString()
    {
        FDate curDate = new FDate(System.currentTimeMillis());
        return curDate.toString().concat(" ").concat(getCompleteTimeString_persiancoders(curDate));
    }




}

