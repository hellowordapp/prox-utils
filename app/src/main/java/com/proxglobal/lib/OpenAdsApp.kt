package com.proxglobal.lib

import android.util.Log
import com.proxglobal.proxads.ads.openads.ProxOpenAdsApplication
import com.proxglobal.proxads.adsv2.ads.ProxAds
import com.proxglobal.purchase.ProxPurchase

class OpenAdsApp: ProxOpenAdsApplication() {
    override fun onCreate() {
        super.onCreate()
        Log.d("vanh", "onCreate: init openads")

        ProxPurchase.getInstance().initBilling(this, listOf(
                BuildConfig.id_test1_purchase
        ), listOf(
                BuildConfig.id_test2_subs,
                BuildConfig.id_test3_subs
        ))

        ProxAds.getInstance().initMax(this);
    }

    override fun getOpenAdsId(): String = "ca-app-pub-3940256099942544/3419835294"

}