package com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.widget.Toast;

import com.app.rum_a.core.MyApplication;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.activities.CallService;
import com.app.rum_a.utils.PreferenceManager;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.users.model.QBUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by Sunny on 2/18/2017.
 */

public class GeneralUtils {

    public static List<File> fileList = new ArrayList<>();
    private final String[] projection = new String[]{MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA};
    private final String[] projection2 = new String[]{MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA};


    public static QBUser getUserFromSession() {
        QBUser user = PreferenceManager.getInstance(MyApplication.getInstance()).getQbUser();
        user.setId(QBSessionManager.getInstance().getSessionParameters().getUserId());
        user.setPassword(user.getPassword());
        return user;
    }


    /**
     * here we get friend id from chat dialog from Occupants id
     *
     * @param dailog
     * @return
     */

    public static String getUserId(QBChatDialog dailog) {

        if (dailog == null || dailog.getOccupants() == null || dailog.getOccupants().size() == 0) {
            return "";
        }

        dailog.getOccupants().remove(PreferenceManager.getInstance(MyApplication.getInstance()).getQbUser().getId());
        if (dailog.getOccupants().size() > 0) {
            return dailog.getOccupants().get(0) + "";
        }

        return "";
    }

    public static String generateProfileImagelink(String blobid) {
        return "https://api.quickblox.com/blobs/" + blobid + "/download";
    }

