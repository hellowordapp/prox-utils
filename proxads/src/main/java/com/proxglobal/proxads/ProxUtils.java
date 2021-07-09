package com.proxglobal.proxads;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.LayoutMode;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.bottomsheets.BottomSheet;
import com.afollestad.materialdialogs.bottomsheets.BuildConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.proxglobal.proxads.ads.ProxInterstitialAd;
import com.proxglobal.proxads.ads.ProxNativeAd;
import com.proxglobal.proxads.remote_config.ConfigUpdateVersion;
import com.proxglobal.proxads.remote_config.ProxRemoteConfig;
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
        new ProxRemoteConfig().showRemoteConfigIfNecessary(activity, appVersionCode);
    }

}
