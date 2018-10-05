package com.app.rum_a.ui.postauth.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.app.rum_a.BuildConfig;
import com.app.rum_a.R;
import com.app.rum_a.core.BaseActivity;
import com.app.rum_a.core.RumApplication;
import com.app.rum_a.di.DaggerValues;
import com.app.rum_a.model.modelutils.ImagePickerModel;
import com.app.rum_a.model.resp.AddPropertyResponseModel;
import com.app.rum_a.model.resp.PropertyListResponseModel;
import com.app.rum_a.net.RestCallback;
import com.app.rum_a.net.RestProcess;
import com.app.rum_a.net.RestService;
import com.app.rum_a.ui.postauth.adapter.HorizontalAdapter;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.ImageUtility;
import com.app.rum_a.utils.NetworkConstatnts;
import com.app.rum_a.utils.PreferenceManager;
import com.app.rum_a.utils.appinterface.OnBottomDialogItemListener;
import com.app.rum_a.utils.appinterface.OnItemClickListener;
import com.app.rum_a.utils.views.RumCheckBox;
import com.app.rum_a.utils.views.RumTextView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by harish on 30/8/18.
 */

public class AddPropertyActivity extends BaseActivity implements OnBottomDialogItemListener, RestCallback {
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
    @BindView(R.id.layout_single_image_btn)
    LinearLayout layoutSingleImageBtn;
    @BindView(R.id.edTxt_name)
    EditText edTxtName;
    @BindView(R.id.edTxt_location)
    EditText edTxtLocation;
    @BindView(R.id.edTxt_description)
    EditText edTxtDescription;
    @BindView(R.id.txt_btn_submit)
    RumTextView txtBtnSubmit;
    @BindView(R.id.recyclar_View_lst)
    RecyclerView recyclarViewLst;
    @BindView(R.id.layout_multi_image_btn)
    LinearLayout layoutMultiImageBtn;
    @BindView(R.id.selling_type_sell)
    RumCheckBox sellingTypeSell;
    @BindView(R.id.selling_type_lease)
    RumCheckBox sellingTypeLease;
    @BindView(R.id.selling_type_rent)
    RumCheckBox sellingTypeRent;
    @BindView(R.id.layout_selling_type)
    LinearLayout layoutSellingType;
    @BindView(R.id.property_type_house)
    RumCheckBox propertyTypeHouse;
    @BindView(R.id.property_type_apartment)
    RumCheckBox propertyTypeApartment;
    @BindView(R.id.property_type_commercial)
    RumCheckBox propertyTypeCommercial;
    @BindView(R.id.property_type_land)
    RumCheckBox propertyTypeLand;
    @BindView(R.id.layout_property_type)
    LinearLayout layoutPropertyType;
    @BindView(R.id.edTxt_price)
    EditText edTxtPrice;
    @BindView(R.id.currencySpinner)
    Spinner currencySpinner;
    @BindView(R.id.selling_type_rent_new)
    RumCheckBox sellingTypeRentNew;
    @BindView(R.id.selling_type_buy_new)
    RumCheckBox sellingTypeBuyNew;
    @BindView(R.id.layout_type_for_buyer)
    LinearLayout layoutTypeForBuyer;
    private int sellingType = AppConstants.LookingTypes.Sell;
    private int propertyType = AppConstants.Seekingtype.House;
    private String outputFile;
    private LinearLayoutManager layoutManager;
    ArrayList<ImagePickerModel> imageList;
    private HorizontalAdapter adapter;
    private String propertyAddress, propertyLocation, propertyLatitude, propertyLongitude;
    String[] currencyTypes = new String[]{"USD", "AUD", "SGD", "Euro", "GBP", "IDR", "CNY", "JPY", "RUB", "INR", "KRW"};
    private ArrayAdapter currencyAdapter;
    String currencyType = "USD";
    private int sellingTypeNew;
    private boolean isEdit;
    private PropertyListResponseModel.ResultBean propertDetail;

