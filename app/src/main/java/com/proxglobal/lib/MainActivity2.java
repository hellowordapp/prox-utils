package com.proxglobal.lib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.proxglobal.proxads.ProxInterstitialAd;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        new ProxInterstitialAd(this, ProxConstant.TEST_ADS_INTERSTITIAL).loadSplash(10000, this::finish);
    }
}