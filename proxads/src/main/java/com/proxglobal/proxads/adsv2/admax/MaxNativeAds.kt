package com.proxglobal.proxads.adsv2.admax

import com.proxglobal.proxads.adsv2.ads.NativeAds
import android.app.Activity
import android.util.Log
import android.widget.FrameLayout
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.view.ContextThemeWrapper
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.*
import com.proxglobal.proxads.R
import java.lang.Exception

class MaxNativeAds : NativeAds<MaxNativeAdView?> {
    private var layoutAdId: Int
    private var styleBtnAds = 0
    private var isCustomStyle: Boolean

    constructor(
        activity: Activity?,
        container: FrameLayout?,
        adId: String?,
        layoutAdId: Int
    ) : super(activity, container, adId) {
        this.layoutAdId = layoutAdId
        isCustomStyle = false
    }

    constructor(
        activity: Activity?,
        container: FrameLayout?,
        adId: String?,
        layoutAdId: Int,
        styleBtnAds: Int
    ) : super(activity, container, adId) {
        this.layoutAdId = layoutAdId
        this.styleBtnAds = styleBtnAds
        isCustomStyle = true
    }

    override fun specificLoadAdsMethod() {
        val layoutAd = LayoutInflater.from(mActivity).inflate(layoutAdId, mContainer, false)
        var adCallToAction: Button
        try {
            if (isCustomStyle) {
                layoutAd.findViewById<View>(R.id.ad_call_to_action).visibility = View.GONE
                adCallToAction =
                    Button(ContextThemeWrapper(mActivity, styleBtnAds), null, styleBtnAds)
                adCallToAction.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                adCallToAction.id = View.generateViewId()
                (layoutAd.findViewById<View>(R.id.linear) as LinearLayout).addView(adCallToAction)
            } else {
                layoutAd.findViewById<View>(R.id.ad_call_to_action).visibility = View.VISIBLE
                adCallToAction = layoutAd.findViewById(R.id.ad_call_to_action)
            }
        } catch (e: Exception) {
            layoutAd.findViewById<View>(R.id.ad_call_to_action).visibility = View.VISIBLE
            adCallToAction = layoutAd.findViewById(R.id.ad_call_to_action)
        }
        val binder = MaxNativeAdViewBinder.Builder(layoutAd)
            .setTitleTextViewId(R.id.ad_headline)
            .setBodyTextViewId(R.id.ad_body)
            .setAdvertiserTextViewId(R.id.ad_advertiser)
            .setIconImageViewId(R.id.ad_app_icon)
            .setMediaContentViewGroupId(R.id.ad_media)
            .setOptionsContentViewGroupId(R.id.ad_options_view)
            .setCallToActionButtonId(adCallToAction.id)
            .build()
        ads = MaxNativeAdView(binder, mActivity)
        val nativeAdLoader = MaxNativeAdLoader(adId, mActivity)
        nativeAdLoader.setRevenueListener { ad ->
            Log.d("ntduc", "NativeMax Revenue: " + ad.revenue)
            Log.d("ntduc", "NativeMax NetworkName: " + ad.networkName)
            Log.d("ntduc", "NativeMax AdUnitId: " + ad.adUnitId)
            Log.d("ntduc", "NativeMax Placement: " + ad.placement)
            Log.d("ntduc", "-------------------------------------------")
        }
        nativeAdLoader.setNativeAdListener(object : MaxNativeAdListener() {
            override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd) {
                onLoadSuccess()
                if (mActivity.isDestroyed) {
                    nativeAdLoader.destroy()
                } else {
                    onShowSuccess()
                    populateNativeAdView(ad.nativeAd, nativeAdView)
                    mContainer!!.removeAllViews()
                    mContainer!!.addView(nativeAdView)
                }
            }

            override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
                onLoadFailed()
                if (mActivity.isDestroyed) {
                    nativeAdLoader.destroy()
                } else {
                    ads = MaxNativeAdView(binder, mActivity)
                    nativeAdLoader.loadAd(ads)
                }
            }

            override fun onNativeAdClicked(ad: MaxAd) {
                if (mActivity.isDestroyed) {
                    nativeAdLoader.destroy()
                } else {
                    ads = MaxNativeAdView(binder, mActivity)
                    nativeAdLoader.loadAd(ads)
                }
            }
        })
        nativeAdLoader.loadAd(ads)
    }

    override fun specificShowAdsMethod(activity: Activity?) {}
    fun populateNativeAdView(nativeAd: MaxNativeAd?, adView: MaxNativeAdView?) {
        // The headline is guaranteed to be in every UnifiedNativeAd.
        try {
            adView!!.titleTextView.text = nativeAd!!.title
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        try {
            if (nativeAd!!.body == null) {
                adView!!.bodyTextView.visibility = View.INVISIBLE
            } else {
                adView!!.bodyTextView.visibility = View.VISIBLE
                adView.bodyTextView.text = nativeAd.body
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            if (nativeAd!!.advertiser == null) {
                adView!!.advertiserTextView.visibility = View.INVISIBLE
            } else {
                adView!!.advertiserTextView.visibility = View.VISIBLE
                adView.advertiserTextView.text = nativeAd.advertiser
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            if (nativeAd!!.icon == null) {
                adView!!.iconImageView.visibility = View.GONE
            } else {
                adView!!.iconImageView.visibility = View.VISIBLE
                adView.iconImageView.setImageURI(nativeAd.icon.uri)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            if (nativeAd!!.mediaView == null) {
                adView!!.mediaContentViewGroup.visibility = View.INVISIBLE
            } else {
                adView!!.mediaContentViewGroup.visibility = View.VISIBLE
                adView.mediaContentViewGroup.removeAllViews()
                adView.mediaContentViewGroup.addView(nativeAd.mediaView)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            if (nativeAd!!.optionsView == null) {
                adView!!.optionsContentViewGroup.visibility = View.INVISIBLE
            } else {
                adView!!.optionsContentViewGroup.visibility = View.VISIBLE
                adView.optionsContentViewGroup.removeAllViews()
                adView.optionsContentViewGroup.addView(nativeAd.optionsView)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            if (nativeAd!!.callToAction == null) {
                adView!!.callToActionButton.visibility = View.INVISIBLE
            } else {
                adView!!.callToActionButton.visibility = View.VISIBLE
                adView.callToActionButton.text = nativeAd.callToAction
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}