    public static void restartSession(final FragmentActivity context) {
        CallService.start(context, getUserFromSession());
//        ProgressDialogFragment.show(context.getSupportFragmentManager(), R.string.trying_to_reconnect);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                ProgressDialogFragment.hide(context.getSupportFragmentManager());
            }
        }, 4000);
    }

   /* public static void showCallSelectionDialog(final Context context, final DialogCallCallBacks callBacks) {
        AlertDialog.Builder builder = GeneralUtils.advanceAlertBuilder(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_call_selection, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        CustomTextView audioCall = (CustomTextView) view.findViewById(R.id.audioCall);
        CustomTextView videoCall = (CustomTextView) view.findViewById(R.id.videoCall);
        audioCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                callBacks.audioCall();
            }
        });

        videoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                callBacks.videoCall();
            }
        });

        dialog.show();
    }*/

    /**
     * Saves a Bitmap object to disk for analysis.
     *
     * @param bitmap The bitmap to save.
     */
 /*   public static File saveBitmap(final Bitmap bitmap, String fname) {


        final File myDir = FilePaths.getInstance().imagePath();

        if (!myDir.mkdirs()) {
            Log.i("dsf", "Make dir failed");
        }

        final File file = new File(myDir, fname);
        if (file.exists()) {
            file.delete();
        }
        try {
            final FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
        } catch (final Exception e) {
            Log.e("dsf", "Exception!", e);
        }
        return file;
    }*/

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        int len = searchStr.length();
        int max = str.length() - len;
        for (int i = 0; i <= max; i++) {
            if (str.regionMatches(true, i, searchStr, 0, len)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the network is available.
     */
    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }

    /**
     * check to empty list
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
     * get city,state,country using geocoder
     *
     * @param lat
     * @param longi
     * @return
     */
    public static String getCountryState(Context mcontext, Double lat, Double longi) {
        String data = "";

        Geocoder gcd = new Geocoder(mcontext, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, longi, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {

            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String city = addresses.get(0).getLocality();


            if (!TextUtils.isEmpty(city)) {
                data = data + city + ",";
            }
            if (!TextUtils.isEmpty(state)) {
                data = data + state + ",";
            }

            if (!TextUtils.isEmpty(country)) {
                data = data + country;
            }
            return data;


        }
        return "";
    }

    /*public static String getFinalString(CustomEditText et) {
        return et.getText().toString().trim();
    }*/

    public static boolean isEmailValid(String mEmail) {
        if (mEmail.matches(Patterns.EMAIL_ADDRESS.pattern())) {
            return true;
        }
        return false;

    }

    public static String getCurrentDateString() {
        String dateString = "";
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date date;
        try {
            date = new Date();
            dateString = format.format(date);
            Log.e("date", dateString);
        } catch (Exception e) {

        }
        return dateString;
    }

    public static byte[] stringToBitmap(String str) {
        byte[] bytesImage = Base64.decode(str, Base64.DEFAULT);
        return bytesImage;
        //Bitmap bitmap = BitmapFactory.decodeByteArray(bytesImage, 0, bytesImage.length);
        //ivImage.setImageBitmap(bitmap);
        // using glide
    }

    public static String bitmapToString(Bitmap bmp) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
        byte[] bytes = os.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static String copyFileOrDirectory(String srcDir, String dstDir) {
        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {

                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);

                }
            } else {
                return copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
//        final String root =
//                Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "iWitness";
//        File currentFile = new File(root, destFile.getName());
//        int index = destFile.getName().lastIndexOf(".");
//        File renamefile = new File(root, "ANDROID_" + System.currentTimeMillis() + destFile.getName().substring(index - 1, destFile.getName().length()));
//        if (currentFile.exists()) {
//            currentFile.renameTo(renamefile);
//        }
        return destFile.getPath();
    }

    public static void renameFile(File filename, File rename) {
        if (filename.exists()) {
            filename.renameTo(rename);
            Log.e("File", "renamed");
        }
    }

/*
    public static void hideKeyboard(Context context, View view) {
        try {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    public static int getScreenWidth(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        return width;
    }

    public static void hideKeyboard(Activity context) {
        try {
            View view = context.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addFragment(AppCompatActivity mAppCompatActivity, Fragment fragment, boolean addTobackstack) {
//        FragmentManager manager = mAppCompatActivity.getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = manager.beginTransaction();
//        fragmentTransaction.add(R.id.containerFL, fragment);
//        if (addTobackstack) {
//            fragmentTransaction.addToBackStack(fragment.getClass().getName());
//        }
//        fragmentTransaction.commit();
    }

    /*public static Uri getOutputMediaFileUri(int type) {

        return Uri.fromFile(getOutputMediaFile(type));
    }*/

    /*private static File getOutputMediaFile(int type) {


        // Create the storage directory if it does not exist
        if (!FilePaths.getInstance().imagePath().exists()) {
            if (!FilePaths.getInstance().imagePath().mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(FilePaths.getInstance().imagePath().getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }*/


//    public boolean isNetworkAvailable(Context context) {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }

    public static void webViewSettings(WebSettings webSettings) {
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDomStorageEnabled(true);
        //   webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAllowFileAccess(true);
    }


    //Todo will use one dialog soon

    /////////////

    public static void clearbackStack(AppCompatActivity mAppCompatActivity) {
        try {
            FragmentManager fm = mAppCompatActivity.getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AlertDialog.Builder advanceAlertBuilder(Context context) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        return builder;
    }

    public static void showToast(Context activity, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();

        }
    }

    public static String onSelectFromGalleryResult(Activity context, Intent data) {

        try {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
            }
            Uri selectedImageUri = data.getData();
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = context.managedQuery(selectedImageUri, projection, null, null,
                    null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();

            return cursor.getString(column_index);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
//        SendPhotos ds = new SendPhotos(selectedImagePath);
//        ds.execute();
        return "";

    }

    public static List<File> getfile(File dir) {

        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
//                    fileList.add(listFile[i]);
                    getfile(listFile[i]);

                } else {
                    if (listFile[i].getName().endsWith(".pdf")
                            || listFile[i].getName().endsWith(".xlsx") || listFile[i].getName().endsWith(".xls")
                            || listFile[i].getName().endsWith(".doc") || listFile[i].getName().endsWith(".docx")
                            || listFile[i].getName().endsWith(".pptx") || listFile[i].getName().endsWith(".ppt")) {
                        fileList.add(listFile[i]);
                        Log.e("file", listFile[i] + "");
                    }
                }
            }
        }
        return fileList;
    }

    public static boolean isFileExist(String file) {
        return new File(file).exists();
    }

