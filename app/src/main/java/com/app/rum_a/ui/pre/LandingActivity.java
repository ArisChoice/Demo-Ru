package com.app.rum_a.ui.pre;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.rum_a.DashActivity;
import com.app.rum_a.R;
import com.app.rum_a.core.BaseActivity;
import com.app.rum_a.net.firebase.MyFirebaseInstanceIDService;
import com.app.rum_a.utils.AppConstants;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by harish on 24/8/18.
 */

public class LandingActivity extends BaseActivity {
    @BindView(R.id.logo_land)
    ImageView logoLand;
    @BindView(R.id.txt_btn_next)
    TextView txtBtnNext;
    @BindView(R.id.layout_option_holder)
    LinearLayout layoutOptionHolder;
    @BindView(R.id.txt_btn_login)
    TextView txtBtnLogin;
    @BindView(R.id.spinner_looking)
    Spinner spinnerLooking;
    @BindView(R.id.spinner_Seeking)
    Spinner spinnerSeeking;
    String[] lookingTypes = new String[]{"Rent", "Buy", "Lease", "Sell"};
    String[] seekingTypes = new String[]{"House", "Apartment", "Commercial Space", "Land"};
    private ArrayAdapter<String> lookingAdapter;
    private ArrayAdapter<String> seekingAdapter;
    private int lookinType;
    private int seekingType;

    @Override
    public int getLayoutId() {
        return R.layout.landing_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spinnerSetup();
        // GET GOOGLE  TOKEN FOR PUSH NOTIFICATION....................
        new StartRegisterationService().execute();
    }

    private void spinnerSetup() {
        lookingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lookingTypes);
        spinnerLooking.setAdapter(lookingAdapter);
        spinnerLooking.setSelection(1);
        spinnerLooking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(" onItemSelected ", " looking " + lookingTypes[position]);
                switch (position) {
                    case 0:
                        lookinType = AppConstants.LookingTypes.Rent;
                        break;
                    case 1:
                        lookinType = AppConstants.LookingTypes.Buy;
                        break;
                    case 2:
                        lookinType = AppConstants.LookingTypes.Lease;
                        break;
                    case 3:
                        lookinType = AppConstants.LookingTypes.Sell;
                        break;


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        seekingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, seekingTypes);
        spinnerSeeking.setAdapter(seekingAdapter);
        spinnerSeeking.setSelection(1);
        spinnerSeeking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(" onItemSelected ", " seeking " + seekingTypes[position]);
                switch (position) {
                    case 0:
                        seekingType = AppConstants.Seekingtype.House;
                        break;
                    case 1:
                        seekingType = AppConstants.Seekingtype.Apartment;
                        break;
                    case 2:
                        seekingType = AppConstants.Seekingtype.Commercial_Space;
                        break;
                    case 3:
                        seekingType = AppConstants.Seekingtype.Land;
                        break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick({R.id.logo_land, R.id.txt_btn_next, R.id.txt_btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logo_land:
                break;
            case R.id.txt_btn_next:
                goNext();
                break;
            case R.id.txt_btn_login:
                goNext();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // GET GOOGLE  TOKEN FOR PUSH NOTIFICATION....................
        new StartRegisterationService().execute();
    }

    private void goNext() {
        final Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.ParmsType.lookingType, lookinType);
        bundle.putInt(AppConstants.ParmsType.seekingType, seekingType);
        showLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoading();
                activitySwitcher(LandingActivity.this, LoginActivity.class, bundle);

            }
        }, AppConstants.TimeLimits.MEDIUM);

    }

    private class StartRegisterationService extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(LandingActivity.this, MyFirebaseInstanceIDService.class);
            startService(intent);
            return null;
        }
    }
}
