package com.proxglobal.proxads.adsv2.admax

import android.app.Activity
import android.util.Log
import com.proxglobal.proxads.adsv2.ads.InterAds
import com.applovin.mediation.ads.MaxInterstitialAd
import com.kaopiz.kprogresshud.KProgressHUD
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError

class MaxInterstitialAds(activity: Activity?, adId: String?) :
    InterAds<MaxInterstitialAd?>(activity, adId) {
    private var kProgressHUD: KProgressHUD? = null
    override fun specificLoadAdsMethod() {
        ads = MaxInterstitialAd(adId, mActivity)
        ads!!.setRevenueListener { ad ->
            Log.d("ntduc", "InterstitialMax Revenue: " + ad.revenue)
            Log.d("ntduc", "InterstitialMax NetworkName: " + ad.networkName)
            Log.d("ntduc", "InterstitialMax AdUnitId: " + ad.adUnitId)
            Log.d("ntduc", "InterstitialMax Placement: " + ad.placement)
            Log.d("ntduc", "-------------------------------------------")
        }
        ads!!.setListener(object : MaxAdListener {
            override fun onAdLoaded(ad: MaxAd) {
                onLoadSuccess()
            }

            override fun onAdDisplayed(ad: MaxAd) {
                onShowSuccess()
                isShowing = true
                if (kProgressHUD == null) {
                    kProgressHUD = createKHub(mActivity)
                }
                kProgressHUD!!.show()
            }

            override fun onAdHidden(ad: MaxAd) {
                onClosed()
                isShowing = false
                if (kProgressHUD != null) {
                    kProgressHUD!!.dismiss()
                    kProgressHUD = null
                }
            }

            override fun onAdClicked(ad: MaxAd) {}
            override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
                onShowError()
                onLoadFailed()
                isShowing = false
                if (ads == null) {
                    ads = MaxInterstitialAd(adId, mActivity)
                }
                ads!!.loadAd()
            }

            override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
                onShowError()
                isShowing = false
                if (ads == null) {
                    ads = MaxInterstitialAd(adId, mActivity)
                }
                ads!!.loadAd()
            }
        })
        ads!!.loadAd()
    }

    override fun specificShowAdsMethod(activity: Activity?) {
        if (ads!!.isReady) {
            ads!!.showAd()
        }
    }

    private fun createKHub(activity: Activity): KProgressHUD {
        return KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(false)
            .setAnimationSpeed(2)
            .setAutoDismiss(true)
            .setDimAmount(0.5f)
    }

    companion object {
        @JvmField
        var isShowing = false
    }
}