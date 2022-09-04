package com.proxglobal.proxads.adsv2.adcolony

import com.proxglobal.proxads.adsv2.ads.InterAds
import com.adcolony.sdk.AdColonyInterstitial
import com.adcolony.sdk.AdColonyInterstitialListener
import android.app.Activity
import com.proxglobal.proxads.adsv2.callback.AdsCallback
import com.adcolony.sdk.AdColony
import androidx.annotation.CallSuper
import com.adcolony.sdk.AdColonyZone
import com.proxglobal.proxads.ads.openads.AppOpenManager

class ColonyInterstitialAd(zoneId: String?) : InterAds<AdColonyInterstitial?>(null, zoneId) {
    private var mListener: ColonyInterstitialCallback? = null
        get() {
            if (field == null) {
                field = ColonyInterstitialCallback()
            }
            return field
        }

    init {
        adId = zoneId!!
    }

    /**
     * show ads if it's available and return result via the callback
     * @param callback
     */
    override fun show(activity: Activity?, callback: AdsCallback?) {
        setAdsCallback(callback)
        show(activity)
    }

    override fun specificLoadAdsMethod() {
        AdColony.requestInterstitial(adId, mListener!!)
    }

    override fun specificShowAdsMethod(activity: Activity?) {
        ads!!.show()
    }

    // base callback for adcolony
    private inner class ColonyInterstitialCallback : AdColonyInterstitialListener() {
        @CallSuper
        override fun onRequestFilled(adColonyInterstitial: AdColonyInterstitial) {
            ads = adColonyInterstitial
            onLoadSuccess()
        }

        @CallSuper
        override fun onRequestNotFilled(zone: AdColonyZone) {
            super.onRequestNotFilled(zone)
            ads = null
            onLoadFailed()
        }

        @CallSuper
        override fun onClosed(ad: AdColonyInterstitial) {
            super.onClosed(ad)
            this@ColonyInterstitialAd.onClosed()
            AppOpenManager.getInstance().enableOpenAds()
        }

        @CallSuper
        override fun onOpened(ad: AdColonyInterstitial) {
            super.onOpened(ad)
            onShowSuccess()
            AppOpenManager.getInstance().disableOpenAds()
        }
    }
}