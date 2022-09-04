package com.proxglobal.proxads.adsv2.adgoogle

import android.app.Activity
import com.proxglobal.proxads.adsv2.ads.InterAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.FullScreenContentCallback
import com.proxglobal.proxads.adsv2.callback.AdsCallback
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest

class GoogleInterstitialAd(activity: Activity?, adId: String?) :
    InterAds<InterstitialAd?>(activity, adId) {

    companion object {
        @JvmField
        var isShowing = false
    }

    private var mListener: GoogleInterstitialCallback? = null
        get() {
            if (field == null) {
                field = GoogleInterstitialCallback()
            }
            return field
        }

    override fun show(activity: Activity?, callback: AdsCallback?) {
        setAdsCallback(callback)
        show(activity)
    }

    override fun specificLoadAdsMethod() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            mActivity.applicationContext,
            adId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    interstitialAd.fullScreenContentCallback = mListener
                    ads = interstitialAd
                    onLoadSuccess()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    ads = null
                    onLoadFailed()
                }
            })
    }

    override fun specificShowAdsMethod(activity: Activity?) {
        ads!!.show(activity!!)
    }

    private inner class GoogleInterstitialCallback : FullScreenContentCallback() {
        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
            onShowError()
            isShowing = false
        }

        override fun onAdDismissedFullScreenContent() {
            onClosed()
            isShowing = false
        }

        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
            onShowSuccess()
            isShowing = true
        }
    }
}