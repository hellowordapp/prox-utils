package com.proxglobal.proxads.adsv2.admax

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.proxglobal.proxads.adsv2.ads.NativeAds
import com.applovin.mediation.ads.MaxAdView
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.proxglobal.proxads.adsv2.ads.ProxAds
import com.applovin.mediation.MaxError
import android.view.ViewGroup
import com.applovin.sdk.AppLovinSdkUtils
import com.proxglobal.proxads.R

class MaxBannerAds(activity: Activity?, container: FrameLayout?, adId: String?) :
    NativeAds<MaxAdView?>(activity, container, adId) {

    init {
        this.adId = adId!!
        mContainer = container
        enableShimmer(R.layout.shimmer_banner_max)
    }

    override fun specificLoadAdsMethod() {
        ads = MaxAdView(adId, mActivity)
        val adsTemp = ads!!
        ads!!.layoutParams = adSize
        ads!!.setRevenueListener { ad ->
            Log.d("ntduc", "BannerMax Revenue: " + ad.revenue)
            Log.d("ntduc", "BannerMax NetworkName: " + ad.networkName)
            Log.d("ntduc", "BannerMax AdUnitId: " + ad.adUnitId)
            Log.d("ntduc", "BannerMax Placement: " + ad.placement)
            Log.d("ntduc", "-------------------------------------------")
        }
        ads!!.setListener(object : MaxAdViewAdListener {
            override fun onAdExpanded(ad: MaxAd) {}
            override fun onAdCollapsed(ad: MaxAd) {}
            override fun onAdLoaded(ad: MaxAd) {
                onLoadSuccess()
                if (ProxAds.isNetworkAvailable(mActivity)) {
                    mContainer!!.visibility = View.VISIBLE
                }
            }

            override fun onAdDisplayed(ad: MaxAd) {
                onShowSuccess()
                if (shimmer != null && !mActivity.isDestroyed) {
                    mContainer!!.removeView(shimmer)
                    shimmer = null
                }
            }

            override fun onAdHidden(ad: MaxAd) {
                adsTemp.destroy()
            }

            override fun onAdClicked(ad: MaxAd) {}
            override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
                onShowError()
                onLoadFailed()
                if (!ProxAds.isNetworkAvailable(mActivity)) {
                    mContainer!!.visibility = View.GONE
                }
            }

            override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
                onShowError()
            }
        })
        ads!!.loadAd()
    }

    override fun specificShowAdsMethod(activity: Activity?) {
        mContainer!!.addView(ads)
    }

    // Set the height of the banner ad based on the device type.
    private val adSize:
    // Banner width must match the screen to be fully functional.
            ViewGroup.LayoutParams
        get() {
            // Set the height of the banner ad based on the device type.
            val isTablet = AppLovinSdkUtils.isTablet(mActivity)
            val heightPx = AppLovinSdkUtils.dpToPx(mActivity, if (isTablet) 90 else 50)
            // Banner width must match the screen to be fully functional.
            return FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx)
        }
}