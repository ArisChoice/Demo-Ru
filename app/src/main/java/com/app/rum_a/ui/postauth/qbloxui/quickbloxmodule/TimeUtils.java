package com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {

    private TimeUtils() {
    }


    public static String getTime(long milliseconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return dateFormat.format(new Date(milliseconds));
    }

    public static String getDate(long milliseconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM ", Locale.getDefault());
        return dateFormat.format(new Date(milliseconds));
    }


    public static String getDateOnly(long milliseconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd E yyyy", Locale.getDefault());
        return dateFormat.format(new Date(milliseconds));
    }


    public static String getDateForCall(long milliseconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd E yyyy, hh:mm a", Locale.getDefault());
        return dateFormat.format(new Date(milliseconds));
    }

    public static long getDateAsHeaderId(long milliseconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        return Long.parseLong(dateFormat.format(new Date(milliseconds)));
    }

    public static String getLastMessageDate(long milliseconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E", Locale.getDefault());
        return dateFormat.format(new Date(milliseconds));
    }

    public static String convertingLocalTimeToUTC() {

        long ts = System.currentTimeMillis();
        Date localTime = new Date(ts);
//        Calendar calendar = Calendar.getInstance();
//        calendar.getTime();
        String format = "yyyy/MM/dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        // Convert Local Time to UTC (Works Fine)
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gmtTime = new Date(sdf.format(localTime));

        Log.e("time ", "Local:" + localTime.toString() + "," + localTime.getTime() + " --> UTC time:"
                + gmtTime.toString() + "," + gmtTime.getTime());

        return gmtTime.getTime() + "";
    }

    //conver 10 digit to 13 dogits
    public static Date convertingUTCToLocal(String time) {
        long ts = System.currentTimeMillis();
        Date localTime = new Date(ts);
        Date fromGmt = new Date(Long.parseLong(time) * 1000L);
        Log.e("timeinlocal ", "UTC time:" + time + "," + time + " --> Local:"
                + fromGmt.toString() + "-" + fromGmt.getTime());
        return fromGmt;
    }

    public static String convertUTCtoMyTime(String time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = "";
        try {
            Date date = df.parse(time);
            df.setTimeZone(TimeZone.getDefault());
            formattedDate = df.format(date);
        } catch (ParseException e) {

            e.printStackTrace();
        }

        return formattedDate;

    }

    /***
     * This will convert local device time to standard UTC zone
     * yhaa se 10 digit milna
     *
     * @return
     */
    public static String convertLocalTimetoUTC() {

        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        localDateFormat.setTimeZone(TimeZone.getDefault());

        String localDateTime = (String) localDateFormat.format(Calendar.getInstance().getTime());
        Date localDateObj = null;

        try {
            localDateObj = localDateFormat.parse(localDateTime);
        } catch (Exception e) {
        }
        Log.e("local ttt gettime", Calendar.getInstance().getTime() + "");
        SimpleDateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String utcDateTime = null;
        Date date = null;
        try {
            utcDateTime = utcDateFormat.format(localDateObj);
            date = utcDateFormat.parse(utcDateTime);
        } catch (Exception e) {
        }
        Log.e("ttt gettime", date.getTime() + "");
        return (date.getTime() / 1000L) + "";

    }

    public static String convertLocalTimetoUT2C() {

        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        localDateFormat.setTimeZone(TimeZone.getDefault());

        String localDateTime = (String) localDateFormat.format(Calendar.getInstance().getTimeInMillis());
        Date localDateObj = null;

        try {
            localDateObj = localDateFormat.parse(localDateTime);
        } catch (Exception e) {
        }
        Log.e("local ttt getmili", Calendar.getInstance().getTimeInMillis() + "");
        SimpleDateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String utcDateTime = null;
        Date date = null;
        try {
            utcDateTime = utcDateFormat.format(localDateObj);
            date = utcDateFormat.parse(utcDateTime);
        } catch (Exception e) {
        }
        Log.e("ttt getmili", date.getTime() + "");
        return date.getTime() + "";

    }

    public static String convertLocalTimetoUT21C() {

        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        localDateFormat.setTimeZone(TimeZone.getDefault());

        String localDateTime = (String) localDateFormat.format((System.currentTimeMillis()));
        Date localDateObj = null;

        try {
            localDateObj = localDateFormat.parse(localDateTime);
        } catch (Exception e) {
        }
        Log.e("local ttt currentime", System.currentTimeMillis() + "");
        SimpleDateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String utcDateTime = null;
        Date date = null;
        try {
            utcDateTime = utcDateFormat.format(localDateObj);
            date = utcDateFormat.parse(utcDateTime);
        } catch (Exception e) {
        }
        Log.e("ttt currentime", date.getTime() + "");
        return date.getTime() + "";

    }

    public static void hideKeyboard(Activity context) {
        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * compairing message date with current date
     *
     * @param messageDate
     * @return
     */
    public static boolean compairingDateswithCurrentDate(String messageDate) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString = "";
        Date currentDate = null;
        try {
            currentDateString = df.format(c.getTime());
            currentDate = df.parse(currentDateString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Calendar c1 = Calendar.getInstance();
        c1.setTime(TimeUtils.convertingUTCToLocal((Long.parseLong(messageDate)) / 1000L + ""));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String parsedMessageDateString = dateFormat.format(c1.getTime());
        Date parsedMessageDate = null;
        try {
            parsedMessageDate = dateFormat.parse(parsedMessageDateString);
        } catch (Exception e) {
        }


        if (currentDate.equals(parsedMessageDate)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * compairing different dates
     *
     * @param previousDate
     * @param currentDate  messageCurrentDate
     * @return
     */
    public static boolean compairingDates(String previousDate, String currentDate) {

        Calendar c1 = Calendar.getInstance();
        c1.setTime(TimeUtils.convertingUTCToLocal(previousDate));
        SimpleDateFormat previousDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String previousDateString = previousDateFormat.format(c1.getTime());
        Date prevousDate = null;
        try {
            prevousDate = previousDateFormat.parse(previousDateString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Calendar c2 = Calendar.getInstance();
        c2.setTime(TimeUtils.convertingUTCToLocal(currentDate));
        SimpleDateFormat currentDateDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString = currentDateDateFormat.format(c2.getTime());
        Date currentDate1 = null;
        try {
            currentDate1 = currentDateDateFormat.parse(currentDateString);
        } catch (Exception e) {
        }


        if (currentDate1.equals(prevousDate)) {
            return true;
        } else {
            return false;
        }

    }

    public static String gettingMidNightTime() {

        // today
        Calendar date = new GregorianCalendar();
// reset hour, minutes, seconds and millis
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return ((date.getTime().getTime() / 1000L) + "");
    }

    public static void deleteRecursive(File fileOrDirectory) {
        try {
            if (fileOrDirectory.isDirectory())
                for (File child : fileOrDirectory.listFiles())
                    deleteRecursive(child);

            fileOrDirectory.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentTimeZone() {
        Log.e("login_date", TimeZone.getDefault().getID());
        return TimeZone.getDefault().getID();
    }

    public static String getCurrentTime() {

        Date date = new Date();
        String login_date = "";

        SimpleDateFormat formate = new SimpleDateFormat("yyyy MM dd  HH:mm:ss");
        login_date = formate.format(date);

        Log.e("login_date", login_date);
        return login_date;
    }

    /**
     * check for empty arraylist
     *
     * @param mlist
     * @param <T>
     * @return
     */
    public static <T> boolean isArrayListEmpty(List<T> mlist) {

        if (mlist == null) {
            return true;
        } else if (mlist.size() == 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * This method will convert time coming from pubnub server to local time
     * according to current timezone
     *
     * @param
     * @return
     */
    public String convertUTCtoMyTime1(String utcDateTimeFromPubnub) {
        SimpleDateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = "";
        Date date = null;
        try {
            date = utcDateFormat.parse(utcDateTimeFromPubnub);
        } catch (Exception e) {
            date = null;
        }

        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        localDateFormat.setTimeZone(TimeZone.getDefault());


        formattedDate = localDateFormat.format(date);


        SimpleDateFormat requiredDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date localDateObj = null;

        try {
            localDateObj = localDateFormat.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        formattedDate = requiredDateFormat.format(localDateObj);


        return formattedDate;

    }


}