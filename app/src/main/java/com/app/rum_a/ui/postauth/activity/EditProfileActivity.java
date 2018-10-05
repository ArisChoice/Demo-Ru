package com.app.rum_a.ui.postauth.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.rum_a.BuildConfig;
import com.app.rum_a.R;
import com.app.rum_a.core.BaseActivity;
import com.app.rum_a.core.RumApplication;
import com.app.rum_a.di.DaggerValues;
import com.app.rum_a.model.resp.DefaultResponse;
import com.app.rum_a.model.resp.UserResponseModel;
import com.app.rum_a.net.RestCallback;
import com.app.rum_a.net.RestProcess;
import com.app.rum_a.net.RestService;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.ImageUtility;
import com.app.rum_a.utils.NetworkConstatnts;
import com.app.rum_a.utils.PreferenceManager;
import com.app.rum_a.utils.appinterface.OnBottomDialogItemListener;
import com.app.rum_a.utils.views.RumTextView;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by harish on 11/9/18.
 */

public class EditProfileActivity extends BaseActivity implements RestCallback, OnBottomDialogItemListener {
    @Named(DaggerValues.AUTH)
    @Inject
    RestService apiInterface;
    @Inject
    PreferenceManager mPref;
    @Inject
    CommonUtils cUtils;
    @BindView(R.id.back_toolbar)
    ImageView backToolbar;
    @BindView(R.id.txt_title_toolbar)
    RumTextView txtTitleToolbar;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.profile_image_one)
    CircleImageView profileImageOne;
    @BindView(R.id.profile_image_two)
    CircleImageView profileImageTwo;
    @BindView(R.id.profile_image_three)
    CircleImageView profileImageThree;
    @BindView(R.id.edTxt_name)
    EditText edTxtName;
    @BindView(R.id.edTxt_about)
    EditText edTxtAbout;
    @BindView(R.id.edTxt_last_name)
    EditText edTxtLastName;
    private String outputFile;
    private int whichImage;

    @Override
    public int getLayoutId() {
        return R.layout.edit_profile_screen;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RumApplication) getApplication()).getMyComponent().inject(this);
        txtTitleToolbar.setText(R.string.str_edit_profile);
        edTxtName.setText(getUserDeatil().getFirstName());
        edTxtLastName.setText(getUserDeatil().getLastName());
