package com.proxglobal.proxads.adsv2.remote_config

import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import com.google.gson.Gson
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.proxglobal.proxads.ads.openads.AppOpenManager
import com.proxglobal.proxads.adsv2.ads.ProxAds
import com.proxglobal.proxads.adsv2.callback.AdsCallback
import com.proxglobal.proxads.adsv2.remote_config.config.Ads
import java.util.HashMap

class ProxAdsConfig {
    private var fetchTime = 0L

    fun setReleaseFetchTime(fetchTime: Long) {
        this.fetchTime = fetchTime
    }

    fun init(activity: AppCompatActivity) {
        val config = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(fetchTime).build()
        config.setConfigSettingsAsync(configSettings)
        config.fetchAndActivate().addOnCompleteListener(activity) { task: Task<Boolean?> ->
            if (!task.isSuccessful) return@addOnCompleteListener

            val json = config.getString("config_ads")
            val result = Gson().fromJson(json, Ads::class.java)
            if (result.status) {
                setupAds(result)
            }
        }
    }

    private fun setupAds(config: Ads) {
        //Open Ads
        if (!config.status_open_ads) AppOpenManager.getInstance().disableOpenAds()

        //Splash ads
        statusSplash = config.splash?.status ?: false
        timeoutSplash = config.splash?.time ?: 0

        //Banner ads
        config.banners?.forEach {
            if (it.locations == null) return@forEach
            locationStorage[it.locations!!] = if (it.status) -1 else null
        }

        //Interstitial ads
        config.interstitials?.forEach {
            if (it.locations == null) return@forEach
            locationStorage[it.locations!!] = if (it.status) it.time else null
            numberClick[it.locations!!] = 0
        }

        //Native ads
        config.natives?.forEach {
            if (it.locations == null) return@forEach
            locationStorage[it.locations!!] = if (it.status) it.style else null
        }
    }

    // ----------------------- Splash -----------------------
    private var statusSplash = false
    private var timeoutSplash = 0
    fun showSplashIfNecessary(activity: Activity, googleAdsId: String, callback: AdsCallback) {
        if (!statusSplash) {
            callback.onError()
            return
        }
        ProxAds.instance.showSplash(activity, callback, googleAdsId, null, timeoutSplash)
    }

    // ----------------------- Banner -----------------------
    fun showBannerIfNecessary(
        activity: Activity,
        container: FrameLayout,
        location: String,
        adId: String,
        callback: AdsCallback
    ) {
        var locations: Array<String>? = null

        locationStorage.keys.forEach {
            if (it.contains(location)) {
                locations = it
                return@forEach
            }
        }
        if (locations == null || locationStorage[locations] == null) {
            container.visibility = View.GONE
            callback.onError()
            return
        }

        if (locationStorage[locations]!! == -1) {
            ProxAds.instance.showBanner(activity, container, adId, callback)
        } else {
            container.visibility = View.GONE
            callback.onError()
        }
    }

    // ----------------------- Interstitial -----------------------
    private val numberClick: HashMap<Array<String>, Int> = HashMap()

    fun showInterstitialIfNecessary(
        activity: Activity,
        location: String,
        tag: String,
        callback: AdsCallback
    ) {
        var locations: Array<String>? = null

        locationStorage.keys.forEach {
            if (it.contains(location)) {
                locations = it
                return@forEach
            }
        }
        if (locations == null
            || numberClick[locations] == null
            || locationStorage[locations] == null
            || locationStorage[locations]!! <= 0
        ) {
            callback.onError()
            return
        }
        numberClick[locations!!] = numberClick[locations!!]!! + 1

        if (numberClick[locations]!! % locationStorage[locations]!! == 0) {
            ProxAds.instance.showInterstitial(activity, tag, callback)
        } else {
            callback.onError()
        }
    }

