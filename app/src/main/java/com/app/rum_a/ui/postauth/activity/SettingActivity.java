package com.app.rum_a.ui.postauth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.rum_a.R;
import com.app.rum_a.core.BaseActivity;
import com.app.rum_a.core.RumApplication;
import com.app.rum_a.di.DaggerValues;
import com.app.rum_a.model.modelutils.SeekingTypesModel;
import com.app.rum_a.model.resp.DefaultResponse;
import com.app.rum_a.model.resp.UserResponseModel;
import com.app.rum_a.model.resp.UserSettingsModel;
import com.app.rum_a.net.RestCallback;
import com.app.rum_a.net.RestProcess;
import com.app.rum_a.net.RestService;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.LogoutUtils;
import com.app.rum_a.utils.NetworkConstatnts;
import com.app.rum_a.utils.PreferenceManager;
import com.app.rum_a.utils.appinterface.CustomeDialogClickListner;
import com.app.rum_a.utils.views.RangeBar;
import com.app.rum_a.utils.views.RumCheckBox;
import com.app.rum_a.utils.views.RumEditText;
import com.app.rum_a.utils.views.RumTextView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by harish on 28/8/18.
 */

public class SettingActivity extends BaseActivity implements RestCallback {
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
    @BindView(R.id.toggleAccount)
    RumCheckBox toggleAccount;
    @BindView(R.id.togglePushNotifications)
    RumCheckBox togglePushNotifications;
    @BindView(R.id.relFeedback)
    RelativeLayout relFeedback;
    @BindView(R.id.txtDeleteAccount)
    RumTextView txtDeleteAccount;
    @BindView(R.id.search_address)
    RumEditText searchAddress;
    @BindView(R.id.distanceTypeMiles)
    RumCheckBox distanceTypeMiles;
    @BindView(R.id.distanceTypeKm)
    RumCheckBox distanceTypeKm;
    @BindView(R.id.seekBarDistanceRangedistanceRange)
    SeekBar seekBarDistanceRangedistanceRange;
    @BindView(R.id.price_range_seekbar)
    RangeBar priceRangeSeekbar;
    @BindView(R.id.seekingType_house)
    RumTextView seekingTypeHouse;
    @BindView(R.id.seekingType_appartment)
    RumTextView seekingTypeAppartment;
    @BindView(R.id.seekingType_commercial_space)
    RumTextView seekingTypeCommercialSpace;
    @BindView(R.id.seekingType_land)
    RumTextView seekingTypeLand;
    @BindView(R.id.mini_price_range)
    RumTextView miniPriceRange;
    @BindView(R.id.max_price_range)
    RumTextView maxiPriceRange;
    @BindView(R.id.txtProgressDistance)
    RumTextView txtProgressDistance;
    @BindView(R.id.lookingType_rent)
    RumTextView lookingTypeRent;
    @BindView(R.id.lookingType_buy)
    RumTextView lookingTypeBuy;
    @BindView(R.id.lookingType_lease)
    RumTextView lookingTypeLease;
    @BindView(R.id.lookingType_sell)
    RumTextView lookingTypeSell;
    @BindView(R.id.currencySpinner)
    Spinner currencySpinner;
    private String locationLatitude = "0.0", locationLongitude = "0.0", locationAddress = "Not Found",
            selectedLocation = "", distanceType = "Miles", maxLocationRange = "1000", minPriceRange = "100000", maxPriceRange = "100000000";
    boolean privateAccount, pushNotification;
    int seekingType = 0, lookingType = 0;
    ArrayList<SeekingTypesModel> seekingTypesList = new ArrayList<>();

    String[] currencyTypes = new String[]{"USD", "AUD", "SGD", "Euro", "GBP", "IDR", "CNY", "JPY", "RUB", "INR", "KRW"};
    private ArrayAdapter currencyAdapter;
    String currencyType = "USD";
    String currencySymbol = cUtils.getCurrencySymbol(currencyType);

    @Override

