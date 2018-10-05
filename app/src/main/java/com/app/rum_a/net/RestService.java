package com.app.rum_a.net;


import com.app.rum_a.model.modelutils.AddressResponse;
import com.app.rum_a.model.modelutils.SwipeOperationRequestModel;
import com.app.rum_a.model.req.RegisterRequestModel;
import com.app.rum_a.model.req.UpdateUserLocation;
import com.app.rum_a.model.resp.AddPropertyResponseModel;
import com.app.rum_a.model.resp.ChatUsersModelResponse;
import com.app.rum_a.model.resp.DefaultResponse;
import com.app.rum_a.model.resp.PropertyDetailResponseModel;
import com.app.rum_a.model.resp.PropertyListResponseModel;
import com.app.rum_a.model.resp.SaveChatInstanceModel;
import com.app.rum_a.model.resp.UserResponseModel;
import com.app.rum_a.model.resp.UserSettingsModel;
import com.app.rum_a.utils.NetworkConstatnts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Harish on 2/3/15.
 */
public interface RestService {

//    @POST(AppConstants.API.LOGIN)
//    void login(@Body LoginRequestModel loginModel, RestProcess<LoginResponseModel> callback);
//
//    @POST(SIGN_UP)
//    void register(@Body RegisterRequestModel registerRequestModel, RestProcess<LoginResponseModel> callback);
//
//    @Multipart
//    @POST(UPLOAD_IMAGE)
//    void uploadImage(@Query("lookingForId") String lookingForId, @Part("image") TypedFile image, RestProcess<LoginResponseModel> callback);
//
//    @Multipart
//    @POST(UPLOAD_IMAGE)
//    void uploadImageTemp(@Query("HasProfilePic") boolean val, @Query("lookingForId") String lookingForId, @Part("image") TypedFile image, RestProcess<LoginResponseModel> callback);

    //    @POST(NetworkConstatnts.API.SIGNUP)
//    Call<DefaultResponse> signInRequest(@Body RegisterRequestModel registerRequestModel);
    @FormUrlEncoded
    @POST(NetworkConstatnts.API.SIGNUP)
    Call<UserResponseModel> signUpRequest(@Field(NetworkConstatnts.Params.firstName) String firstName,
                                          @Field(NetworkConstatnts.Params.lastName) String lastName,
                                          @Field(NetworkConstatnts.Params.nickname) String nickNme,
                                          @Field(NetworkConstatnts.Params.email) String email,
                                          @Field(NetworkConstatnts.Params.password) String password,
                                          @Field(NetworkConstatnts.Params.lookingType) int lookingType,
                                          @Field(NetworkConstatnts.Params.seekingType) int seekingType);

    @FormUrlEncoded
    @POST(NetworkConstatnts.API.SIGNUP)
    Call<UserResponseModel> signUpRequest(@FieldMap Map<String, String> fields);

    @FormUrlEncoded


    @POST(NetworkConstatnts.API.SIGNIN)
    Call<UserResponseModel> signInRequest(@Field(NetworkConstatnts.Params.email) String email,
                                          @Field(NetworkConstatnts.Params.password) String password);

    @Multipart
    @POST(NetworkConstatnts.API.addProperty)
    Call<AddPropertyResponseModel> uploadProperty(@PartMap() Map<String, RequestBody> params,
                                                  @Part List<MultipartBody.Part> file);
    @Multipart
    @POST(NetworkConstatnts.API.updateProperty)
    Call<AddPropertyResponseModel> updateProperty(@PartMap() Map<String, RequestBody> params,
                                                  @Part List<MultipartBody.Part> file);

    @FormUrlEncoded
    @POST(NetworkConstatnts.API.GET_PROPERTY)
    Call<PropertyListResponseModel> getProperty(@Field(NetworkConstatnts.Params.userId_) String userId);

    @Multipart
    @POST(NetworkConstatnts.API.uploadProfileImage)
    Call<DefaultResponse> uploadProfileImage(@PartMap() Map<String, RequestBody> params,
                                             @Part MultipartBody.Part file);

    //    @FormUrlEncoded
    @GET(NetworkConstatnts.API.GET_PROFILE)
    Call<UserResponseModel> getProfile(@Query("userId") String userId);

    @FormUrlEncoded
    @POST(NetworkConstatnts.API.UPDATE_LOCATION)
    Call<DefaultResponse> updateUserLocation(@Field("userId") String userId,
                                             @Field(NetworkConstatnts.Params.lat) String lat,
                                             @Field(NetworkConstatnts.Params.lang) String lang);

    @FormUrlEncoded
    @POST(NetworkConstatnts.API.UPDATE_PROFILE_DETAIL)
    Call<DefaultResponse> updateProfileDetail(@Field("userId") String userId,
                                              @Field(NetworkConstatnts.Params.firstName) String s,
                                              @Field(NetworkConstatnts.Params.lastName) String s1,
                                              @Field(NetworkConstatnts.Params.username) String s3,
                                              @Field(NetworkConstatnts.Params.about) String s2);