//        setUserData(userResponse);
        getUserDetail();
    }

    private void getUserDetail() {
        String userId = String.valueOf(new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId());
        Call<UserResponseModel> call = apiInterface.getProfile(userId);
        call.enqueue(new RestProcess<UserResponseModel>(AppConstants.ServiceModes.GET_PROFILE, this, EditProfileActivity.this, false));
    }

    private void setUserData(UserResponseModel userResponse) {
        edTxtName.setText(userResponse.getResult().getUser().getFirstName());
        edTxtLastName.setText(userResponse.getResult().getUser().getLastName());
        if (userResponse.getResult().getUser().getAboutMe() != null && !userResponse.getResult().getUser().getAboutMe().equals(""))
            edTxtAbout.setText(userResponse.getResult().getUser().getAboutMe());
        try {
            List<UserResponseModel.ResultBean.ProfilePic> userImages = userResponse.getResult().getUser().getProfilePics();
            new ImageUtility(EditProfileActivity.this).LoadImage(CommonUtils.getValidUrl(userImages.get(0).getImageURL()), profileImageOne);
            new ImageUtility(EditProfileActivity.this).LoadImage(CommonUtils.getValidUrl(userImages.get(1).getImageURL()), profileImageTwo);
            new ImageUtility(EditProfileActivity.this).LoadImage(CommonUtils.getValidUrl(userImages.get(2).getImageURL()), profileImageThree);
        } catch (Exception e) {
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, int serviceMode) {

    }

    @Override
    public void onSuccess(Call call, Response model, int serviceMode) {
        DefaultResponse responseData;
        switch (serviceMode) {
            case AppConstants.ServiceModes.UPLOAD_PROFILE_IMAGE:
                responseData = (DefaultResponse) model.body();
                if (responseData.getStatus() == NetworkConstatnts.ResponseCode.success) {
                    cUtils.ShowToast(responseData.getMessage());
                }
                break;
            case AppConstants.ServiceModes.UPDATE_PROFILE_DETAIL:
                responseData = (DefaultResponse) model.body();
                if (responseData.getStatus() == NetworkConstatnts.ResponseCode.success) {
                    cUtils.ShowToast(responseData.getMessage());
                    UserResponseModel.ResultBean.UserBean userData = new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class);
                    userData.setFirstName(edTxtName.getText().toString());
                    userData.setLastName(edTxtLastName.getText().toString());
                    mPref.setUserData(new Gson().toJson(userData));
                }
                break;
            case AppConstants.ServiceModes.GET_PROFILE:
                UserResponseModel userResponse = (UserResponseModel) model.body();
                if (userResponse.getStatus() == NetworkConstatnts.ResponseCode.success) {
                    setUserData(userResponse);
                }
                break;
        }
    }

    @Override
    public void onLogout() {

    }

    @OnClick({R.id.back_toolbar, R.id.profile_image_one, R.id.profile_image_two, R.id.profile_image_three, R.id.save_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_toolbar:
                onBackPressed();
                break;
            case R.id.profile_image_one:
                whichImage = 1;
                selectImage();
                break;
            case R.id.profile_image_two:
                whichImage = 2;
                selectImage();
                break;
            case R.id.profile_image_three:
                whichImage = 3;
                selectImage();
                break;
            case R.id.save_btn:
                if (isvalidatedFields()) updateDetailxRequest();
                break;
        }
    }

    private void updateDetailxRequest() {
        String userId = String.valueOf(new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId());
        Call<DefaultResponse> call = apiInterface.updateProfileDetail(userId, edTxtName.getText().toString(),
                edTxtLastName.getText().toString(),
                edTxtName.getText().toString() + " " +
                        edTxtLastName.getText().toString(),
                edTxtAbout.getText().toString());
        call.enqueue(new RestProcess<DefaultResponse>(AppConstants.ServiceModes.UPDATE_PROFILE_DETAIL, this,
                EditProfileActivity.this, true));

    }

    private boolean isvalidatedFields() {
        if (CommonUtils.isEmpty(edTxtName)) {
            CommonUtils.getInstance(this).ShowToast(getString(R.string.error_toast_enter_first_name));
            return false;
        } else if (CommonUtils.isEmpty(edTxtLastName)) {
            CommonUtils.getInstance(this).ShowToast(getString(R.string.error_toast_enter_last_name));
            return false;
        } else if (CommonUtils.isEmpty(edTxtAbout)) {
            CommonUtils.getInstance(this).ShowToast(getString(R.string.error_toast_about));
            return false;
        } else {
            return true;
        }
    }

    private void selectImage() {
        if (Build.VERSION.SDK_INT < 23) {
            //Do not need to check the permission
            openPickAlert(1);
        } else {
            if (checkAndRequestPermissions()) {
                //If you have already permitted the permission
                openPickAlert(1);
            }
        }
    }

    private boolean checkAndRequestPermissions() {
        int camPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int readstoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writestoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (readstoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writestoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), AppConstants.RequestCode.PERMISSIONS_REQUEST_CAMERA);
            return false;
        }

        return true;
    }

    /**
     * MULTI IMAGE PICKER CALL
     *
     * @param i
     */
    private void openPickAlert(int i) {
        String[] name = {"Camera", "Gallery"};
        int[] icons = {R.drawable.ic_menu_camera, R.drawable.ic_menu_gallery};
        CommonUtils.getInstance(EditProfileActivity.this).openDialog(this, name, icons, this);

    }

    @Override
    public void onItemClick(View view, int position, int type, Object t) {
        switch (type) {
            case AppConstants.RequestCode.APAPTER_BOTTOM_DIALOG_CLICK:
                switch (position) {
                    case 0:
                        openCamera();
                        break;
                    case 1:
                        openGallerypicker();
                        break;
                }
                break;
        }
    }

    private void openGallerypicker() {
        // Intent to gallery
        Intent in = new Intent(Intent.ACTION_PICK);
        in.setType("image/*");
        startActivityForResult(in, AppConstants.RequestCode.GALLERY_REQUEST);// start
    }

    private void openCamera() {
        File f = null;
        //            f = storage.setUpPhotoFile();
        File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Rum-A");
        File wallpaperDirectory = direct;
        if (!direct.exists()) {
            wallpaperDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Rum-A");
            wallpaperDirectory.mkdirs();
        }
        String fileName = ImageUtility.getFileName();
        File file = new File(wallpaperDirectory, fileName + ImageUtility.JPEG_FILE_SUFFIX);
        if (file.exists()) {
            file.delete();
        }
        outputFile = file.getPath();
//      mCurrentCameraPhotoPath = f.getAbsolutePath();
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri photoURI = FileProvider.getUriForFile(EditProfileActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);

            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        } else
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        takePicture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(takePicture, AppConstants.RequestCode.REQUEST_TAKE_IMAGE);
    }

    private void cropRequest(String outputFile) {
        CropImage.activity(Uri.fromFile(new File(outputFile))).setAspectRatio(1,
                1).setFixAspectRatio(true).start(EditProfileActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.RequestCode.REQUEST_TAKE_IMAGE && resultCode == RESULT_OK) {
            Log.e("onActivityResult ", " REQUEST_TAKE_IMAGE " + outputFile);
            cropRequest(outputFile);
        } else if (requestCode == AppConstants.RequestCode.GALLERY_REQUEST && resultCode == RESULT_OK) {
            outputFile = ImageUtility.getString(getApplicationContext(), data.getData());
            Log.e("onActivityResult ", " REQUEST_TAKE_IMAGE " + outputFile);
            cropRequest(outputFile);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Log.e("onActivityResult ", " --Icon Image Replaced --> " + resultUri + "  " + resultUri.getPath());
                switch (whichImage) {
                    case 1:
                        profileImageOne.setImageURI(Uri.parse(resultUri.getPath().toString()));
                        updateImageRequest(resultUri.getPath(), "A");
                        break;
                    case 2:
                        profileImageTwo.setImageURI(Uri.parse(resultUri.getPath().toString()));
                        updateImageRequest(resultUri.getPath(), "B");
                        break;
                    case 3:
                        profileImageThree.setImageURI(Uri.parse(resultUri.getPath().toString()));
                        updateImageRequest(resultUri.getPath(), "C");
                        break;
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void updateImageRequest(String path, String iType) {
        Map<String, RequestBody> params = new HashMap<>();
        // Add Parameter
        params.put("UserID", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(getUserDeatil().getUserId())));
        params.put(NetworkConstatnts.Params.imaeType, RequestBody.create(MediaType.parse("text/plain"), iType));
        // Add Images

        File file = new File(path);
        MultipartBody.Part partData = MultipartBody.Part.
                createFormData(NetworkConstatnts.Params.image, file.getName(),
                        RequestBody.create(MediaType.parse("file"), file));

        Log.e("gerRequestData ", "   " + new Gson().toJson(params));
        postRequestData(params, partData);
    }

    private void postRequestData(Map<String, RequestBody> params, MultipartBody.Part imagePart) {
        Call<DefaultResponse> call = apiInterface.uploadProfileImage(params, imagePart);
        call.enqueue(new RestProcess<DefaultResponse>(AppConstants.ServiceModes.UPLOAD_PROFILE_IMAGE, this, EditProfileActivity.this,
                true));
    }
}
