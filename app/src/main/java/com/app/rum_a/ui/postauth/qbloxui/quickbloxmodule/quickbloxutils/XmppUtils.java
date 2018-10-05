package com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.quickbloxutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.*;
import android.util.Log;

import com.app.rum_a.R;
import com.app.rum_a.core.MyApplication;
import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.ChatModel;
import com.google.gson.Gson;

import com.quickblox.videochat.webrtc.QBRTCConfig;
import com.quickblox.videochat.webrtc.QBRTCMediaConfig;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * Created by tarun on 20/7/15.
 */
public class XmppUtils {


    private static final String TAG = "  :: XmppUtils ::";

    private static void setSettingsForMultiCall(List<Integer> users) {
        if (users.size() <= 4) {
            setDefaultVideoQuality();
        } else {
            //set to minimum settings
            QBRTCMediaConfig.setVideoWidth(QBRTCMediaConfig.VideoQuality.QBGA_VIDEO.width);
            QBRTCMediaConfig.setVideoHeight(QBRTCMediaConfig.VideoQuality.QBGA_VIDEO.height);
            QBRTCMediaConfig.setVideoHWAcceleration(false);
            QBRTCMediaConfig.setVideoCodec(null);
        }
    }

    public static void setSettingsStrategy(List<Integer> users, SharedPreferences sharedPref, Context context) {
        setCommonSettings(sharedPref, context);
        if (users.size() == 1) {
            setSettingsFromPreferences(sharedPref, context);
        } else {
            setSettingsForMultiCall(users);
        }
    }

    private static void setCommonSettings(SharedPreferences sharedPref, Context context) {
        String audioCodecDescription = getPreferenceString(sharedPref, context, com.quickblox.sample.groupchatwebrtc.R.string.pref_audiocodec_key,
                com.quickblox.sample.groupchatwebrtc.R.string.pref_audiocodec_def);
        QBRTCMediaConfig.AudioCodec audioCodec = QBRTCMediaConfig.AudioCodec.ISAC.getDescription()
                .equals(audioCodecDescription) ?
                QBRTCMediaConfig.AudioCodec.ISAC : QBRTCMediaConfig.AudioCodec.OPUS;
        Log.e(TAG, "audioCodec =: " + audioCodec.getDescription());
        QBRTCMediaConfig.setAudioCodec(audioCodec);
        Log.v(TAG, "audioCodec = " + QBRTCMediaConfig.getAudioCodec());
        // Check Disable built-in AEC flag.
        boolean disableBuiltInAEC = getPreferenceBoolean(sharedPref, context,
                com.quickblox.sample.groupchatwebrtc.R.string.pref_disable_built_in_aec_key,
                com.quickblox.sample.groupchatwebrtc.R.string.pref_disable_built_in_aec_default);

        QBRTCMediaConfig.setUseBuildInAEC(!disableBuiltInAEC);
        Log.v(TAG, "setUseBuildInAEC = " + QBRTCMediaConfig.isUseBuildInAEC());
        // Check Disable Audio Processing flag.
        boolean noAudioProcessing = getPreferenceBoolean(sharedPref, context,
                com.quickblox.sample.groupchatwebrtc.R.string.pref_noaudioprocessing_key,
                com.quickblox.sample.groupchatwebrtc.R.string.pref_noaudioprocessing_default);
        QBRTCMediaConfig.setAudioProcessingEnabled(!noAudioProcessing);
        Log.v(TAG, "isAudioProcessingEnabled = " + QBRTCMediaConfig.isAudioProcessingEnabled());
        // Check OpenSL ES enabled flag.
        boolean useOpenSLES = getPreferenceBoolean(sharedPref, context,
                com.quickblox.sample.groupchatwebrtc.R.string.pref_opensles_key,
                com.quickblox.sample.groupchatwebrtc.R.string.pref_opensles_default);
        QBRTCMediaConfig.setUseOpenSLES(useOpenSLES);
        Log.v(TAG, "isUseOpenSLES = " + QBRTCMediaConfig.isUseOpenSLES());
    }