/*
    public static void showDailog(Context context, String title, String msg, String btnPosName,String btnNegName,final DialogButtonClick dialogButtonClick) {

        if(TextUtils.isEmpty(btnPosName)){
            btnPosName=context.getString(R.string.ok);
        }
        if(TextUtils.isEmpty(btnNegName)){
            btnNegName=context.getString(R.string.cancel);
        }

        AlertDialog.Builder builder = advanceAlertBuilder(context);
        builder.setTitle(title);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(btnPosName, new DialogInterface.OnClickListener() {
                    public static void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        dialogButtonClick.btnPositive();
                    }
                }).setNegativeButton(btnNegName, new DialogInterface.OnClickListener() {
            @Override
            public static void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.show();
    }
*/

    public static void openFile(Context context, File url) throws IOException {
        try {
            // Create URI
            File file = url;
            Uri uri = Uri.fromFile(file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            // Check what kind of file you are trying to open, by comparing the url with extensions.
            // When the if condition is matched, plugin sets the correct intent (mime) type,
            // so Android knew what application to use to open the file
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                //if you want you can also define the intent type for any other file

                //additionally use else clause below, to manage other unknown extensions
                //in this case, Android will show all applications installed on the device
                //so you can choose which application to use
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "No application found to handle such action", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static ArrayList<String> loadSongsFromCard(Context context) {
        ArrayList<String> songs = new ArrayList<String>();

        // Filter only mp3s, only those marked by the MediaStore to be music and longer than 1 minute
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
                + " AND " + MediaStore.Audio.Media.MIME_TYPE + "= 'audio/mpeg'"
                + " AND " + MediaStore.Audio.Media.DURATION + " > 60000";

        final String[] projection = new String[]{
                MediaStore.Audio.Media._ID,             //0
                MediaStore.Audio.Media.TITLE,           //1
                MediaStore.Audio.Media.ARTIST,          //2
                MediaStore.Audio.Media.DATA,            //3
                MediaStore.Audio.Media.DISPLAY_NAME
        };

        final String sortOrder = MediaStore.Audio.AudioColumns.TITLE
                + " COLLATE LOCALIZED ASC";

        Cursor cursor = null;

        try {
            // the uri of the table that we want to query
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; //getContentUriForPath("");
            // query the db
            cursor = context.getContentResolver().query(uri,
                    projection, selection, null, sortOrder);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    //if (cursor.getString(3).contains("AwesomePlaylists")) {
//                    String GSC = new String();
//                    GSC.ID = cursor.getLong(0);
//                    GSC.songTitle = cursor.getString(1);
//                    GSC.songArtist = cursor.getString(2);
//                    GSC.path = cursor.getString(3);
                    songs.add(cursor.getString(3));
                    Log.e("songpath", cursor.getString(3));
//                   // This code assumes genre is stored in the first letter of the song file name
//                     String genreCodeString = cursor.getString(4).substring(0, 1);
//                    if (!genreCodeString.isEmpty()) {
//                        try {
//                            GSC.genre = Short.parseShort(genreCodeString);
//                        } catch (NumberFormatException ex) {
//                            Random r = new Random();
//                            GSC.genre = (short) r.nextInt(4);
//                        } finally {
//                            songs.add(GSC);
//                        }
//                    }
                    //}
                    cursor.moveToNext();
                }
            }
        } catch (Exception ex) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return songs;
    }

    public static boolean hasPermission(Activity context, String permissions) {

        boolean hasAllPermissions = true;


        if (ContextCompat.checkSelfPermission(context, permissions) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(new String[]{permissions}, 200);
            }
            return false;
        } else {
            return true;
        }
    }


    /**
     * Description : Check if internet is switched on
     */

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public String getTimeZoneLocal() {
        try {
          /*  String ss= String.valueOf(TimeZone.getDefault());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date*/
           /* Calendar calendar = Calendar.getInstance(TimeZone.getDefault(),
                    Locale.getDefault());
            Date currentLocalTime = calendar.getTime();
            DateFormat date = new SimpleDateFormat("Z");
            String localTime = date.format(currentLocalTime);
            return localTime;*/
            //  Log.e("One", TimeZone.getDefault().getDisplayName());
            Log.e("Two", TimeZone.getDefault().getID());
            return TimeZone.getDefault().getID();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public String getDeviceType() {
        return "android";
    }

    public String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /*public boolean isEmptyET(CustomEditText et) {
        if (TextUtils.isEmpty(et.getText().toString().trim())) {
            return true;
        } else {
            return false;
        }
    }*/

    public void getVideoBuckets(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
                        null, null, MediaStore.Video.Media.DATE_ADDED);
        if (cursor.moveToLast()) {
            do {
                if (Thread.interrupted()) {
                    return;
                }
                String album = cursor.getString(cursor.getColumnIndex(projection[0]));
                getVideos(context, album);
//                String image = cursor.getString(cursor.getColumnIndex(projection[1]));

            } while (cursor.moveToPrevious());
        }
        cursor.close();
    }

    public List<String> getVideos(Context context, String bucket) {
        Cursor cursor = context.getContentResolver()
                .query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection2,
                        MediaStore.Video.Media.BUCKET_DISPLAY_NAME + " =?", new String[]{bucket}, MediaStore.Video.Media.DATE_ADDED);
        List<String> mVideosList = new ArrayList<>();
        if (cursor.moveToLast()) {
            do {
                if (Thread.interrupted()) {
                    return null;
                }
                String filePath = cursor.getString(cursor.getColumnIndex(projection2[1]));
//                Bitmap bmp = ThumbnailUtils.createVideoThumbnail(filePath,1);
                mVideosList.add(filePath);
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        return mVideosList;

    }


    public static String getCountrycode(Context context) {
        try {
            TelephonyManager manger = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            if (TextUtils.isEmpty(manger.getNetworkCountryIso())) {
                return manger.getNetworkCountryIso();
            } else {
                return manger.getSimCountryIso();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
