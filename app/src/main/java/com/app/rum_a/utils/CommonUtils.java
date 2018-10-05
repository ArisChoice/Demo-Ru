package com.app.rum_a.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.app.rum_a.R;
import com.app.rum_a.core.MyApplication;
import com.app.rum_a.model.resp.PropertyListResponseModel;
import com.app.rum_a.model.resp.UserResponseModel;
import com.app.rum_a.ui.postauth.activity.SettingActivity;
import com.app.rum_a.ui.postauth.adapter.OpenBottonDialogAdapter;
import com.app.rum_a.ui.pre.LandingActivity;
import com.app.rum_a.ui.pre.LoginActivity;
import com.app.rum_a.utils.AppConstants.CurrencySymbols;
import com.app.rum_a.utils.appinterface.CustomeDialogClickListner;
import com.app.rum_a.utils.appinterface.OnBottomDialogItemListener;
import com.app.rum_a.utils.appinterface.OnItemClickListener;
import com.app.rum_a.utils.views.RumTextView;
import com.app.rum_a.utils.views.TouchImageView;
import com.quickblox.users.model.QBUser;
import com.rd.PageIndicatorView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by harish on 23/8/18.
 */

public class CommonUtils {
    private static Context context;


    public CommonUtils() {
        // This utility class is not publicly instantiable
    }

    public static CommonUtils getInstance(Context context) {
        CommonUtils.context = context;
        return new CommonUtils();
    }


    public static void hideView(View v) {
        v.setVisibility(View.GONE);
    }

    public static void showView(View v) {
        v.setVisibility(View.VISIBLE);
    }

    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getTimestamp() {
        return new SimpleDateFormat(AppConstants.TIMESTAMP_FORMAT, Locale.US).format(new Date());
    }

    public static long getTimestampOther() {
        return System.currentTimeMillis() / 10000;
    }

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isEmpty(EditText editText) {
        if (editText.getText().toString().trim().contentEquals("") || editText.getText().toString().trim() == null) {
            editText.requestFocus();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Description : Toast with Message String as input
     */
    Toast toast;

    public void ShowToast(String msg) {
        try {
            if (toast != null)
                toast.cancel();
            if (msg != null && !msg.trim().equalsIgnoreCase("")) {
                toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Description : Validates the email
     *
     * @param editText
     * @return true : if email is valid
     */
    public boolean isValidEmail(EditText editText) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(editText.getText().toString());
        if (matcher.matches()) {
            return true;
        } else {
            ShowToast(context.getString(R.string.error_toast_invalid_email));
            return false;
        }
    }

    /**
     * @param context
     * @desc to check weather app is in
     * background or not.
     * *
     */
    public static boolean isAppInBackground(Context context) {

        boolean isInBackground = true;
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (String activeProcess : processInfo.pkgList) {
                            if (activeProcess.equals(context.getPackageName())) {
                                isInBackground = false;
                            }
                        }
                    }
                }
            } else {
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                ComponentName componentInfo = taskInfo.get(0).topActivity;
                if (componentInfo.getPackageName().equals(context.getPackageName())) {
                    isInBackground = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isInBackground;
    }

    public static String getText(EditText editText) {
        return editText.getText().toString();
    }

    /**
     * get unique device id
     */

    @SuppressLint("MissingPermission")
    public static String getUniqueDeviceId(Context context) {

        String deviceId;
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            deviceId = deviceUuid.toString();
        } catch (Exception e) {
            deviceId = tm.getDeviceId();
        }
        return deviceId;
    }

    public void openDialog(Activity activity, final String[] name, int[] icons, final OnBottomDialogItemListener listener) {
        final Dialog mBottomSheetDialog = new Dialog(activity, R.style.MaterialDialogSheet);
        View child = activity.getLayoutInflater().inflate(R.layout.view_image_pick_dialog, null);
        mBottomSheetDialog.setContentView(child);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        RecyclerView list_view = child.findViewById(R.id.list_view);
        list_view.setLayoutManager(new LinearLayoutManager(activity));
        list_view.setAdapter(new OpenBottonDialogAdapter(name, icons, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int type, Object o) {
                switch (type) {
                    case AppConstants.RequestCode.APAPTER_BOTTOM_DIALOG_CLICK:
                        String s = (String) o;
                        Log.e("Data ", s);
                        listener.onItemClick(view, position, type, o);
                        mBottomSheetDialog.dismiss();
                        break;
                }
            }
        }));
    }

    public void getKeyHash() {//3pWvky8jAayZrBrP7iZm/US+hck=(debug)
        // Add code to print out the key hash
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.app.rum_a",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public void LogoutUser() {
        CommonUtils.getInstance(context).ShowToast(context.getString(R.string.session_expired));
        new PreferenceManager().setPrefrencesBoolean(AppConstants.ParmsType.isLogedIn, false);
        Intent intent = new Intent(context, LandingActivity.class);
        context.startActivity(intent);

    }

    public static String getValidUrl(String imageURL) {
        return NetworkConstatnts.URL.BASE_URL + imageURL;
    }


