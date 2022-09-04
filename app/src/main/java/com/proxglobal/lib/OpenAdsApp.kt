package com.proxglobal.lib

import com.proxglobal.proxads.ProxUtils
import com.proxglobal.proxads.ads.openads.ProxOpenAdsApplication
import com.proxglobal.purchase.ProxPurchase
import java.util.*

class OpenAdsApp : ProxOpenAdsApplication() {
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
    }

    override fun getOpenAdsId(): String = ProxUtils.TEST_OPEN_APP_ID

    override fun getListTestDeviceId(): MutableList<String> {
        return Collections.singletonList("EC25F576DA9B6CE74778B268CB87E431");
    }
}