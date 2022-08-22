package com.proxglobal.proxads.adsv2.admax.openads

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.proxglobal.proxads.R
import com.proxglobal.proxads.adsv2.admax.MaxInterstitialAds
import com.proxglobal.proxads.adsv2.admax.MaxNativeAds
import com.proxglobal.proxads.adsv2.ads.ProxAds.Companion.isNetworkAvailable
import com.proxglobal.purchase.ProxPurchase
import com.wang.avi.AVLoadingIndicatorView

class MaxOpenAds private constructor() : LifecycleObserver, ActivityLifecycleCallbacks {
    private var ads: MaxInterstitialAd? = null
    private var openAdsDialog: OpenAdsDialog? = null
    private var currentActivity: Activity? = null
    private var adId: String? = null
    private var isShowingAd = false
    private var isLoadingAd = false
    private var disableOpenAdsList: MutableList<Class<*>>? = null
    private var openAdsEnable = true
    fun init(myApplication: Application, adId: String?) {
        this.adId = adId
        myApplication.registerActivityLifecycleCallbacks(this)
        disableOpenAdsList = ArrayList()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    /**
     * LifecycleObserver methods
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        if (MaxNativeAds.isOpenAds) {
            MaxNativeAds.isOpenAds = false
            return
        }

        if (MaxInterstitialAds.isShowing || ProxPurchase.getInstance()
                .checkPurchased() || !isNetworkAvailable(
                currentActivity!!
            )
        ) {
            return
        }
        show()
    }

    private fun show() {
        if (inDisableOpenAdsList() || !openAdsEnable || isLoadingAd || isShowingAd) return
        if (ads != null && ads!!.isReady) {
            Log.d(LOG_TAG, "Will show ad.")
            if (openAdsDialog == null) {
                openAdsDialog = OpenAdsDialog(currentActivity!!)
            }
            if (currentActivity!!.isFinishing) {
                return
            }
            openAdsDialog!!.show()
            Handler(Looper.getMainLooper()).postDelayed({
                if (!currentActivity!!.isFinishing) {
                    ads!!.showAd()
                } else {
                    openAdsDialog!!.dismiss()
                }

            }, 1500)
        } else {
            Log.d(LOG_TAG, "Can not show ad.")
            load()
        }
    }

    private fun load() {
        ads = MaxInterstitialAd(adId, currentActivity)
        ads!!.setRevenueListener { ad ->
            Log.d("ntduc", "OpenAdsMax Revenue: " + ad.revenue)
            Log.d("ntduc", "OpenAdsMax NetworkName: " + ad.networkName)
            Log.d("ntduc", "OpenAdsMax AdUnitId: " + ad.adUnitId)
            Log.d("ntduc", "OpenAdsMax Placement: " + ad.placement)
            Log.d("ntduc", "-------------------------------------------")
        }
        ads!!.setListener(object : MaxAdListener {
            override fun onAdLoaded(ad: MaxAd) {
                Log.d(LOG_TAG, "onAdLoaded")
                isLoadingAd = false
            }

            override fun onAdDisplayed(ad: MaxAd) {
                Log.d(LOG_TAG, "onAdDisplayed")
                isShowingAd = true
                if (openAdsDialog != null) {
                    openAdsDialog!!.dismiss()
                    openAdsDialog = null
                }
            }

            override fun onAdHidden(ad: MaxAd) {
                Log.d(LOG_TAG, "onAdHidden")
                isShowingAd = false
                if (ads == null) {
                    ads = MaxInterstitialAd(adId, currentActivity)
                }
                ads!!.loadAd()
                isLoadingAd = true
            }

            override fun onAdClicked(ad: MaxAd) {}
            override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
                Log.d(LOG_TAG, "onAdLoadFailed")
                isLoadingAd = false
                isShowingAd = false
                if (ads == null) {
                    ads = MaxInterstitialAd(adId, currentActivity)
                }
                ads!!.loadAd()
                isLoadingAd = true
            }

            override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
                Log.d(LOG_TAG, "onAdDisplayFailed")
                if (openAdsDialog != null) {
                    openAdsDialog!!.dismiss()
                    openAdsDialog = null
                }
                isShowingAd = false
                if (ads == null) {
                    ads = MaxInterstitialAd(adId, currentActivity)
                }
                ads!!.loadAd()
                isLoadingAd = true
            }
        })
        ads!!.loadAd()
        isLoadingAd = true
    }

    fun registerDisableOpenAdsAt(cls: Class<*>) {
        disableOpenAdsList!!.add(cls)
    }

    fun removeDisableOpenAdsAt(cls: Class<*>) {
        disableOpenAdsList!!.remove(cls)
    }

    private fun inDisableOpenAdsList(): Boolean {
        return disableOpenAdsList!!.contains(currentActivity!!.javaClass)
    }

    fun disableOpenAds() {
        openAdsEnable = false
    }

    fun enableOpenAds() {
        openAdsEnable = true
    }


    // Activity lifecycle
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }

    internal class OpenAdsDialog(context: Context) : Dialog(context) {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_open_ads)
            window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            setCancelable(false)
            val loading = findViewById<AVLoadingIndicatorView>(R.id.loading)
            loading.smoothToShow()
        }
    }

    companion object {
        private const val LOG_TAG = "MaxOpenAds"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: MaxOpenAds? = null

        @JvmStatic
        val instance: MaxOpenAds
            get() {
                synchronized(MaxOpenAds::class.java) {
                    if (INSTANCE == null) {
                        synchronized(MaxOpenAds::class.java) { INSTANCE = MaxOpenAds() }
                    }
                }
                return INSTANCE!!
            }
    }
}