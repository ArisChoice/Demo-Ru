package com.app.rum_a.utils;

import android.content.Context;
import android.content.SharedPreferences;


import com.app.rum_a.core.RumApplication;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.users.model.QBUser;


/**
 * Created by Harish on 1/1/18.
 */
public class PreferenceManager {

    private static final String USER_SETTINGS = "userSettings";
    private final String PREFERENCE_NAME = "CGIT_PREFERENCES";
    private final String TUTORIAL_STATUS = "TutorialStatus";
    private final String DEVICE_ID = "device_id";
    private final String USER_DATA = "user_data";
    private final String BLOCKED_LIST = "blockedList";
    private final String SUBSCRIPTION_STATUS = "subscription_Status";
    private static Context context;
    private static PreferenceManager ourInstance;
    private final String USER_LOCATION = "user_location";
    private String FREE_KISS_COUNT = "free_kisses_count";
    private String FREE_WINKS_COUNT = "free_winks_count";

    public static PreferenceManager getInstance(Context context1) {
        PreferenceManager.context = context1;
        if (ourInstance == null) {
            ourInstance = new PreferenceManager();
        }
        return ourInstance;
    }

    private SharedPreferences getPreferences() {
        if (context != null)
            return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        else
            return RumApplication.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

   /* private SharedPreferences.Editor getEditor() {
        return getPreferences().edit();
    }*/

    /**
     * SharedStorage
     * SAVE STRING VALUES
     */
    public String getPrefrencesString(String key) {
        try {
            return getPreferences().getString(key, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setPrefrencesString(String key, String value) {
        getPreferences().edit().putString(key, value).apply();
    }

    /**
     * SAVE boolean VALUES
     */
    public boolean getPrefrencesBoolean(String key) {
        try {
            return getPreferences().getBoolean(key, false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setPrefrencesBoolean(String key, boolean value) {
        getPreferences().edit().putBoolean(key, value).apply();
    }

    public void setTutorialStatus(boolean isShown) {
        getPreferences().edit().putBoolean(TUTORIAL_STATUS, isShown).apply();
    }

    public boolean getTutorialStatus() {
        return getPreferences().getBoolean(TUTORIAL_STATUS, false);
    }

    public void setDeviceId(String deviceId) {
        getPreferences().edit().putString(DEVICE_ID, deviceId).apply();
    }

    public String getDeviceId() {
        return getPreferences().getString(DEVICE_ID, "");
    }

    public void setUserSettings(String userData) {

        getPreferences().edit().putString(USER_SETTINGS, userData).apply();
    }

    public String getUserSettings() {
        return getPreferences().getString(USER_SETTINGS, null);
    }



    public void setUserData(String userData) {

        getPreferences().edit().putString(USER_DATA, userData).apply();
    }

    public String getUserData() {
        return getPreferences().getString(USER_DATA, null);
    }


    public void setBlockedList(String blockedList) {
        getPreferences().edit().putString(BLOCKED_LIST, blockedList).apply();
    }

    public String getBlockedList() {
        return getPreferences().getString(BLOCKED_LIST, null);
    }

    /**
     * get and set subscription status.
     */
    public void setSubscriptionStatus(String blockedList) {
        getPreferences().edit().putString(SUBSCRIPTION_STATUS, blockedList).apply();
    }

    public String getSubscripion() {
        return getPreferences().getString(SUBSCRIPTION_STATUS, null);
    }

    public void clearUserData() {
        getPreferences().edit().remove(USER_DATA).apply();
        getPreferences().edit().remove(DEVICE_ID).apply();
        getPreferences().edit().remove(FREE_WINKS_COUNT).apply();
        getPreferences().edit().remove(FREE_KISS_COUNT).apply();
        clearBlockedList();
    }

    public void clearBlockedList() {
        getPreferences().edit().remove(BLOCKED_LIST).apply();
    }

    public void setUserLocation(String location) {
        getPreferences().edit().putString(USER_LOCATION, location).apply();
    }

    /**
     * Free trail restriction for kisses.
     */
    public void setKissCount(int kissCount) {
        getPreferences().edit().putInt(FREE_KISS_COUNT, kissCount).apply();
    }

    public Integer getKissCount() {
        return getPreferences().getInt(FREE_KISS_COUNT, 0);
    }

    /**
     * Free trail restriction for  winks.
     */
    public void setWinkCount(int winkCount) {
        getPreferences().edit().putInt(FREE_WINKS_COUNT, winkCount).apply();
    }

    public Integer getWinkCount() {
        return getPreferences().getInt(FREE_WINKS_COUNT, 0);
    }

    public double[] getUserLocation() {
        double[] location = new double[2];
        String userLocation = getPreferences().getString(USER_LOCATION, null);
        if (userLocation != null) {
            location[0] = Double.parseDouble(userLocation.split(",")[0]);
            location[1] = Double.parseDouble(userLocation.split(",")[1]);
        } else {
            location[0] = 0.0;
            location[1] = 0.0;
        }
        return location;
    }

    private static final String QB_USER_ID = "qb_user_id";
    private static final String QB_USER_LOGIN = "qb_user_login";
    private static final String QB_USER_PASSWORD = "qb_user_password";
    private static final String QB_USER_FULL_NAME = "qb_user_full_name";
    private static final String QB_USER_TAGS = "qb_user_tags";
    private static final String QB_USER_CUSTOM_DATA = "qb_user_custom_data";
    private static final String QB_FILE_ID = "file_id";
    private static final String DEVICE_TOKEN = "devicetoken";

    public void saveQbUser(QBUser qbUser) {
        save(QB_USER_ID, qbUser.getId());
        save(QB_USER_LOGIN, qbUser.getLogin());
        save(QB_USER_PASSWORD, qbUser.getPassword());
        save(QB_USER_FULL_NAME, qbUser.getFullName());
        save(QB_USER_TAGS, qbUser.getTags().getItemsAsString());
        save(QB_USER_CUSTOM_DATA, qbUser.getCustomData());
        if (qbUser.getFileId() != null) {
            save(QB_FILE_ID, qbUser.getFileId());
        }
    }

    public void save(String key, Object value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Enum) {
            editor.putString(key, value.toString());
        } else if (value != null) {
            throw new RuntimeException("Attempting to save non-supported preference");
        }

        editor.commit();
    }

    public boolean hasQbUser() {
        return has(QB_USER_LOGIN);
    }

    public boolean has(String key) {
        return getPreferences().contains(key);
    }

    public QBUser getQbUser() {
        if (hasQbUser()) {
            Integer id = get(QB_USER_ID);
            String login = get(QB_USER_LOGIN);
            String password = get(QB_USER_PASSWORD);
            String fullName = get(QB_USER_FULL_NAME);
            String tagsInString = get(QB_USER_TAGS);
            String customString = get(QB_USER_CUSTOM_DATA);
            Integer fileid = get(QB_FILE_ID);

            StringifyArrayList<String> tags = null;

            if (tagsInString != null) {
                tags = new StringifyArrayList<>();
                tags.add(tagsInString.split(","));
            }

            QBUser user = new QBUser(login, password);
            user.setId(id);
            user.setFullName(fullName);
            user.setTags(tags);
            user.setCustomData(customString);
            if (fileid != null) {
                user.setFileId(fileid);
            }
            return user;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) getPreferences().getAll().get(key);
    }

    public String getDeviceToken() {
        return getPreferences().getString(DEVICE_TOKEN, "");
    }

   /* public void saveFileId(QBUser qbUser) {
        save(QB_FILE_ID, qbUser.getFileId());
    }*/
}
