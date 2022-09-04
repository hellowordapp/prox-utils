package com.proxglobal.proxads.adsv2.ads

import android.app.Activity
import com.proxglobal.purchase.ProxPurchase
import com.proxglobal.proxads.adsv2.adgoogle.GoogleInterstitialAd
import com.proxglobal.proxads.adsv2.callback.LoadCallback
import com.proxglobal.proxads.adsv2.adcolony.ColonyInterstitialAd
import com.proxglobal.proxads.adsv2.callback.AdsCallback
import com.kaopiz.kprogresshud.KProgressHUD
import com.proxglobal.proxads.adsv2.adgoogle.GoogleRewardAds
import com.proxglobal.proxads.adsv2.callback.RewardCallback
import com.adcolony.sdk.AdColony
import android.app.Application
import android.content.Context
import com.adcolony.sdk.AdColonyAppOptions
import android.widget.FrameLayout
import com.proxglobal.proxads.adsv2.adgoogle.GoogleBannerAds
import com.proxglobal.proxads.adsv2.adgoogle.GoogleNativeAds
import com.proxglobal.proxads.R
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.view.View
import java.util.*

class ProxAds private constructor() {

    // ----------------------- Splash -----------------------
    var splashAds: InterAds<*>? = null
    private var splashDone = false