    // ---------------------- Native -------------------------
    fun showNativeIfNecessary(
        activity: Activity,
        container: FrameLayout,
        location: String,
        adId: String,
        callback: AdsCallback,
        styleButtonAds: Int = ProxAds.NATIVE_DEFAULT
    ) {
        var locations: Array<String>? = null

        locationStorage.keys.forEach {
            if (it.contains(location)) {
                locations = it
                return@forEach
            }
        }
        if (locations == null
            || locationStorage[locations] == null
            || locationStorage[locations]!! <= 0
        ) {
            container.visibility = View.GONE
            callback.onError()
            return
        }

        when (locationStorage[locations]!!) {
            1 -> ProxAds.instance.showBigNativeWithShimmerStyle1(
                activity,
                adId,
                container,
                callback,
                styleButtonAds
            )
            2 -> ProxAds.instance.showBigNativeWithShimmerStyle2(
                activity,
                adId,
                container,
                callback,
                styleButtonAds
            )
            3 -> ProxAds.instance.showBigNativeWithShimmerStyle3(
                activity,
                adId,
                container,
                callback,
                styleButtonAds
            )
            4 -> ProxAds.instance.showBigNativeWithShimmerStyle4(
                activity,
                adId,
                container,
                callback,
                styleButtonAds
            )
            5 -> ProxAds.instance.showBigNativeWithShimmerStyle5(
                activity,
                adId,
                container,
                callback,
                styleButtonAds
            )
            6 -> ProxAds.instance.showBigNativeWithShimmerStyle6(
                activity,
                adId,
                container,
                callback,
                styleButtonAds
            )
            7 -> ProxAds.instance.showBigNativeWithShimmerStyle7(
                activity,
                adId,
                container,
                callback,
                styleButtonAds
            )
            9 -> ProxAds.instance.showBigNativeWithShimmerStyle9(
                activity,
                adId,
                container,
                callback,
                styleButtonAds
            )
            10 -> ProxAds.instance.showBigNativeWithShimmerStyle10(
                activity,
                adId,
                container,
                callback,
                styleButtonAds
            )
            11 -> ProxAds.instance.showBigNativeWithShimmerStyle11(
                activity,
                adId,
                container,
                callback,
                styleButtonAds
            )
            12 -> ProxAds.instance.showBigNativeWithShimmerStyle12(
                activity,
                adId,
                container,
                callback,
                styleButtonAds
            )
            13 -> ProxAds.instance.showBigNativeWithShimmerStyle13(
                activity,
                adId,
                container,
                callback,
                styleButtonAds
            )
            14 -> ProxAds.instance.showBigNativeWithShimmerStyle14(
                activity,
                adId,
                container,
                callback,
                styleButtonAds
            )
            15 -> ProxAds.instance.showSmallNativeWithShimmerStyle15(
                activity,
                adId,
                container,
                callback,
                styleButtonAds
            )
            16 -> ProxAds.instance.showSmallNativeWithShimmerStyle16(
                activity,
                adId,
                container,
                callback,
                styleButtonAds
            )
            19 -> ProxAds.instance.showMediumNativeWithShimmerStyle19(
                activity,
                adId,
                container,
                callback,
                styleButtonAds
            )
            20 -> ProxAds.instance.showMediumNativeWithShimmerStyle20(
                activity,
                adId,
                container,
                callback,
                styleButtonAds
            )
            21 -> ProxAds.instance.showSmallNativeWithShimmerStyle21(
                activity,
                adId,
                container,
                callback,
                styleButtonAds
            )
            22 -> ProxAds.instance.showSmallNativeWithShimmerStyle22(
                activity,
                adId,
                container,
                callback,
                styleButtonAds
            )
            else -> {
                container.visibility = View.GONE
                callback.onError()
            }
        }
    }

    // ------------------------------------------------------------
    val locationStorage: HashMap<Array<String>, Int?> = HashMap()

    companion object {
        private var INSTANCE: ProxAdsConfig? = null

        val instance: ProxAdsConfig
            get() {
                if (INSTANCE == null) {
                    synchronized(ProxAdsConfig::class.java) {
                        if (INSTANCE == null) {
                            INSTANCE = ProxAdsConfig()
                        }
                    }
                }
                return INSTANCE!!
            }
    }
}