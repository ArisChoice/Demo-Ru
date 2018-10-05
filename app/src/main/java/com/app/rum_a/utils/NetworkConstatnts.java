package com.app.rum_a.utils;

/**
 * Created by harish on 24/8/18.
 */

public interface NetworkConstatnts {
    String STATUS_FAILED = "failed";
    String STATUS_SUCCESS = "success";

    interface ResponseCode {
        int success = 201;
        int sessionExpred = 203;
    }

    interface URL {
        String BASE_URL = "";
    }

    interface KEYS {
        String secretKey = "B329F8C72242D5BFD925129E4592A1BD2B8C5E5B";
        String deviceType = "DeviceType";
        String uniqueDeviceId = "UniqueDeviceId";
        String deviceId = "DeviceID";
        String TimeStamp = "TimeStamp";
        String timezone = "timezone";
        String deviceToken = "DeviceID";
        String userId = "userId";
        String sessionId = "SessionId";
        String ClientHash = "ClientHash";


    }

    interface API {
        String ABOUT_US = URL.BASE_URL + "Page/AboutUs";
        String FAQ = URL.BASE_URL + "Page/FAQ";
        String TERMS = URL.BASE_URL + "Page/TermsAndConditions";


        String SIGNUP = "api/Account/SignUp";
        String SIGNIN = "api/Account/login";
        String addProperty = "api/Property/AddProperty";
        String GET_PROPERTY = "api/Property/GetPropertyByLocationOrUserID";

        String uploadProfileImage = "api/Account/UpdateProfilePics";
        String GET_PROFILE = "api/Account/GetUserProfile";
        String UPDATE_LOCATION = "api/Account/UpdateUserLocation";
        String UPDATE_PROFILE_DETAIL = "api/Account/EditUserProfile";
        String GET_SETTINGS = "api/account/GetUserSettings";
        String UPDATE_USER_SETTINGS = "api/Account/UpdateUserSettings";
        String HOME_LISTING_PROPERY = "api/Property/GetPropertyByUserFilter";
        String LIKE_PROPERTY = "api/Property/AddLikedProperty";
        String DISLIKE_PROPERTY = "api/Property/AddDislikedProperty";
        String DELETE_PROPERTY = "api/Property/DeleteProperty";
        String LIKED_PROPERTIES_LIST = "api/Property/GetUserLikedPropertyList";
        String SAVE_QB_DETAIL = "api/Account/SaveQBDetails";
        String SAVE_CHAT_DIALOG = "api/Chat/SaveChat";
        String GET_CHAT_DIALOGS = "api/Chat/GetOwnerChatslist";
        String GET_PROPERTY_DETAIL = "api/Property/GetPropertyDetails";
        String REMOVE_LIKED_PROPERTY = "api/Property/RemoveLikedProperty";
        String SEND_FEEDBACK = "api/Account/AddFeedback";
        String updateProperty = "api/Property/UpdateProperty";
        String DELETE_ACCOUNT = "api/Account/DeleteUserAccount";
    }

    interface Params {
        String firstName = "firstName";
        String email = "Email";
        String lastName = "lastName";
        String password = "Password";
        String lookingType = "lookingType";
        String seekingType = "seekingType";
        String nickname = "NickName";
        String googleId = "GoogleId";
        String facebookId = "FacebookId";


        String name = "name";
        String address = "address";
        String location = "location";
        String latitude = "latitude";
        String longitude = "longitude";
        String price = "price";
        String propertyType = "PropertyType";
        String sellingType = "SellingType";
        String description = "description";
        String images = "PropertyPic";
        String userId = "FKUserID";
        String userId_ = "userID";
        String imaeType = "ImageType";
        String image = "image";
        String lat = "Lat";
        String lang = "Long";
        String about = "AboutMe";
        String username = "UserName";
        String currency = "Currency";
        String sellingTypeNew = "ForRentOrBuy";
        String propertyID = "PropertyID";
    }
}
