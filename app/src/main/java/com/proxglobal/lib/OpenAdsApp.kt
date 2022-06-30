package com.proxglobal.lib

import com.proxglobal.proxads.ProxUtils
import com.proxglobal.proxads.adsv2.admax.openads.MaxOpenAdsApplication
import com.proxglobal.proxads.adsv2.ads.ProxAds
import com.proxglobal.purchase.ProxPurchase

class OpenAdsApp : MaxOpenAdsApplication() {
    override fun onCreate() {
        super.onCreate()

        ProxPurchase.getInstance().initBilling(
            this, listOf(
                BuildConfig.id_test1_purchase
            ), listOf(
                BuildConfig.id_test2_subs,
                BuildConfig.id_test3_subs
            )
        )

        ProxAds.getInstance().initMax(this);
    }

    override fun getOpenAdsId(): String = ProxUtils.TEST_INTERSTITIAL_MAX_ID

}