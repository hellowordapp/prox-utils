package com.proxglobal.lib

import android.util.Log
import com.proxglobal.proxads.ads.openads.ProxOpenAdsApplication
import com.proxglobal.purchase.ProxPurchase
import java.util.*

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
    }

    override fun getOpenAdsId(): String = "ca-app-pub-3940256099942544/3419835294"

    override fun getListTestDeviceId(): MutableList<String> {
        return Collections.singletonList("EC25F576DA9B6CE74778B268CB87E431");
    }

}