    fun showSplash(
        activity: Activity, callback: AdsCallback,
        googleAdsId: String, colonyZoneId: String?,
        timeout: Int
    ) {
        if (ProxPurchase.getInstance().checkPurchased()) {
            callback.onError()
            return
        }
        splashDone = false
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            if (!splashDone) {
                callback.onError()
                splashDone = true
            }
        }
        handler.postDelayed(runnable, timeout.toLong())
        splashAds = GoogleInterstitialAd(activity, googleAdsId)
        splashAds!!.setLoadCallback(object : LoadCallback {
            override fun onLoadSuccess() {
                if (splashDone) return
                splashAds!!.turnOffAutoReload()
                showAdsWithKHub(activity, splashAds!!, callback)
                handler.removeCallbacksAndMessages(null)
                splashAds = null
            }

            override fun onLoadFailed() {
                if (splashDone) return
                if (colonyZoneId != null) {
                    splashAds =
                        ColonyInterstitialAd(colonyZoneId).setLoadCallback(object : LoadCallback {
                            override fun onLoadSuccess() {
                                if (splashDone) return
                                splashAds!!.turnOffAutoReload()
                                showAdsWithKHub(activity, splashAds!!, callback)
                                handler.removeCallbacksAndMessages(null)
                                splashAds = null
                            }

                            override fun onLoadFailed() {
                                splashAds = null
                                splashDone = true
                                handler.removeCallbacksAndMessages(null)
                                callback.onError()
                            }
                        }) as InterAds<*>
                    splashAds!!.load()
                }
            }
        }).load()
    }

    // ----------------------- Interstitial -----------------------
    private val adsStorage: HashMap<String, BaseAds<*>?> = HashMap()

    fun initInterstitial(
        activity: Activity, googleAdsId: String,
        colonyZoneId: String?, tag: String
    ) {
        if (ProxPurchase.getInstance().checkPurchased()) return
        val ads: InterAds<*> = GoogleInterstitialAd(activity, googleAdsId)
        adsStorage[tag] = ads
        ads.setLoadCallback(object : LoadCallback {
            override fun onLoadSuccess() {}
            override fun onLoadFailed() {
                if (colonyZoneId != null) {
                    adsStorage[tag] = ColonyInterstitialAd(colonyZoneId).load()
                }
            }
        }).load()
    }

    fun showInterstitial(activity: Activity, tag: String, callback: AdsCallback) {
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
    fun showBanner(
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
        GoogleBannerAds(activity, container, adId).load()!!.show(activity, callback)
    }

    // ---------------------- Native -------------------------
    fun showSmallNativeWithShimmerStyle15(
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
        val nativeAds = GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_small_15)
        nativeAds.enableShimmer(R.layout.shimmer_native_small_15)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showSmallNativeWithShimmerStyle15(
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
        val nativeAds = GoogleNativeAds(
            activity,
            adContainer,
            adId,
            R.layout.ads_native_small_15,
            styleButtonAds
        )
        nativeAds.enableShimmer(R.layout.shimmer_native_small_15)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showSmallNativeWithShimmerStyle16(
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
        val nativeAds = GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_small_16)
        nativeAds.enableShimmer(R.layout.shimmer_native_small_16)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showSmallNativeWithShimmerStyle16(
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
        val nativeAds = GoogleNativeAds(
            activity,
            adContainer,
            adId,
            R.layout.ads_native_small_16,
            styleButtonAds
        )
        nativeAds.enableShimmer(R.layout.shimmer_native_small_16)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showSmallNativeWithShimmerStyle21(
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
        val nativeAds = GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_small_21)
        nativeAds.enableShimmer(R.layout.shimmer_native_small_21)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showSmallNativeWithShimmerStyle21(
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
        val nativeAds = GoogleNativeAds(
            activity,
            adContainer,
            adId,
            R.layout.ads_native_small_21,
            styleButtonAds
        )
        nativeAds.enableShimmer(R.layout.shimmer_native_small_21)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showSmallNativeWithShimmerStyle22(
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
        val nativeAds = GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_small_22)
        nativeAds.enableShimmer(R.layout.shimmer_native_small_22)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showSmallNativeWithShimmerStyle22(
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
        val nativeAds = GoogleNativeAds(
            activity,
            adContainer,
            adId,
            R.layout.ads_native_small_22,
            styleButtonAds
        )
        nativeAds.enableShimmer(R.layout.shimmer_native_small_22)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showMediumNativeWithShimmerStyle19(
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
        val nativeAds = GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_medium_19)
        nativeAds.enableShimmer(R.layout.shimmer_native_medium_19)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showMediumNativeWithShimmerStyle19(
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
        val nativeAds = GoogleNativeAds(
            activity,
            adContainer,
            adId,
            R.layout.ads_native_medium_19,
            styleButtonAds
        )
        nativeAds.enableShimmer(R.layout.shimmer_native_medium_19)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showMediumNativeWithShimmerStyle20(
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
        val nativeAds = GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_medium_20)
        nativeAds.enableShimmer(R.layout.shimmer_native_medium_20)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showMediumNativeWithShimmerStyle20(
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
        val nativeAds = GoogleNativeAds(
            activity,
            adContainer,
            adId,
            R.layout.ads_native_medium_20,
            styleButtonAds
        )
        nativeAds.enableShimmer(R.layout.shimmer_native_medium_20)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle1(
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
        val nativeAds = GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_1)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_1)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle1(
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
        val nativeAds =
            GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_1, styleButtonAds)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_1)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle2(
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
        val nativeAds = GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_2)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_2)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle2(
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
        val nativeAds =
            GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_2, styleButtonAds)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_2)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle3(
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
        val nativeAds = GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_3)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_3)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle3(
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
        val nativeAds =
            GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_3, styleButtonAds)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_3)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle4(
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
        val nativeAds = GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_4)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_4)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle4(
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
        val nativeAds =
            GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_4, styleButtonAds)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_4)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle5(
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
        val nativeAds = GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_5)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_5)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle5(
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
        val nativeAds =
            GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_5, styleButtonAds)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_5)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle6(
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
        val nativeAds = GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_6)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_6)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle6(
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
        val nativeAds =
            GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_6, styleButtonAds)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_6)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle7(
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
        val nativeAds = GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_7)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_7)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle7(
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
        val nativeAds =
            GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_7, styleButtonAds)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_7)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle9(
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
        val nativeAds = GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_9)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_9)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle9(
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
        val nativeAds =
            GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_9, styleButtonAds)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_9)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle10(
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
        val nativeAds = GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_10)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_10)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle10(
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
        val nativeAds =
            GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_10, styleButtonAds)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_10)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle11(
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
        val nativeAds = GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_11)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_11)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle11(
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
        val nativeAds =
            GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_11, styleButtonAds)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_11)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle12(
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
        val nativeAds = GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_12)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_12)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle12(
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
        val nativeAds =
            GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_12, styleButtonAds)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_12)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle13(
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
        val nativeAds = GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_13)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_13)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle13(
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
        val nativeAds =
            GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_13, styleButtonAds)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_13)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle14(
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
        val nativeAds = GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_14)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_14)
        nativeAds.load()!!.show(activity, callback)
    }

    fun showBigNativeWithShimmerStyle14(
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
        val nativeAds =
            GoogleNativeAds(activity, adContainer, adId, R.layout.ads_native_big_14, styleButtonAds)
        nativeAds.enableShimmer(R.layout.shimmer_native_big_14)
        nativeAds.load()!!.show(activity, callback)
    }

    // ----------------------- Reward -------------------------
    fun initRewardAds(activity: Activity, googleAdsId: String, tag: String) {
        if (ProxPurchase.getInstance().checkPurchased()) return
        val ads: RewardAds<*> = GoogleRewardAds(activity, googleAdsId)
        adsStorage[tag] = ads
        ads.load()
    }

    fun showRewardAds(
        activity: Activity,
        tag: String,
        callback: AdsCallback,
        rewardCallback: RewardCallback
    ) {
        if (ProxPurchase.getInstance().checkPurchased()) {
            callback.onError()
            return
        }
        val ads = adsStorage[tag] as GoogleRewardAds?
        if (ads == null) {
            callback.onError()
            return
        }
        if (!ads.isAvailable) {
            ads.show(activity, callback, rewardCallback)
            return
        }
        showRewardAdsWithKHub(activity, ads, callback, rewardCallback)
    }

    private fun showRewardAdsWithKHub(
        activity: Activity,
        ads: GoogleRewardAds,
        callback: AdsCallback,
        rewardCallback: RewardCallback
    ) {
        val khub = createKHub(activity)
        khub.show()
        Handler(Looper.getMainLooper()).postDelayed({
            ads.show(activity, callback, rewardCallback)
            khub.dismiss()
        }, 700)
    }

    // --------------- config ad colony ------------------------
    /**
     * this method due to configure adcolony in only once time
     * @param activity
     * @param appId
     * @param zoneIds
     */
    fun configure(activity: Activity?, appId: String?, vararg zoneIds: String?) {
        AdColony.configure(activity, appId!!, *zoneIds)
    }

    /**
     * this method due to configure adcolony in only once time
     * @param application
     * @param appId
     * @param zoneIds
     */
    fun configure(application: Application?, appId: String?, vararg zoneIds: String?) {
        AdColony.configure(application, appId!!, *zoneIds)
    }

    /**
     * this method due to configure adcolony in only once time
     * @param activity
     * @param appId
     * @param zoneIds
     */
    fun configure(
        activity: Activity?,
        options: AdColonyAppOptions?,
        appId: String?,
        vararg zoneIds: String?
    ) {
        AdColony.configure(activity, options, appId!!, *zoneIds)
    }

    /**
     * this method due to configure adcolony in only once time
     * @param application
     * @param appId
     * @param zoneIds
     */
    fun configure(
        application: Application?,
        options: AdColonyAppOptions?,
        appId: String?,
        vararg zoneIds: String?
    ) {
        AdColony.configure(application, options, appId!!, *zoneIds)
    }

    // ------------------- show sequence ads ---------------
    fun initInterstitials(activity: Activity, tag: String, vararg adss: InterAds<*>) {
        if (ProxPurchase.getInstance().checkPurchased() || adss.isEmpty()) return
        val idAdsStack = Stack<InterAds<*>>()
        idAdsStack.addAll(listOf(*adss))
        val loadAds = arrayOf(idAdsStack.pop())
        val isSuccess = booleanArrayOf(false)
        val callback: LoadCallback = object : LoadCallback {
            override fun onLoadSuccess() {
                isSuccess[0] = true
            }

            override fun onLoadFailed() {
                loadAds[0] = idAdsStack.pop()
            }
        }
        while (!idAdsStack.isEmpty() && !isSuccess[0]) {
            loadAds[0].setLoadCallback(callback)
            loadAds[0].load()
        }
        if (isSuccess[0]) adsStorage[tag] = loadAds[0]
        idAdsStack.clear()
    }

    class Factory {
        fun getInterAds(activity: Activity, adId: String?, type: AdsType?): InterAds<*>? {
            when (type) {
                AdsType.GOOGLE -> return GoogleInterstitialAd(activity, adId)
                AdsType.COLONY -> return ColonyInterstitialAd(adId)
            }
            return null
        }
    }

    enum class AdsType {
        GOOGLE, COLONY
    }

    companion object {
        private var INSTANCE: ProxAds? = null

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