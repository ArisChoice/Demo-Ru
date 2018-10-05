package com.app.rum_a.utils;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by harish on 21/8/18.
 */

public class AppConstants {


    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    public static final String STATIC_FB_EMAIL = "@facebook.com";

    public interface RequestCode {

        int RC_SIGN_IN = 131;
        int GOOGLE_SOCIAL = 1001;
        int APAPTER_BOTTOM_DIALOG_CLICK = 1002;
        int PERMISSIONS_REQUEST_CAMERA = 1003;
        int REQUEST_TAKE_IMAGE = 1004;
        int PLACE_PICKER_REQUEST = 1005;
        int GALLERY_REQUEST = 1006;
        int FACEBOOK_SOCIAL = 1007;
        int CHAT_DIALOG_NOTIFY = 1008;
        int EDIT_PROPERTY_REQUEST = 1009;

    }

    public interface LocationRequest {
        public static final int SUCCESS_RESULT = 0;
        public static final int FAILURE_RESULT = 1;
        public static final String PACKAGE_NAME =
                "com.app.rum_a";
        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
        public static final String RESULT_DATA_KEY = PACKAGE_NAME +
                ".RESULT_DATA_KEY";
        public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
                ".LOCATION_DATA_EXTRA";

    }

    private AppConstants() {
        // This utility class is not publicly instantiable
    }

    public interface TimeLimits {
        long LITTLE = 20;
        long SMALL = 100;
        long MEDIUM = 500;
        long LONG = 2000;
        long VERY_LONG = 5000;
    }

    public interface ServiceModes {
        int SIGN_UP_REQUEST = 101;
        int SIGN_IN_REQUEST = 102;
        int UPLOAD_PROPERTY = 103;
        int GET_PROPERTIES = 104;
        int UPLOAD_PROFILE_IMAGE = 105;
        int GET_PROFILE = 106;
        int UPDATE_LOCATION = 107;

        int UPDATE_PROFILE_DETAIL = 108;

        int GET_SETTINGS = 109;

        int UPDATE_SETTINGS = 110;
        int SERVICE_MODE_GET_NEAR_BY_RESULT = 111;
        int GET_HOME_PROPERTIES = 112;
        int LIKE_PROPERTY = 113;
        int DISLIKE_PROPERTY = 114;
        int DELETE_PROPERTY = 115;
        int GET_LIKED_PROPERTIES = 116;
        int SAVE_QBDETAIL_REQUEST = 117;
        int SAVE_CHAT_DIALOG = 118;
        int GET_CHAT_DIALOGS = 119;
        int GET_PROPERTY_DETAIL = 120;
        int REMOVE_LIKED_PROPERTIES = 121;
        int SAVE_FEEDBACK = 122;
        int DELETE_ACCOUNT = 123;
    }

    public interface ForRentOrBuy {/*Selling Types New:-  Rent = 0, Buy = 1*/
        int Rent = 0;
        int Buy = 1;
    }

    public interface Seekingtype {/*Seeking Types:-  House = 0, Apartment = 1, Commercial_Space = 2,Land = 3*/
        int House = 0;
        int Apartment = 1;
        int Commercial_Space = 2;
        int Land = 3;
    }

    public interface SeekingtypeStrings {/*Seeking Types:-  House = 0, Apartment = 1, Commercial_Space = 2,Land = 3*/
        String House = "House";
        String Apartment = "Apartment";
        String Commercial_Space = "Commercial Space";
        String Land = "Land";
    }

    public interface LookingTypes {   /*lookingTypes:- Rent = 0,Buy = 1, Lease = 2,Sell = 3*/
        int Rent = 0;
        int Buy = 1;
        int Lease = 2;
        int Sell = 3;
    }

    public interface LookingTypesStrings {   /*lookingTypes:- Rent = 0,Buy = 1, Lease = 2,Sell = 3*/
        String Rent = "Rent";
        String Buy = "Buy";
        String Lease = "Lease";
        String Sell = "Sell";
    }

    public interface CurrencySymbols {
        String USD = "$";
        String AUD = "$";
        String SGD = "$";
        String Euro = "€";
        String GBP = "£";
        String IDR = "Rp";
        String CNY = "¥";
        String JPY = "¥";
        String RUB = "р.";
        String INR = "₨";
        String KRW = "₩";
    }

    public interface CurrencyTypes {
        String USD = "USD";
        String AUD = "AUD";
        String SGD = "SGD";
        String Euro = "Euro";
        String GBP = "GBP";
        String IDR = "IDR";
        String CNY = "CNY";
        String JPY = "JPY";
        String RUB = "RUB";
        String INR = "INR";
        String KRW = "KRW";
    }

    public interface ParmsType {
        String seekingType = "seekingType";
        String lookingType = "lookingType";
        String isLogedIn = "isUserLogedin";
        String PROPERTY_DETAIL = "propertyDetail";
        String USER_ADDRESS = "userAddress";
        String USER_ID = "userId";
        String USER_NAME = "userName";
        String isAlreadyChatted = "alreadyChat";
        String PROPERTY_ID = "propertyId";
        String USER_IMAGE = "userImage";
        String IS_EDIT = "isEdit";
        String WEB_URL = "webUrl";
    }

    public interface DeviceType {
        String Website = "1";
        String Android = "2";
        String IPhone = "3";
    }

    public interface SwipeType {
        int CROSS = 1;
        int LIKE = 2;
        int CLICK = 0;
    }

    public interface Clickerations {
        int FULL_VIEW_CLICK = 01;
        int IMAGE_VIEW_CLICK = 03;
        int OPTION_VIEW_CLICK = 04;
        int REMOVE_VIEW_CLICK = 05;
    }

    public interface QuickBloxConstants {
        String USER_GLOBAL_PASSWORD = "12345678RUM_A";
    }
}