    private static void setSettingsFromPreferences(SharedPreferences sharedPref, Context context) {

        // Check HW codec flag.
        boolean hwCodec = sharedPref.getBoolean(context.getString(com.quickblox.sample.groupchatwebrtc.R.string.pref_hwcodec_key),
                Boolean.valueOf(context.getString(com.quickblox.sample.groupchatwebrtc.R.string.pref_hwcodec_default)));

        QBRTCMediaConfig.setVideoHWAcceleration(hwCodec);

        // Get video resolution from settings.
        int resolutionItem = Integer.parseInt(sharedPref.getString(context.getString(com.quickblox.sample.groupchatwebrtc.R.string.pref_resolution_key),
                "0"));
        Log.e(TAG, "resolutionItem =: " + resolutionItem);
        setVideoQuality(resolutionItem);
        Log.v(TAG, "resolution = " + QBRTCMediaConfig.getVideoHeight() + "x" + QBRTCMediaConfig.getVideoWidth());

        // Get start bitrate.
        int startBitrate = getPreferenceInt(sharedPref, context,
                com.quickblox.sample.groupchatwebrtc.R.string.pref_startbitratevalue_key,
                com.quickblox.sample.groupchatwebrtc.R.string.pref_startbitratevalue_default);
        Log.e(TAG, "videoStartBitrate =: " + startBitrate);
        QBRTCMediaConfig.setVideoStartBitrate(startBitrate);
        Log.v(TAG, "videoStartBitrate = " + QBRTCMediaConfig.getVideoStartBitrate());

        int videoCodecItem = Integer.parseInt(getPreferenceString(sharedPref, context, com.quickblox.sample.groupchatwebrtc.R.string.pref_videocodec_key, "0"));
        for (QBRTCMediaConfig.VideoCodec codec : QBRTCMediaConfig.VideoCodec.values()) {
            if (codec.ordinal() == videoCodecItem) {
                Log.e(TAG, "videoCodecItem =: " + codec.getDescription());
                QBRTCMediaConfig.setVideoCodec(codec);
                Log.v(TAG, "videoCodecItem = " + QBRTCMediaConfig.getVideoCodec());
                break;
            }
        }
        // Get camera fps from settings.
        int cameraFps = getPreferenceInt(sharedPref, context, com.quickblox.sample.groupchatwebrtc.R.string.pref_frame_rate_key, com.quickblox.sample.groupchatwebrtc.R.string.pref_frame_rate_default);
        Log.e(TAG, "cameraFps = " + cameraFps);
        QBRTCMediaConfig.setVideoFps(cameraFps);
        Log.v(TAG, "cameraFps = " + QBRTCMediaConfig.getVideoFps());
    }

    public static void configRTCTimers(Context context) {
        SharedPreferences sharedPref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);

        long answerTimeInterval = getPreferenceInt(sharedPref, context,
                R.string.pref_answer_time_interval_key,
                R.string.pref_answer_time_interval_default_value);
        QBRTCConfig.setAnswerTimeInterval(answerTimeInterval);
        Log.e(TAG, "answerTimeInterval = " + answerTimeInterval);

        int disconnectTimeInterval = getPreferenceInt(sharedPref, context,
                R.string.pref_disconnect_time_interval_key,
                R.string.pref_disconnect_time_interval_default_value);
        QBRTCConfig.setDisconnectTime(disconnectTimeInterval);
        Log.e(TAG, "disconnectTimeInterval = " + disconnectTimeInterval);

