package com.proxglobal.proxads.adsv2.adgoogle

import android.app.Activity
import android.widget.FrameLayout
import com.proxglobal.proxads.adsv2.ads.NativeAds
import android.util.DisplayMetrics
import com.google.android.gms.ads.*
import com.proxglobal.proxads.R

class GoogleBannerAds(activity: Activity?, container: FrameLayout?, adId: String?) :
    NativeAds<AdView?>(activity, container!!, adId) {

    init {
        this.adId = adId!!
        mContainer = container!!
        enableShimmer(R.layout.shimmer_banner)
    }

    override fun specificLoadAdsMethod() {
        ads = AdView(mActivity)
        ads!!.setAdSize(adSize)
        ads!!.adUnitId = adId
        mContainer.addView(ads)
    }

    override fun specificShowAdsMethod(activity: Activity?) {
        ads!!.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                onLoadSuccess()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                onClosed()
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                onShowError()
                onLoadFailed()
            }

            override fun onAdImpression() {
                super.onAdImpression()
                onShowSuccess()
            }
        }
        val adRequest = AdRequest.Builder().build()
        ads!!.loadAd(adRequest)
    }

    // Determine the screen width (less decorations) to use for the ad width.
    private val adSize: AdSize
        get() {
            // Determine the screen width (less decorations) to use for the ad width.
            val display = mActivity.windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)
            val widthPixels = outMetrics.widthPixels.toFloat()
            val density = outMetrics.density
            val adWidth = (widthPixels / density).toInt()

            // Get adaptive ad size and return for setting on the ad view.
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mActivity, adWidth)
        }
}