    public int getLayoutId() {
        return R.layout.setting_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RumApplication) getApplication()).getMyComponent().inject(this);
        txtTitleToolbar.setText(R.string.action_settings);
        seekingTypesList.addAll(getSeekingTypes());
        getUserSettings();
        setupSeekBars();
        spinnerSetup();
        setToogleBtns();
    }

    private void setToogleBtns() {
        toggleAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privateAccount = toggleAccount.isChecked();
            }
        });
        togglePushNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushNotification = togglePushNotifications.isChecked();
            }
        });
    }

    private void spinnerSetup() {
        currencyAdapter = new ArrayAdapter<String>(this, R.layout.custom_view_spinner, currencyTypes);
        currencySpinner.setAdapter(currencyAdapter);
        currencySpinner.setSelection(1);
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(" onItemSelected ", " currency " + currencyTypes[position]);
                currencyType = currencyTypes[position];
                currencySymbol = cUtils.getCurrencySymbol(currencyType);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupSeekBars() {
        priceRangeSeekbar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {
                minPriceRange = "" + (leftThumbIndex * 100000);
                maxPriceRange = "" + (rightThumbIndex * 1000000);
                miniPriceRange.setText(currencySymbol + minPriceRange);
                maxiPriceRange.setText(currencySymbol + maxPriceRange);
                Log.e(" onIndexChangeListener ", "  " + minPriceRange + "  " + maxPriceRange);
            }
        });
        seekBarDistanceRangedistanceRange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxLocationRange = "" + progress;
                Log.e(" onProgressChanged ", "  " + maxLocationRange);
                if (distanceTypeMiles.isChecked())
                    txtProgressDistance.setText(String.valueOf(progress) + " " + getResources().getString(R.string.str_miles));
                else
                    txtProgressDistance.setText(String.valueOf(progress) + " " + getResources().getString(R.string.str_km));
                addRule();
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void addRule() {
        txtProgressDistance.setX(seekBarDistanceRangedistanceRange.getThumb().getBounds().right);
    }

    private void getUserSettings() {
        String userId = String.valueOf(new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId());
        Call<UserSettingsModel> call = apiInterface.getUserSettings(userId);
        call.enqueue(new RestProcess<UserSettingsModel>(AppConstants.ServiceModes.GET_SETTINGS,
                this, SettingActivity.this, true));

    }

    private void updateUserSettings() {

        String userId = String.valueOf(new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId());
        Call<UserSettingsModel> call = apiInterface.updateUserSettings(userId,
                locationAddress,
                locationLatitude,
                locationLongitude,
                distanceType,
                maxLocationRange,
                minPriceRange,
                maxPriceRange,
                seekingType, lookingType, currencyType,
                privateAccount,
                pushNotification
        );
        call.enqueue(new RestProcess<UserSettingsModel>(AppConstants.ServiceModes.UPDATE_SETTINGS, this, SettingActivity.this, true));

    }

    private ArrayList<SeekingTypesModel> getSeekingTypes() {
        ArrayList<SeekingTypesModel> arraylist = new ArrayList<>();
        SeekingTypesModel seekingType = new SeekingTypesModel();
        seekingType.setSelected(false);
        seekingType.setSeekingId(AppConstants.Seekingtype.House);
        seekingType.setSeekingName("Home");
        arraylist.add(seekingType);
        seekingType = new SeekingTypesModel();
        seekingType.setSelected(false);
        seekingType.setSeekingId(AppConstants.Seekingtype.Apartment);
        seekingType.setSeekingName("Apartment");
        arraylist.add(seekingType);
        seekingType = new SeekingTypesModel();
        seekingType.setSelected(false);
        seekingType.setSeekingId(AppConstants.Seekingtype.Commercial_Space);
        seekingType.setSeekingName("Commercial_Space");
        arraylist.add(seekingType);
        seekingType = new SeekingTypesModel();
        seekingType.setSelected(false);
        seekingType.setSeekingId(AppConstants.Seekingtype.Land);
        seekingType.setSeekingName("Land");
        arraylist.add(seekingType);
        return arraylist;
    }


    @Override
    public void onFailure(Call call, Throwable t, int serviceMode) {

    }

    @Override
    public void onSuccess(Call call, Response model, int serviceMode) {
        switch (serviceMode) {
            case AppConstants.ServiceModes.GET_SETTINGS:
                UserSettingsModel responseData = (UserSettingsModel) model.body();
                if (responseData.getStatus() == NetworkConstatnts.ResponseCode.success) {
                    setSettingValues(responseData);
                    try {
                        mPref.setUserSettings(new Gson().toJson(responseData.getResult()));
                        Log.e(" onSuccess ", " " + mPref.getUserSettings());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case AppConstants.ServiceModes.UPDATE_SETTINGS:
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                break;
            case AppConstants.ServiceModes.DELETE_ACCOUNT:
                DefaultResponse dResponse = (DefaultResponse) model.body();
                if (dResponse.getStatus() == NetworkConstatnts.ResponseCode.success)
                    LogoutUtils.logOutApp(SettingActivity.this);
                else cUtils.ShowToast(dResponse.getMessage());
                break;
        }

    }

    @Nullable
    private void setSettingValues(UserSettingsModel responseData) {
        try {
            searchAddress.setText(responseData.getResult().getAddress());
            if (responseData.getResult().getPrivateAccount()) {
                toggleAccount.setChecked(true);
                privateAccount = true;
            } else {
                toggleAccount.setChecked(false);
                privateAccount = false;
            }
            if (responseData.getResult().getPushNotification()) {
                togglePushNotifications.setChecked(true);
                pushNotification = true;
            } else {
                togglePushNotifications.setChecked(false);
                pushNotification = false;
            }
            try {
                if (responseData.getResult().getDistanceType().equals("Miles")) {
                    distanceTypeMiles.setChecked(true);
                    distanceTypeKm.setChecked(false);
                    distanceType = "Miles";
                } else {
                    distanceTypeMiles.setChecked(false);
                    distanceTypeKm.setChecked(true);
                    distanceType = "Km";
                }
            } catch (Exception e) {

            }

            setSeekingTypes(responseData.getResult().getSeekingType());
            setLookingTypes(responseData.getResult().getLookingType());
            locationLatitude = responseData.getResult().getLatitude();
            locationLongitude = responseData.getResult().getLongitude();
            locationAddress = responseData.getResult().getAddress();
            try {

                for (int i = 0; i < currencyTypes.length; i++) {
                    if (currencyTypes[i].equals(responseData.getResult().getCurrency())) {
                        currencySpinner.setSelection(i);
                        currencySymbol = cUtils.getCurrencySymbol(responseData.getResult().getCurrency());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                seekBarDistanceRangedistanceRange.setProgress(responseData.getResult().getMaxLocation());
                priceRangeSeekbar.setThumbIndices((responseData.getResult().getStartRangePrice() / 1000000),
                        (responseData.getResult().getEndRangePrice() / 1000000));
                maxiPriceRange.setText(currencySymbol + " " + (responseData.getResult().getEndRangePrice()));
                miniPriceRange.setText(currencySymbol + " " + (responseData.getResult().getStartRangePrice()));
                if (responseData.getResult().getStartRangePrice() == 0) {
                    minPriceRange = "100000";
                    maxPriceRange = "10000000";
                }
            } catch (Exception e) {
                e.printStackTrace();
                minPriceRange = "100000";
                maxPriceRange = "10000000";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSeekingTypes(int seekingType) {
        switch (seekingType) {
            case AppConstants.Seekingtype.House:
                seekingTypeHouse.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check, 0);
                seekingTypeAppartment.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                seekingTypeCommercialSpace.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                seekingTypeLand.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case AppConstants.Seekingtype.Apartment:
                seekingTypeHouse.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                seekingTypeAppartment.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check, 0);
                seekingTypeCommercialSpace.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                seekingTypeLand.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case AppConstants.Seekingtype.Commercial_Space:
                seekingTypeHouse.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                seekingTypeAppartment.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                seekingTypeCommercialSpace.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check, 0);
                seekingTypeLand.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case AppConstants.Seekingtype.Land:
                seekingTypeHouse.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                seekingTypeAppartment.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                seekingTypeCommercialSpace.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                seekingTypeLand.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check, 0);
                break;
        }
    }

    private void setLookingTypes(int lookingTypes) {
        switch (lookingTypes) {
            case AppConstants.LookingTypes.Rent:
                lookingTypeRent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check, 0);
                lookingTypeLease.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                lookingTypeBuy.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                lookingTypeSell.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case AppConstants.LookingTypes.Sell:
                lookingTypeRent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                lookingTypeSell.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check, 0);
                lookingTypeLease.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                lookingTypeBuy.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case AppConstants.LookingTypes.Lease:
                lookingTypeRent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                lookingTypeSell.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                lookingTypeLease.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check, 0);
                lookingTypeBuy.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case AppConstants.LookingTypes.Buy:
                lookingTypeRent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                lookingTypeSell.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                lookingTypeLease.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                lookingTypeBuy.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check, 0);
                break;
        }
    }

    @Override
    public void onLogout() {

    }

    @OnClick({R.id.back_toolbar, R.id.search_address, R.id.txtDeleteAccount, R.id.distanceTypeMiles, R.id.distanceTypeKm,
            R.id.seekingType_house,
            R.id.seekingType_appartment,
            R.id.seekingType_commercial_space,
            R.id.seekingType_land,

            R.id.lookingType_buy,
            R.id.lookingType_lease,
            R.id.lookingType_rent,
            R.id.lookingType_sell, R.id.relFeedback})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.distanceTypeMiles:
                distanceTypeKm.setChecked(false);
                distanceTypeMiles.setChecked(true);
                distanceType = "Miles";
                break;
            case R.id.distanceTypeKm:
                distanceTypeKm.setChecked(true);
                distanceTypeMiles.setChecked(false);
                distanceType = "Km";
                break;
            case R.id.seekingType_house:
                seekingType = AppConstants.Seekingtype.House;
                setSeekingTypes(seekingType);
