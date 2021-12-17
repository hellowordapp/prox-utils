package com.proxglobal.lib;

import android.os.Bundle;
import com.proxglobal.proxads.ProxUtils;
import com.proxglobal.proxads.ads.ProxInterstitialAd;
import com.proxglobal.proxads.ads.callback.AdCallback;

public class MainActivity2 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        new ProxInterstitialAd(this, ProxUtils.INSTANCE.TEST_INTERSTITIAL_ID).loadSplash(10000, new AdCallback() {
            @Override
            public void onAdClose() {
                MainActivity2.this.finish();
            }
        });
    }
}