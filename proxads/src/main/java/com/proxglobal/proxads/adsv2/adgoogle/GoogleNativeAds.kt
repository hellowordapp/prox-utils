package com.proxglobal.proxads.adsv2.adgoogle

import android.annotation.SuppressLint
import com.proxglobal.proxads.adsv2.ads.NativeAds
import com.google.android.gms.ads.nativead.NativeAdView
import android.app.Activity
import android.view.View
import android.widget.*
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.gms.ads.*
import com.proxglobal.proxads.R
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import java.lang.Exception

class GoogleNativeAds : NativeAds<NativeAdView?> {

    companion object {
        @JvmField
        var isOpenAds = false
    }

    private var layoutAdId: Int
    private var styleBtnAds = 0
    private var isCustomStyle: Boolean

    constructor(
        activity: Activity?,
        container: FrameLayout?,
        adId: String?,
        layoutAdId: Int
    ) : super(activity, container!!, adId) {
        this.layoutAdId = layoutAdId
        isCustomStyle = false
    }

    constructor(
        activity: Activity?,
        container: FrameLayout?,
        adId: String?,
        layoutAdId: Int,
        styleBtnAds: Int
    ) : super(activity, container!!, adId) {
        this.layoutAdId = layoutAdId
        this.styleBtnAds = styleBtnAds
        isCustomStyle = true
    }

    override fun specificLoadAdsMethod() {
        ads = mActivity.layoutInflater.inflate(layoutAdId, null) as NativeAdView
        val builder = AdLoader.Builder(mActivity, adId).forNativeAd { nativeAd: NativeAd ->
            if (mActivity.isDestroyed) {
                if (ads != null) {
                    ads!!.destroy()
                }
                nativeAd.destroy()
            } else {
                onShowSuccess()
                if (ads == null) ads =
                    mActivity.layoutInflater.inflate(layoutAdId, null) as NativeAdView
                populateNativeAdView(nativeAd, ads!!)
                mContainer.removeAllViews()
                mContainer.addView(ads)
            }
        }
        val videoOptions = VideoOptions.Builder().setStartMuted(true).build()
        val adOptions = NativeAdOptions.Builder().setVideoOptions(videoOptions).build()
        builder.withNativeAdOptions(adOptions)
        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdOpened() {
                super.onAdOpened()
                isOpenAds = true
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                onLoadFailed()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                onLoadSuccess()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                onClosed()
            }

            override fun onAdImpression() {
                super.onAdImpression()
                onShowSuccess()
            }
        }).build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    override fun specificShowAdsMethod(activity: Activity?) {}
    @SuppressLint("CutPasteId")
    fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        val mediaView = adView.findViewById<MediaView>(R.id.ad_media)
        adView.mediaView = mediaView

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        var adCallToAction: Button
        try {
            if (isCustomStyle) {
                adView.findViewById<View>(R.id.ad_call_to_action).visibility = View.GONE
                adCallToAction =
                    Button(ContextThemeWrapper(mActivity, styleBtnAds), null, styleBtnAds)
                adCallToAction.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                (adView.findViewById<View>(R.id.linear) as LinearLayout).addView(adCallToAction)
            } else {
                adView.findViewById<View>(R.id.ad_call_to_action).visibility = View.VISIBLE
                adCallToAction = adView.findViewById(R.id.ad_call_to_action)
            }
        } catch (e: Exception) {
            adView.findViewById<View>(R.id.ad_call_to_action).visibility = View.VISIBLE
            adCallToAction = adView.findViewById(R.id.ad_call_to_action)
        }
        adView.callToActionView = adCallToAction
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)

        // The headline is guaranteed to be in every UnifiedNativeAd.
        try {
            (adView.headlineView as TextView?)!!.text = nativeAd.headline
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        try {
            if (nativeAd.body == null) {
                adView.bodyView!!.visibility = View.INVISIBLE
            } else {
                adView.bodyView!!.visibility = View.VISIBLE
                (adView.bodyView as TextView?)!!.text = nativeAd.body
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            if (nativeAd.callToAction == null) {
                adView.callToActionView!!.visibility = View.INVISIBLE
            } else {
                adView.callToActionView!!.visibility = View.VISIBLE
                adCallToAction.text = nativeAd.callToAction
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            if (nativeAd.icon == null) {
                adView.iconView!!.visibility = View.GONE
            } else {
                (adView.iconView as ImageView?)!!.setImageDrawable(
                    nativeAd.icon!!.drawable
                )
                adView.iconView!!.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            if (nativeAd.price == null) {
                adView.priceView!!.visibility = View.INVISIBLE
            } else {
                adView.priceView!!.visibility = View.VISIBLE
                (adView.priceView as TextView?)!!.text = nativeAd.price
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            if (nativeAd.store == null) {
                adView.storeView!!.visibility = View.INVISIBLE
                adView.findViewById<View>(R.id.ic_store2).visibility = View.GONE
            } else {
                adView.storeView!!.visibility = View.VISIBLE
                (adView.storeView as TextView?)!!.text = nativeAd.store
                if (nativeAd.store.equals("Google Play", ignoreCase = true)) {
                    adView.findViewById<View>(R.id.ic_store2).visibility = View.VISIBLE
                } else {
                    adView.findViewById<View>(R.id.ic_store2).visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            if (nativeAd.starRating == null) {
                adView.starRatingView!!.visibility = View.INVISIBLE
            } else {
                (adView.starRatingView as RatingBar?)?.rating = nativeAd.starRating!!.toFloat()
                adView.starRatingView!!.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            if (nativeAd.advertiser == null) {
                adView.advertiserView!!.visibility = View.INVISIBLE
            } else {
                (adView.advertiserView as TextView?)!!.text = nativeAd.advertiser
                adView.advertiserView!!.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd)
    }
}