//                updateView(AppConstants.Seekingtype.House, (TextView) view);
                break;
            case R.id.seekingType_appartment:
                seekingType = AppConstants.Seekingtype.Apartment;
                setSeekingTypes(seekingType);
//                updateView(AppConstants.Seekingtype.Apartment, (TextView) view);
                break;
            case R.id.seekingType_commercial_space:
                seekingType = AppConstants.Seekingtype.Commercial_Space;
                setSeekingTypes(seekingType);
//                updateView(AppConstants.Seekingtype.Commercial_Space, (TextView) view);
                break;
            case R.id.seekingType_land:
                seekingType = AppConstants.Seekingtype.Land;
                setSeekingTypes(seekingType);
//                updateView(AppConstants.Seekingtype.Land, (TextView) view);
                break;
            case R.id.lookingType_buy:
                lookingType = AppConstants.LookingTypes.Buy;
                setLookingTypes(lookingType);
//                updateView(AppConstants.Seekingtype.House, (TextView) view);
                break;
            case R.id.lookingType_lease:
                lookingType = AppConstants.LookingTypes.Lease;
                setLookingTypes(lookingType);
//                updateView(AppConstants.Seekingtype.Apartment, (TextView) view);
                break;
            case R.id.lookingType_rent:
                lookingType = AppConstants.LookingTypes.Rent;
                setLookingTypes(lookingType);