    @FormUrlEncoded
    @POST(NetworkConstatnts.API.GET_SETTINGS)
    Call<UserSettingsModel> getUserSettings(@Field("userId") String userId);

    @FormUrlEncoded
    @POST(NetworkConstatnts.API.UPDATE_USER_SETTINGS)
    Call<UserSettingsModel> updateUserSettings(@Field("UserId") String userId,
                                               @Field("address") String locationAddress,
                                               @Field("latitude") String locationLatitude,
                                               @Field("longitude") String locationLongitude,
                                               @Field("DistanceType") String distanceType,
                                               @Field("MaxLocation") String maxLocation,
                                               @Field("StartRangePrice") String startRangePrice,
                                               @Field("EndRangePrice") String endRangePrice,
                                               @Field("seekingType") int seekingType,
                                               @Field("lookingType") int lookingType,
                                               @Field("Currency") String currency,
                                               @Field("PrivateAccount") boolean privateAccount,
                                               @Field("PushNotification") boolean pushNotification);

    @GET
    Call<AddressResponse> getAddress(@Url String fileUrl);


    @FormUrlEncoded
    @POST(NetworkConstatnts.API.UPDATE_LOCATION)
    Call<DefaultResponse> updateUserLocation(@Field("userId") String userId,
                                             @Field(NetworkConstatnts.Params.lat) String latitude,
                                             @Field(NetworkConstatnts.Params.lang) String longitude,
                                             @Field(NetworkConstatnts.Params.address) String locationAddress);

    //    @Headers("Content-Type: application/json")
    @POST(NetworkConstatnts.API.UPDATE_LOCATION)
    Call<DefaultResponse> updateUserLocation(@Body UpdateUserLocation locationModel);

    @GET(NetworkConstatnts.API.HOME_LISTING_PROPERY)
    Call<PropertyListResponseModel> getHomeProperty();

    //    @FormUrlEncoded
    @GET(NetworkConstatnts.API.LIKE_PROPERTY)
    Call<DefaultResponse> likeProperty(@Query("userId") String userId,
                                       @Query("proprtyid") String s);

    //    @FormUrlEncoded
    @GET(NetworkConstatnts.API.DISLIKE_PROPERTY)
    Call<DefaultResponse> dislikeProperty(@Query("userId") String userId,
                                          @Query("proprtyid") String s);

    //    @Headers("Content-Type: application/json")
    @GET(NetworkConstatnts.API.LIKE_PROPERTY)
    Call<DefaultResponse> likeProperty(@Body SwipeOperationRequestModel swipeRequest);

    @GET(NetworkConstatnts.API.DELETE_PROPERTY)
    Call<PropertyListResponseModel> deleteProperty(@Query("userId") String userId,
                                                   @Query("proprtyid") int propertyID);

    @GET(NetworkConstatnts.API.LIKED_PROPERTIES_LIST)
    Call<PropertyListResponseModel> getLikedProperty(@Query("userId") String userId);

    @FormUrlEncoded
    @POST(NetworkConstatnts.API.SAVE_QB_DETAIL)
    Call<UserResponseModel> saveUserRequest(@Field("UserId") String userId,
                                            @Field("QbFirstName") String firstName,
                                            @Field("QbLastName") String lastName,
                                            @Field("QbId") String id,
                                            @Field("QbImage") String password);

    @FormUrlEncoded
    @POST(NetworkConstatnts.API.SAVE_CHAT_DIALOG)
    Call<SaveChatInstanceModel> seveChatDialog(@Field("PropertyId") String propertyId,
                                               @Field("OwnerId") String ownerId,
                                               @Field("ChatDialog") String chatDialog,
                                               @Field("SenderId") String senderId,
                                               @Field("ReceiverId") String receiverId,
                                               @Field("ChatTime") long chatTime,
                                               @Field("RecentMessage") String recentMessage);


    @GET(NetworkConstatnts.API.GET_CHAT_DIALOGS)
    Call<ChatUsersModelResponse> getPropertyChatDialog(@Query("propertyId") int propertyId,
                                                       @Query("ownerId") Integer ownerId);

    @GET(NetworkConstatnts.API.GET_PROPERTY_DETAIL)
    Call<PropertyDetailResponseModel> getPropertyDetail(@Query("PropertyId") int propertyID,
                                                        @Query("userId") int userID);

    @GET(NetworkConstatnts.API.REMOVE_LIKED_PROPERTY)
    Call<DefaultResponse> removeLikedProperty(@Query("propertyid") String propertyID,
                                              @Query("userId") String userId);
    @FormUrlEncoded
    @POST(NetworkConstatnts.API.SEND_FEEDBACK)
    Call<DefaultResponse> sendFeedback(@Field("userId")String s,
                                       @Field("FeedbackMessage")String s1);
    @FormUrlEncoded
    @POST(NetworkConstatnts.API.DELETE_ACCOUNT)
    Call<DefaultResponse> deleteAccount(@Field("userId")String userId);
}