        long dialingTimeInterval = getPreferenceInt(sharedPref, context,
                R.string.pref_dialing_time_interval_key,
                R.string.pref_dialing_time_interval_default_value);
        QBRTCConfig.setDialingTimeInterval(dialingTimeInterval);
        Log.e(TAG, "dialingTimeInterval = " + dialingTimeInterval);
    }


    private static void setVideoQuality(int resolutionItem) {
        if (resolutionItem != -1) {
            setVideoFromLibraryPreferences(resolutionItem);
        } else {
            setDefaultVideoQuality();
        }
    }

    private static void setDefaultVideoQuality() {
        QBRTCMediaConfig.setVideoWidth(QBRTCMediaConfig.VideoQuality.VGA_VIDEO.width);
        QBRTCMediaConfig.setVideoHeight(QBRTCMediaConfig.VideoQuality.VGA_VIDEO.height);
    }

    private static void setVideoFromLibraryPreferences(int resolutionItem) {
        for (QBRTCMediaConfig.VideoQuality quality : QBRTCMediaConfig.VideoQuality.values()) {
            if (quality.ordinal() == resolutionItem) {
                Log.e(TAG, "resolution =: " + quality.height + ":" + quality.width);
                QBRTCMediaConfig.setVideoHeight(quality.height);
                QBRTCMediaConfig.setVideoWidth(quality.width);
            }
        }
    }

    private static String getPreferenceString(SharedPreferences sharedPref, Context context, int strResKey, int strResDefValue) {
        return sharedPref.getString(context.getString(strResKey), context.getString(strResDefValue));
    }

    private static String getPreferenceString(SharedPreferences sharedPref, Context context, int strResKey, String strResDefValue) {
        return sharedPref.getString(context.getString(strResKey), strResDefValue);
    }

    public static int getPreferenceInt(SharedPreferences sharedPref, Context context, int strResKey, int strResDefValue) {
        return sharedPref.getInt(context.getString(strResKey), Integer.valueOf(context.getString(strResDefValue)));
    }

    private static boolean getPreferenceBoolean(SharedPreferences sharedPref, Context context, int StrRes, int strResDefValue) {
        return sharedPref.getBoolean(context.getString(StrRes), Boolean.valueOf(context.getString(strResDefValue)));
    }

    public static boolean checkBlockStatus(String gcmdata) {

//        ChatModel.Gcm chatModel = new Gson().fromJson(gcmdata, ChatModel.Gcm.class);
//        String blockedUsersData = PreferenceManager.getInstance(MyApplication.getInstance()).getPrefrencesString(Constants.ValueKeys.BLOCKED_DATA);
//        if (blockedUsersData != null) {
//            BlockedUserModel dataModel = new Gson().fromJson(blockedUsersData, BlockedUserModel.class);
//            if (dataModel != null && dataModel.getResult() != null && dataModel.getResult().getBlockedByMe() !=
//                    null && dataModel.getResult().getBlockedByMe().size() > 0) {
//                for (int i = 0; i < dataModel.getResult().getBlockedByMe().size(); i++) {
//                    if (dataModel.getResult().getBlockedByMe().get(i).getQBUserId().equals(chatModel.getNOTI_ID().toString())) {
//                        Log.e(TAG, " Kissa  , From ::::::: P U S H  " + " checkBlockStatus   " + true);
//                        return true;
//                    }
//                }
//            }
//        }
        return false;
    }

    public interface XmppConstants {
        String SERVICE_NAME = "@ip-172-31-17-30.us-east-2.compute.internal";//"@ip-172-31-3-80.us-west-2.compute.internal";/*"@dev.localdomain.com"*/;//"@openfire.localhost.com";
        String HOST = "52.14.223.215";//"35.161.83.81";//"52.11.206.84";//"192.168.1.108";
        String PORT = "5222";
        String IMAGE = "URL";
        int PRESENCE_CHANGE = 1;
        int RECEIVE_MESSAGE = 2;
        int DELIVER_MESSAGE = 3;
        int READ_MESSAGE = 4;
        String LookingFor = "LookingFor";
    }

    public interface QuickBloxConstants {
        String APP_ID = "69420";
        String AUTH_KEY = "3FDT2wkrPVdV5Fs";
        String AUTH_SECRET = "ZkDRKeXmB2jVyDy";
        String ACCOUNT_KEY = "961";
        String USER_GLOBAL_PASSWORD = "KissaQuickbloxPassword";
    }

    public static String getUserIdFromJID(String jId) {
        try {
            Log.e(" getUserIdFromJID ", " :: " + jId);
            return jId.split("@")[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getJIdFromUserId(String userId) {
        return userId + XmppConstants.SERVICE_NAME;
    }


    public static String getChatId(String userId, String anotherUserId) {
        return userId + ":::" + anotherUserId;
    }


    public static String getAnotherUserIdFromChatId(String chatId) {
        return chatId.split(":::")[1];
    }


   /* public static void loadVCard(XMPPConnection conn, UserData userData) {
        VCard vcard = new VCard();
        try {
            ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp", new VCardProvider());
            vcard.load(conn);
            try {
                vcard.setNickName(userData.getNickName());
                vcard.setEmailHome(userData.getEmail());
                vcard.setField(XmppConstants.LookingFor, userData.getLookingFor().toString());
                vcard.setField(XmppConstants.IMAGE, userData.getProfileImage());
                vcard.save(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }*/


  /*  public static VCard getVcard(XMPPConnection connection, String userName) {
        VCard vCard = new VCard();
        try {
            ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp", new VCardProvider());
            vCard.load(connection, getJIdFromUserId(userName));
            return vCard;
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /*public static boolean isUserRegister(String userId) {

        XMPPConnection mXMPPConnection = XmppConnectionService.connection;
        String Jid = getJIdFromUserId(userId);
        Log4Android.e("", "JabberId:" + Jid + ",ServiceName:" + mXMPPConnection.getServiceName());
        ProviderManager.getInstance().addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
        UserSearchManager search = new UserSearchManager(mXMPPConnection);
        Form searchForm = null;
        try {
            searchForm = search.getSearchForm("search." + mXMPPConnection.getServiceName());
            Form answerForm = searchForm.createAnswerForm();
            answerForm.setAnswer("Username", true);
            answerForm.setAnswer("search", userId);
            ReportedData data = search
                    .getSearchResults(answerForm, "search." + mXMPPConnection.getServiceName());
            if (data.getRows() != null) {
                return true;
            } else {
                return false;
            }
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        return false;
    }
*/
    public static String getReadMessageId(String xml) {

        XmlPullParser parser = null;
        String id = null;
        try {
            StringReader sr = new StringReader(xml);
            XmlPullParserFactory xmlPullParser = XmlPullParserFactory.newInstance();
            parser = xmlPullParser.newPullParser();
            parser.setInput(sr);


            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.d("paarse", "Start document");
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equals("read")) {
                            id = parser.getAttributeValue(null, "id");
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        name = parser.getName();

                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;

    }
}