//                updateView(AppConstants.Seekingtype.Commercial_Space, (TextView) view);
                break;
            case R.id.lookingType_sell:
                lookingType = AppConstants.LookingTypes.Sell;
                setLookingTypes(lookingType);
//                updateView(AppConstants.Seekingtype.Land, (TextView) view);
                break;
            case R.id.back_toolbar:
                updateUserSettings();
                onBackPressed();
                break;
            case R.id.txtDeleteAccount:
                cUtils.DialogSingleButton(SettingActivity.this, getString(R.string.str_delete_account), getString(R.string.str_are_you_sure), new CustomeDialogClickListner() {
                    @Override
                    public void onOkClick() {
                        deleteAccount();
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });

                break;
            case R.id.search_address:
                selectLocation();
                break;
            case R.id.relFeedback:
                activitySwitcher(SettingActivity.this, FeedbackActivity.class, null);
                break;
        }
    }

    private void deleteAccount() {
        String userId = String.valueOf(new Gson().fromJson(mPref.getUserData(), UserResponseModel.ResultBean.UserBean.class).getUserId());
        Call<DefaultResponse> call = apiInterface.deleteAccount(userId);
        call.enqueue(new RestProcess<DefaultResponse>(AppConstants.ServiceModes.DELETE_ACCOUNT, this, SettingActivity.this, true));

    }

    @Override
    public void onBackPressed() {
        updateUserSettings();

    }

    private void updateView(int typeId, TextView view) {
        for (int i = 0; i < seekingTypesList.size(); i++) {
            if (seekingTypesList.get(i).getSeekingId() == typeId) {
                if (seekingTypesList.get(i).isSelected()) {
                    seekingTypesList.get(i).setSelected(false);
                    view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    seekingTypesList.get(i).setSelected(true);
                    view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check, 0);
                }

            }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.RequestCode.PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(data, this);
            Log.e("onActivityResult ", new Gson().toJson(place));
            try {
                locationLatitude = String.valueOf(place.getLatLng().latitude);
                locationLongitude = String.valueOf(place.getLatLng().longitude);
                locationAddress = place.getAddress().toString();
                searchAddress.setText(locationAddress);
                selectedLocation = place.getName().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