    @Override
    public int getLayoutId() {
        return R.layout.add_property_screen;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RumApplication) getApplication()).getMyComponent().inject(this);
        txtTitleToolbar.setText(R.string.str_add_property);
        initRecyclar();
        imageList = new ArrayList<>();
        spinnerSetup();
        getIntentData(getIntent());
    }

    private void getIntentData(Intent intent) {
        isEdit = intent.getBooleanExtra(AppConstants.ParmsType.IS_EDIT, false);
        if (isEdit) {
            Serializable propertyDetail = intent.getSerializableExtra(AppConstants.ParmsType.PROPERTY_DETAIL);
            if (propertyDetail != null) {
                propertDetail = (PropertyListResponseModel.ResultBean) propertyDetail;
                setdetailedData(propertDetail);
            }
        }
    }

    private void setdetailedData(final PropertyListResponseModel.ResultBean propertDetail) {
        edTxtName.setText(propertDetail.getName());
        edTxtDescription.setText(propertDetail.getDescription());
        edTxtPrice.setText("" + propertDetail.getPrice());
        edTxtLocation.setText(propertDetail.getAddress());
        propertyAddress = propertDetail.getAddress();
        propertyLongitude = propertDetail.getLongitude();
        propertyLatitude = propertDetail.getLatitude();
        propertyLocation = propertDetail.getLocation();
        for (int i = 0; i < currencyTypes.length; i++) {
            if (currencyTypes[i] == propertDetail.getCurrency()) {
                currencySpinner.setSelection(i);
            }
        }
        Log.e(" setdetailedData ", " " + cUtils.getSeekingType(propertDetail.getSellingType())
                + " " + propertDetail.getForRentOrBuy()
                + " " + cUtils.getlookingType(propertDetail.getPropertyType()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setSeekingType(propertDetail.getSellingType());
                setLookingType(propertDetail.getPropertyType());
                setLandFor(propertDetail.getForRentOrBuy());
            }
        }, AppConstants.TimeLimits.SMALL);
    }

    private void setLookingType(int propertyType) {
        if (propertyType == AppConstants.LookingTypes.Lease)
            sellingTypeLease.performClick();
        else if (propertyType == AppConstants.LookingTypes.Sell)
            sellingTypeSell.performClick();
    }

    private void setSeekingType(int sellingType) {

        if (sellingType == AppConstants.Seekingtype.Apartment) {
            propertyTypeApartment.performClick();
        } else if (sellingType == AppConstants.Seekingtype.Commercial_Space) {
            propertyTypeCommercial.performClick();
        } else if (sellingType == AppConstants.Seekingtype.Land) {
            propertyTypeLand.performClick();
        } else if (sellingType == AppConstants.Seekingtype.House) {
            propertyTypeHouse.performClick();
        }

    }

    private void spinnerSetup() {
        currencyAdapter = new ArrayAdapter<String>(this, R.layout.custom_view_spinner, currencyTypes);
        currencySpinner.setAdapter(currencyAdapter);
        currencySpinner.setSelection(1);
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(" onItemSelected ", " looking " + currencyTypes[position]);
                currencyType = currencyTypes[position];
               /* switch (position) {
                    case 0:
                        currencyType = AppConstants.LookingTypes.Rent;
                        break;
                    case 1:
                        currencyType = AppConstants.LookingTypes.Buy;
                        break;
                    case 2:
                        currencyType = AppConstants.LookingTypes.Lease;
                        break;
                    case 3:
                        currencyType = AppConstants.LookingTypes.Sell;
                        break;


                }*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRecyclar() {
        layoutManager = new LinearLayoutManager(AddPropertyActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclarViewLst.setLayoutManager(layoutManager);
        adapter = new HorizontalAdapter(this, new ArrayList<ImagePickerModel>(), new OnItemClickListener<ImagePickerModel>() {
            @Override
            public void onItemClick(View view, int position, int type, Object t) {
                adapter.removeItem(position);
                adapter.notifyDataSetChanged();
            }
        });
        recyclarViewLst.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        if (isEdit) {
            setResult(RESULT_OK);
            finish();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        } else
            super.onBackPressed();
    }

    @OnClick({R.id.back_toolbar, R.id.txt_btn_submit, R.id.layout_single_image_btn, R.id.add_multi_image_btn, R.id.edTxt_location,
            R.id.selling_type_lease,
            R.id.selling_type_rent,
            R.id.selling_type_sell,
            R.id.property_type_apartment,
            R.id.property_type_commercial,
            R.id.property_type_land,
            R.id.property_type_house, R.id.selling_type_buy_new, R.id.selling_type_rent_new})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_toolbar:
                onBackPressed();
                break;
            case R.id.txt_btn_submit:
                if (validateFields()) gerRequestData();
                break;
            case R.id.edTxt_location:
                selectLocation();
                break;
            case R.id.add_multi_image_btn:
            case R.id.layout_single_image_btn:
                if (Build.VERSION.SDK_INT < 23) {
                    //Do not need to check the permission
                    openPickAlert(1);
                } else {
                    if (checkAndRequestPermissions()) {
                        //If you have already permitted the permission
                        openPickAlert(1);
                    }
                }
                break;
            case R.id.selling_type_lease:
                sellingTypeSelection(view.getId());
                break;
            case R.id.selling_type_rent:
                sellingTypeSelection(view.getId());
                break;
            case R.id.selling_type_sell:
                sellingTypeSelection(view.getId());
                break;
            case R.id.property_type_apartment:
                propertyTypeSelection(view.getId());
                break;
            case R.id.property_type_commercial:
                propertyTypeSelection(view.getId());
                break;
            case R.id.property_type_house:
                propertyTypeSelection(view.getId());
                break;
            case R.id.property_type_land:
                propertyTypeSelection(view.getId());
                break;
            case R.id.selling_type_buy_new:
                propertyTypeSelectionNew(view.getId());
                break;
            case R.id.selling_type_rent_new:
                propertyTypeSelectionNew(view.getId());
                break;
        }
    }

    private void propertyTypeSelectionNew(int id) {
        switch (id) {
            case R.id.selling_type_buy_new:
                sellingTypeNew = 1;//rent{ Rent = 0,Buy = 1 }
                setLandFor(sellingTypeNew);
                break;
            case R.id.selling_type_rent_new:
                sellingTypeNew = 0;//rent{ Rent = 0,Buy = 1 }
                setLandFor(sellingTypeNew);
                break;
        }
    }

    private void setLandFor(int sellingTpeNew) {
        switch (sellingTypeNew) {
            case 0:
                sellingTypeNew = sellingTpeNew;
                sellingTypeBuyNew.setChecked(false);
                sellingTypeRentNew.setChecked(true);
                break;
            case 1:
                sellingTypeNew = sellingTpeNew;
                sellingTypeBuyNew.setChecked(true);
                sellingTypeRentNew.setChecked(false);
                break;
        }
    }

    private void gerRequestData() {
        List<MultipartBody.Part> list = new ArrayList<MultipartBody.Part>(5);
        Map<String, RequestBody> params = new HashMap<>();
        // Add Parameter
        params.put(NetworkConstatnts.Params.userId, RequestBody.create(MediaType.parse("text/plain"), String.valueOf(getUserDeatil().getUserId())));
        params.put(NetworkConstatnts.Params.name, RequestBody.create(MediaType.parse("text/plain"), edTxtName.getText().toString()));
        params.put(NetworkConstatnts.Params.address, RequestBody.create(MediaType.parse("text/plain"), propertyAddress));
        params.put(NetworkConstatnts.Params.latitude, RequestBody.create(MediaType.parse("text/plain"), propertyLatitude));
        params.put(NetworkConstatnts.Params.longitude, RequestBody.create(MediaType.parse("text/plain"), propertyLongitude));
        params.put(NetworkConstatnts.Params.location, RequestBody.create(MediaType.parse("text/plain"), propertyLocation));
        params.put(NetworkConstatnts.Params.propertyType, RequestBody.create(MediaType.parse("text/plain"), String.valueOf(propertyType)));
        params.put(NetworkConstatnts.Params.sellingType, RequestBody.create(MediaType.parse("text/plain"), String.valueOf(sellingType)));
        params.put(NetworkConstatnts.Params.description, RequestBody.create(MediaType.parse("text/plain"), edTxtDescription.getText().toString()));
        params.put(NetworkConstatnts.Params.price, RequestBody.create(MediaType.parse("text/plain"), edTxtPrice.getText().toString()));
        params.put(NetworkConstatnts.Params.currency, RequestBody.create(MediaType.parse("text/plain"), currencyType));
        params.put(NetworkConstatnts.Params.sellingTypeNew, RequestBody.create(MediaType.parse("text/plain"), String.valueOf(sellingTypeNew)));
        // Add Images
        if (adapter.getImageList().size() > 0) {
            for (int i = 0; i < adapter.getImageList().size(); i++) {
                File file = new File(adapter.getImageList().get(i).getImagePath());
                list.add(MultipartBody.Part.
                        createFormData(NetworkConstatnts.Params.images, file.getName(),
                                RequestBody.create(MediaType.parse("file"), file)));
            }
        }
        Log.e("gerRequestData ", "   " + new Gson().toJson(params));
        if (!isEdit)
            postRequestData(params, list);
        else {
            params.put(NetworkConstatnts.Params.propertyID, RequestBody.create(MediaType.parse("text/plain"), String.valueOf(propertDetail.getPropertyID())));
            updateProperty(params, list);
        }
    }

    private void updateProperty(Map<String, RequestBody> params, List<MultipartBody.Part> list) {
        Call<AddPropertyResponseModel> call = apiInterface.updateProperty(params, list);
        call.enqueue(new RestProcess<AddPropertyResponseModel>(AppConstants.ServiceModes.UPLOAD_PROPERTY, this,
                AddPropertyActivity.this, true));
    }

    private void postRequestData(Map<String, RequestBody> params, List<MultipartBody.Part> list) {
        Call<AddPropertyResponseModel> call = apiInterface.uploadProperty(params, list);
        call.enqueue(new RestProcess<AddPropertyResponseModel>(AppConstants.ServiceModes.UPLOAD_PROPERTY, this,
                AddPropertyActivity.this, true));
    }

    private boolean validateFields() {
        Log.e("validateFields ", "   " + adapter.getItemCount());
        if (CommonUtils.isEmpty(edTxtName)) {
            return false;
        } else if (CommonUtils.isEmpty(edTxtLocation)) {
            CommonUtils.getInstance(this).ShowToast(getString(R.string.error_location));
            return false;
        } else if (CommonUtils.isEmpty(edTxtPrice)) {
            CommonUtils.getInstance(this).ShowToast(getString(R.string.error_price));
            return false;
        } else if (CommonUtils.isEmpty(edTxtDescription)) {
            CommonUtils.getInstance(this).ShowToast(getString(R.string.error_desc));
            return false;
        } else {
            return true;
        }

    }

    private void selectLocation() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), AppConstants.RequestCode.PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void propertyTypeSelection(int id) {
        switch (id) {
            case R.id.property_type_apartment:
                propertyTypeApartment.setChecked(true);
                propertyTypeCommercial.setChecked(false);
                propertyTypeHouse.setChecked(false);
                propertyTypeLand.setChecked(false);
                propertyType = AppConstants.Seekingtype.Apartment;
                break;
            case R.id.property_type_commercial:
                propertyTypeApartment.setChecked(false);
                propertyTypeCommercial.setChecked(true);
                propertyTypeHouse.setChecked(false);
                propertyTypeLand.setChecked(false);
                propertyType = AppConstants.Seekingtype.Commercial_Space;
                break;
            case R.id.property_type_house:
                propertyTypeApartment.setChecked(false);
                propertyTypeCommercial.setChecked(false);
                propertyTypeHouse.setChecked(true);
                propertyTypeLand.setChecked(false);
                propertyType = AppConstants.Seekingtype.House;
                break;
            case R.id.property_type_land:
                propertyTypeApartment.setChecked(false);
                propertyTypeCommercial.setChecked(false);
                propertyTypeHouse.setChecked(false);
                propertyTypeLand.setChecked(true);
                propertyType = AppConstants.Seekingtype.Land;
                break;
        }
    }

    private void sellingTypeSelection(int id) {
        switch (id) {
            case R.id.selling_type_lease:
                sellingTypeLease.setChecked(true);
                sellingTypeRent.setChecked(false);
                sellingTypeSell.setChecked(false);
                sellingType = AppConstants.LookingTypes.Lease;
                break;
            case R.id.selling_type_rent:
                sellingTypeLease.setChecked(false);
                sellingTypeRent.setChecked(true);
                sellingTypeSell.setChecked(false);
                sellingType = AppConstants.LookingTypes.Rent;
                break;
            case R.id.selling_type_sell:
                sellingTypeLease.setChecked(false);
                sellingTypeRent.setChecked(false);
                sellingTypeSell.setChecked(true);
                sellingType = AppConstants.LookingTypes.Sell;
                break;
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
        CommonUtils.getInstance(AddPropertyActivity.this).openDialog(this, name, icons, this);

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
            Uri photoURI = FileProvider.getUriForFile(AddPropertyActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);

            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        } else
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        takePicture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(takePicture, AppConstants.RequestCode.REQUEST_TAKE_IMAGE);
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
                ImagePickerModel pickedImage = new ImagePickerModel();
                pickedImage.setImageCroped(true);
                pickedImage.setImagePath(resultUri.getPath());
                ArrayList<ImagePickerModel> imageList = new ArrayList<>();
                imageList.add(pickedImage);
                adapter.update(imageList);
                notifiScreenView();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if (requestCode == AppConstants.RequestCode.PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(data, this);
            Log.e("onActivityResult ", new Gson().toJson(place));
            try {
                propertyLatitude = String.valueOf(place.getLatLng().latitude);
                propertyLongitude = String.valueOf(place.getLatLng().longitude);
                propertyAddress = place.getAddress().toString();
                edTxtLocation.setText(propertyAddress);
                propertyLocation = place.getName().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void notifiScreenView() {
        if (adapter.getItemCount() > 0) {
            layoutSingleImageBtn.setVisibility(View.GONE);
            layoutMultiImageBtn.setVisibility(View.VISIBLE);
        } else {
            layoutSingleImageBtn.setVisibility(View.VISIBLE);
            layoutMultiImageBtn.setVisibility(View.GONE);
        }
    }

    private void cropRequest(String outputFile) {
        CropImage.activity(Uri.fromFile(new File(outputFile))).setAspectRatio(1,
                1).setFixAspectRatio(true).start(AddPropertyActivity.this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case AppConstants.RequestCode.PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openPickAlert(1);
                    //Permission Granted Successfully. Write working code here.
                } else {
                    //You did not accept the request can not use the functionality.
                    CommonUtils.getInstance(this).ShowToast(getString(R.string.permission_denied));
                }
                break;
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, int serviceMode) {

    }

    @Override
    public void onSuccess(Call call, Response model, int serviceMode) {
        switch (serviceMode) {
            case AppConstants.ServiceModes.UPLOAD_PROPERTY:
                AddPropertyResponseModel propModel = (AddPropertyResponseModel) model.body();
                if (propModel.getStatus() == NetworkConstatnts.ResponseCode.success) {
                    cUtils.ShowToast(propModel.getMessage());
                    resetFields();
                } else cUtils.ShowToast(propModel.getMessage());
                break;
        }
    }

    private void resetFields() {
        edTxtLocation.setText("");
        edTxtDescription.setText("");
        edTxtName.setText("");
        edTxtPrice.setText("");
        propertyTypeHouse.performClick();
        sellingTypeSell.performClick();
        layoutMultiImageBtn.setVisibility(View.GONE);
        layoutSingleImageBtn.setVisibility(View.VISIBLE);
        adapter.removeItems();
    }

    @Override
    public void onLogout() {

    }
}
