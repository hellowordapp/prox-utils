package com.proxglobal.proxads.adsv2.ads

import android.app.Activity
import android.content.Context
import com.proxglobal.purchase.ProxPurchase
import com.proxglobal.proxads.adsv2.admax.MaxInterstitialAds
import com.proxglobal.proxads.adsv2.callback.AdsCallback
import com.kaopiz.kprogresshud.KProgressHUD
import android.widget.FrameLayout
import com.proxglobal.proxads.adsv2.admax.MaxBannerAds
import com.proxglobal.proxads.adsv2.admax.MaxNativeAds
import com.facebook.ads.AdSettings
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinMediationProvider
import com.applovin.sdk.AppLovinSdkConfiguration
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.view.View
import com.proxglobal.proxads.R
import com.proxglobal.proxads.adsv2.callback.LoadCallback
import java.util.HashMap

class ProxAds private constructor() {

    // ----------------------- Splash -----------------------
    fun showSplashMax(
        activity: Activity, callback: AdsCallback,
        adsId: String, timeout: Int
    ) {
        if (ProxPurchase.getInstance().checkPurchased()) {
            callback.onError()
            return
        }
        var splashDone = false
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            if (!splashDone) {
                callback.onError()
                splashDone = true
            }
        }
        handler.postDelayed(runnable, timeout.toLong())
        var splashAds: MaxInterstitialAds? = MaxInterstitialAds(activity, adsId)
        splashAds!!.setLoadCallback(object : LoadCallback {
            override fun onLoadSuccess() {
                if (splashDone) return
                splashAds!!.turnOffAutoReload()
                showAdsWithKHub(activity, splashAds!!, callback)
                handler.removeCallbacksAndMessages(null)
                splashAds = null
            }

            override fun onLoadFailed() {}
        }).load()
    }

    // ----------------------- Interstitial -----------------------
    private val adsStorage: HashMap<String, InterAds<*>> = HashMap()

    fun initInterstitialMax(activity: Activity, adsId: String, tag: String) {
        if (ProxPurchase.getInstance().checkPurchased()) return
        val ads: InterAds<*> = MaxInterstitialAds(activity, adsId)
        adsStorage[tag] = ads
        ads.load()
    }

    fun showInterstitialMax(activity: Activity, tag: String, callback: AdsCallback) {
        if (ProxPurchase.getInstance().checkPurchased()) {
            callback.onError()
            return
        }
        val ads = adsStorage[tag]
        if (ads == null) {
            callback.onError()
            return
        }
        if (!ads.isAvailable) {
            ads.show(activity, callback)
            return
        }
        if (ads.inLoading()) {
            callback.onError()
            return
        }
        showAdsWithKHub(activity, ads, callback)
    }

    private fun clearStorage() {
        adsStorage.clear()
    }

    private fun showAdsWithKHub(activity: Activity, ads: BaseAds<*>, callback: AdsCallback) {
        val khub = createKHub(activity)
        khub.show()
        Handler(Looper.getMainLooper()).postDelayed({
            ads.show(activity, callback)
            khub.dismiss()
        }, 700)
    }

    // ----------------------- Banner -----------------------
    fun showBannerMax(
        activity: Activity,
        container: FrameLayout?,
        adId: String?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased()) {
            callback.onError()
            if (container != null) {
                container.visibility = View.GONE
            }
            return
        }
        if (!isNetworkAvailable(activity)) {
            callback.onError()
            if (container != null) {
                container.visibility = View.GONE
            }
        }
        MaxBannerAds(activity, container, adId).load()!!.show(activity, callback)
    }

    // ---------------------- Native -------------------------
    fun showSmallNativeMaxWithShimmerStyle15(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds =
            MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_small_15)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_small_15)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showSmallNativeMaxWithShimmerStyle15(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        styleButtonAds: Int,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(
            activity,
            adContainer,
            adId,
            R.layout.ads_native_small_15,
            styleButtonAds
        )
        maxNativeAds.enableShimmer(R.layout.shimmer_native_small_15)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showSmallNativeMaxWithShimmerStyle16(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds =
            MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_small_16)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_small_16)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showSmallNativeMaxWithShimmerStyle16(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        styleButtonAds: Int,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(
            activity,
            adContainer,
            adId,
            R.layout.ads_native_small_16,
            styleButtonAds
        )
        maxNativeAds.enableShimmer(R.layout.shimmer_native_small_16)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showSmallNativeMaxWithShimmerStyle21(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds =
            MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_small_21)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_small_21)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showSmallNativeMaxWithShimmerStyle21(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        styleButtonAds: Int,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(
            activity,
            adContainer,
            adId,
            R.layout.ads_native_small_21,
            styleButtonAds
        )
        maxNativeAds.enableShimmer(R.layout.shimmer_native_small_21)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showSmallNativeMaxWithShimmerStyle22(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds =
            MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_small_22)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_small_22)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showSmallNativeMaxWithShimmerStyle22(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        styleButtonAds: Int,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(
            activity,
            adContainer,
            adId,
            R.layout.ads_native_small_22,
            styleButtonAds
        )
        maxNativeAds.enableShimmer(R.layout.shimmer_native_small_22)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showMediumNativeMaxWithShimmerStyle19(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds =
            MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_medium_19)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_medium_19)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showMediumNativeMaxWithShimmerStyle19(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        styleButtonAds: Int,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(
            activity,
            adContainer,
            adId,
            R.layout.ads_native_medium_19,
            styleButtonAds
        )
        maxNativeAds.enableShimmer(R.layout.shimmer_native_medium_19)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showMediumNativeMaxWithShimmerStyle20(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds =
            MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_medium_20)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_medium_20)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showMediumNativeMaxWithShimmerStyle20(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        styleButtonAds: Int,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(
            activity,
            adContainer,
            adId,
            R.layout.ads_native_medium_20,
            styleButtonAds
        )
        maxNativeAds.enableShimmer(R.layout.shimmer_native_medium_20)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxStyle1(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        MaxNativeAds(
            activity,
            adContainer,
            adId,
            R.layout.ads_native_big_1
        ).load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle1(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_1)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_1)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle1(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        styleButtonAds: Int,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds =
            MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_1, styleButtonAds)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_1)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle2(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_2)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_2)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle2(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        styleButtonAds: Int,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds =
            MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_2, styleButtonAds)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_2)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle3(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_3)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_3)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle3(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        styleButtonAds: Int,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds =
            MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_3, styleButtonAds)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_3)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle4(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_4)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_4)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle4(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        styleButtonAds: Int,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds =
            MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_4, styleButtonAds)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_4)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle5(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_5)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_5)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle5(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        styleButtonAds: Int,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds =
            MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_5, styleButtonAds)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_5)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle6(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_6)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_6)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle6(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        styleButtonAds: Int,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds =
            MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_6, styleButtonAds)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_6)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle7(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_7)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_7)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle7(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        styleButtonAds: Int,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds =
            MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_7, styleButtonAds)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_7)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle9(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_9)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_9)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle9(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        styleButtonAds: Int,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds =
            MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_9, styleButtonAds)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_9)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle10(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_10)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_10)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle10(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        styleButtonAds: Int,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(
            activity,
            adContainer,
            adId,
            R.layout.ads_native_big_10,
            styleButtonAds
        )
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_10)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle11(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_11)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_11)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle11(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        styleButtonAds: Int,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(
            activity,
            adContainer,
            adId,
            R.layout.ads_native_big_11,
            styleButtonAds
        )
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_11)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle12(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_12)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_12)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle12(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        styleButtonAds: Int,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(
            activity,
            adContainer,
            adId,
            R.layout.ads_native_big_12,
            styleButtonAds
        )
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_12)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle13(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_13)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_13)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle13(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        styleButtonAds: Int,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(
            activity,
            adContainer,
            adId,
            R.layout.ads_native_big_13,
            styleButtonAds
        )
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_13)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle14(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(activity, adContainer, adId, R.layout.ads_native_big_14)
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_14)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeMaxWithShimmerStyle14(
        activity: Activity,
        adId: String?,
        adContainer: FrameLayout?,
        styleButtonAds: Int,
        callback: AdsCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased() || !isNetworkAvailable(activity)) {
            callback.onError()
            if (adContainer != null) {
                adContainer.visibility = View.GONE
            }
            return
        }
        val maxNativeAds = MaxNativeAds(
            activity,
            adContainer,
            adId,
            R.layout.ads_native_big_14,
            styleButtonAds
        )
        maxNativeAds.enableShimmer(R.layout.shimmer_native_big_14)
        maxNativeAds.load()!!.show(activity, callback)
    }

    fun initMax(context: Context?) {
        //bật chế độ LDU cho người dùng ở California
        AdSettings.setDataProcessingOptions(arrayOf("LDU"), 1, 1000)

        // Initialize the AppLovin SDK
        AppLovinSdk.getInstance(context).mediationProvider = AppLovinMediationProvider.MAX
        AppLovinSdk.getInstance(context).initializeSdk { config: AppLovinSdkConfiguration? -> }
    }

    fun showMaxMediationDebug(context: Context?) {
        AppLovinSdk.getInstance(context).showMediationDebugger()
    }

    companion object {
        private var INSTANCE: ProxAds? = null

        @JvmStatic
        val instance: ProxAds
            get() {
                if (INSTANCE == null) {
                    synchronized(ProxAds::class.java) {
                        if (INSTANCE == null) {
                            INSTANCE = ProxAds()
                        }
                    }
                }
                return INSTANCE!!
            }

        @JvmStatic
        fun createKHub(activity: Activity): KProgressHUD {
            return KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(activity.getString(R.string._loading_ads))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setAutoDismiss(true)
                .setDimAmount(0.5f)
        }

        @JvmStatic
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}