    public static String getCurrencySymbol(String currency) {
        switch (currency) {
            case AppConstants.CurrencyTypes.AUD:
                return CurrencySymbols.AUD;
            case AppConstants.CurrencyTypes.USD:
                return CurrencySymbols.USD;
            case AppConstants.CurrencyTypes.SGD:
                return CurrencySymbols.SGD;
            case AppConstants.CurrencyTypes.Euro:
                return CurrencySymbols.Euro;
            case AppConstants.CurrencyTypes.GBP:
                return CurrencySymbols.GBP;
            case AppConstants.CurrencyTypes.IDR:
                return CurrencySymbols.IDR;
            case AppConstants.CurrencyTypes.CNY:
                return CurrencySymbols.CNY;
            case AppConstants.CurrencyTypes.JPY:
                return CurrencySymbols.JPY;
            case AppConstants.CurrencyTypes.RUB:
                return CurrencySymbols.RUB;
            case AppConstants.CurrencyTypes.INR:
                return CurrencySymbols.INR;
            case AppConstants.CurrencyTypes.KRW:
                return CurrencySymbols.KRW;
            default:
                return CurrencySymbols.USD;

        }

    }

    public static String getlookingType(int lType) {
        switch (lType) {
            case AppConstants.LookingTypes.Rent:
                return AppConstants.LookingTypesStrings.Rent;
            case AppConstants.LookingTypes.Lease:
                return AppConstants.LookingTypesStrings.Lease;
            case AppConstants.LookingTypes.Buy:
                return AppConstants.LookingTypesStrings.Buy;
            case AppConstants.LookingTypes.Sell:
                return AppConstants.LookingTypesStrings.Sell;
            default:
                return AppConstants.LookingTypesStrings.Rent;
        }
    }

    public static String getLandForType(int lType) {
        switch (lType) {
            case AppConstants.ForRentOrBuy.Rent:
                return AppConstants.LookingTypesStrings.Rent;
            case AppConstants.ForRentOrBuy.Buy:
                return AppConstants.LookingTypesStrings.Buy;
            default:
                return AppConstants.LookingTypesStrings.Rent;
        }
    }

    public static String getSeekingType(int lType) {
        switch (lType) {
            case AppConstants.Seekingtype.Land:
                return AppConstants.SeekingtypeStrings.Land;
            case AppConstants.Seekingtype.House:
                return AppConstants.SeekingtypeStrings.House;
            case AppConstants.Seekingtype.Apartment:
                return AppConstants.SeekingtypeStrings.Apartment;
            case AppConstants.Seekingtype.Commercial_Space:
                return AppConstants.SeekingtypeStrings.Commercial_Space;
            default:
                return AppConstants.SeekingtypeStrings.House;
        }
    }

    public static Point getDisplaySize(WindowManager windowManager) {
        try {
            if (Build.VERSION.SDK_INT > 16) {
                Display display = windowManager.getDefaultDisplay();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                display.getMetrics(displayMetrics);
                return new Point(displayMetrics.widthPixels, displayMetrics.heightPixels);
            } else {
                return new Point(0, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Point(0, 0);
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public QBUser registerToQb(UserResponseModel.ResultBean.UserBean user) {
        QBUser qbUser = new QBUser();
        qbUser.setFullName(user.getFirstName() + " " + user.getLastName());
        qbUser.setEmail(user.getEmail());
        qbUser.setPhone(user.getUserId() + "");
        qbUser.setLogin(user.getUserId() + "");
        qbUser.setPassword(AppConstants.QuickBloxConstants.USER_GLOBAL_PASSWORD);
//        StringifyArrayList<String> tagsArray = new StringifyArrayList<>();
//        tagsArray.add("testing");
//        qbUser.setTags(tagsArray);
        return qbUser;
    }

    public String getDevideGCMid() {
        String mypreferenceNew = "mypref_Token";
        //GET THIS TOKEN FROM PREF
        SharedPreferences sharedpreferences = context.getSharedPreferences(mypreferenceNew, Context.MODE_PRIVATE);
        String deviceId = sharedpreferences.getString("pushToken", "");
//      Log.e(TAG, " Refreshed token  ::: sendRegistrationToServer  =====> " + sharedpreferences.getString("pushToken", ""));
        return deviceId;
    }

    //  Used in Message screen activity & Chat screen Activity.
    public static boolean IsChatActive = false;

    public static boolean getIsChatActive() {
        return IsChatActive;
    }

    public boolean setIsChatActive(boolean IsChatActive) {
        CommonUtils.IsChatActive = IsChatActive;
        return IsChatActive;
    }

    public void displayMessage(Activity aCtivty, String toastString) {
        Log.e("displayMessage", "" + toastString);
        /*Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();*/
        Snackbar snackbar = Snackbar.make(aCtivty.getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(R.color.white));
        snackbar.show();
    }

    public Dialog DialogSingleButton(Activity activity, String title, String message, final CustomeDialogClickListner customeDialogClickListner) {
        // custom dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.layout_custome_dialog_view);
        dialog.setTitle(null);

        // set the custom dialog components - text, image and button
        TextView txtTitle = dialog.findViewById(R.id.dialog_title_txt);
        txtTitle.setText(title);
        TextView txtMessage = dialog.findViewById(R.id.dialog_message_txt);
        txtMessage.setText(message);
        TextView txtOk = dialog.findViewById(R.id.dialog_ok_txt);


        // if button is clicked, close the custom dialog
        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                customeDialogClickListner.onOkClick();
            }
        });

        dialog.show();

        return dialog;
    }

    public Dialog FullImageScreem(Activity activity, String imageUrl) {
        final Dialog dialog = new Dialog(activity, android.R.style.Theme_NoTitleBar_Fullscreen);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.full_image_view_layout);
        dialog.setCancelable(true);
        TouchImageView fullImage = (TouchImageView)dialog.findViewById(R.id.image_full_view);
        try {
            new ImageUtility(activity).LoadImage(CommonUtils.getValidUrl(imageUrl), fullImage);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        ImageView backImage = dialog.findViewById(R.id.back_full_view);
        backImage.bringToFront();
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }
}
