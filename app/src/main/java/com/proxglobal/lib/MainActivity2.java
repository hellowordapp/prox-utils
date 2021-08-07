package com.proxglobal.lib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.afollestad.materialdialogs.LayoutMode;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.bottomsheets.BottomSheet;
import com.proxglobal.proxads.ProxUtils;
import com.proxglobal.proxads.ads.ProxInterstitialAd;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        new ProxInterstitialAd(this, ProxUtils.INSTANCE.TEST_INTERSTITIAL_ID).loadSplash(10000, this::finish);
    }
}