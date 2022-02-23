package com.proxglobal.proxads.remote_config;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.LayoutMode;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.bottomsheets.BottomSheet;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.proxglobal.proxads.R;
import com.proxglobal.proxads.remote_config.callback.RemoteConfigCallback;

public class ProxRemoteConfig {
    private static final String TAG = "ProxRemoteConfig";

    public static final String PREF_RATE = "PREF_RATE";
    private int iconAppId;
    private String appName;

    private RemoteConfigCallback callback;
    private long fetchTime = 12 * 60 * 60;

    public ProxRemoteConfig(int iconAppId, String appName) {
        this.iconAppId = iconAppId;
        this.appName = appName;
    }

    public void setReleaseFetchTime(long fetchTime) {
        this.fetchTime = fetchTime;
    }

    public void showRemoteConfigIfNecessary(AppCompatActivity activity, int appVersionCode, boolean isDebug) {
        FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
        long minFetch = fetchTime;
        if (isDebug) {
            minFetch = 0;
        }

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(minFetch).build();

        config.setConfigSettingsAsync(configSettings);

        config.fetchAndActivate().addOnCompleteListener(activity, task -> {
            if (!task.isSuccessful()) return;
            String json = config.getString("config_update_version");

            ConfigUpdateVersion result = new Gson().fromJson(json, ConfigUpdateVersion.class);
            
            if (result.isRequired) {
                for (int version : result.versionCodeRequired) {
                    if (version == appVersionCode) {
                        if(callback == null) {
                            UpdateDialog updateDialog = new UpdateDialog(iconAppId, appName);
                            updateDialog.showDialog(activity.getSupportFragmentManager(), result);
                            break;
                        } else {
                            callback.onRequireUpdateConfig(result);
                        }
                    }
                }
            }
            else if (!result.status) {
                if(callback == null) {
                    return;
                } else {
                    callback.onNull();
                }
            }
            else if (result.versionCode > appVersionCode) {
                if(callback == null) {
                    showBottomSheetUpdate(activity, result);
                } else {
                    callback.onOptionsUpdateConfig(result);
                }
            }
        });
    }

    private void showBottomSheetUpdate(Activity activity, ConfigUpdateVersion config) {
        BottomSheet bottomSheet = new BottomSheet(LayoutMode.WRAP_CONTENT);
        MaterialDialog dialog = new MaterialDialog(activity, bottomSheet);

        dialog.getView().contentLayout.addCustomView(R.layout.bottom_remote_update, null, false, false);

        ((TextView) dialog.findViewById(R.id.bru_title)).setText(config.title);
        ((TextView) dialog.findViewById(R.id.bru_version_name)).setText(config.versionName);
        ((TextView) dialog.findViewById(R.id.bru_message)).setText(config.message);
        ((ImageView) dialog.findViewById(R.id.bru_icon)).setBackgroundResource(iconAppId);
        ((TextView) dialog.findViewById(R.id.bru_app_title)).setText(appName);

        ((Button) dialog.findViewById(R.id.bru_update)).setOnClickListener(v ->{
            linkToStore(activity, config.newPackage);
            dialog.cancel();
        });

        dialog.show();
    }

    public ProxRemoteConfig setRemoteConfigCallback(RemoteConfigCallback callback) {
        this.callback = callback;
        return this;
    }

    private void linkToStore(Activity activity, String newPackage) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(
                PREF_RATE, Context.MODE_PRIVATE
        );

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(PREF_RATE, true);
        editor.apply();

        String appPackageName = "";
        if (newPackage.equals("")) {
            appPackageName = activity.getPackageName();
        } else {
            appPackageName = newPackage;
        }

        try {
            activity.startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + appPackageName)
                    )
            );
        } catch (ActivityNotFoundException e) {
            activity.startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)
                    )
            );
        }
    }
}
