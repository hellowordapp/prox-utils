package com.proxglobal.proxads;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.LayoutMode;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.bottomsheets.BottomSheet;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.proxglobal.proxads.ads.ProxInterstitialAd;
import com.proxglobal.proxads.ads.ProxNativeAd;
import com.proxglobal.proxads.remote_config.ConfigUpdateVersion;
import com.proxglobal.proxads.remote_config.UpdateDialog;

import java.util.function.Function;

public class ProxUtils {
    private ProxUtils() {
    }
    public static ProxUtils INSTANCE = new ProxUtils();

    public static String TAG = "ProxUtils";

    public static final String TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712";
    public static final String TEST_NATIVE_ID = "ca-app-pub-3940256099942544/2247696110";

    public ProxInterstitialAd createInterstitialAd (Activity activity, String adId) {
        return new ProxInterstitialAd(activity, adId);
    }

    public ProxNativeAd createNativeAd (Activity activity, String adId, FrameLayout adContainer, int layoutAdId) {
        return new ProxNativeAd(activity, adId, adContainer, layoutAdId);
    }

    public void initFirebaseRemoteConfig(AppCompatActivity activity, int appVersionCode) {
        UpdateDialog updateDialog = new UpdateDialog();

        FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
        long minFetch = 12 * 60 * 60;
        if (BuildConfig.DEBUG) {
            minFetch = 0;
        }

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(minFetch).build();

        config.setConfigSettingsAsync(configSettings);

        config.fetchAndActivate().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) return;
            String json = config.getString("config_update_version");
            Log.d(TAG, "RemoteConfig: " + json);

            ConfigUpdateVersion result = new Gson().fromJson(json, ConfigUpdateVersion.class);

            if (result.isRequired) {
                for (int version : result.versionCodeRequired) {
                    if (version == appVersionCode) {
                        updateDialog.showDialog(activity.getSupportFragmentManager());
                        break;
                    }
                }
            }
            else if (!result.status) return;
            else if (result.versionCode > appVersionCode) {
                showBottomSheetUpdate(activity, result);
            }


        });
    }

    private void showBottomSheetUpdate(Activity activity, ConfigUpdateVersion config) {
        new MaterialDialog(activity, new BottomSheet(LayoutMode.WRAP_CONTENT)).show();
    }



}
