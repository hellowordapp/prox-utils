package com.proxglobal.proxads.adsv2.adgoogle

import android.app.Activity
import com.google.android.gms.ads.*
import com.proxglobal.proxads.adsv2.ads.RewardAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.proxglobal.proxads.adsv2.callback.RewardCallback
import com.proxglobal.proxads.adsv2.callback.AdsCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem

class GoogleRewardAds(activity: Activity?, adId: String?) : RewardAds<RewardedAd?>(activity, adId) {
    private var rewardCallback: RewardCallback? = null
    private var mListener: GoogleRewardCallback? = null
        get() {
            if (field == null) {
                field = GoogleRewardCallback()
            }
            return field
        }

    fun show(activity: Activity?, adCallback: AdsCallback?, rewardCallback: RewardCallback?) {
        setAdsCallback(adCallback)
        this.rewardCallback = rewardCallback
        show(activity)
    }

    override fun specificLoadAdsMethod() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(mActivity, adId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdLoaded(rewardedAd: RewardedAd) {
                rewardedAd.fullScreenContentCallback = mListener
                ads = rewardedAd
                onLoadSuccess()
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                onLoadFailed()
            }
        })
    }

    override fun specificShowAdsMethod(activity: Activity?) {
        ads!!.show(activity!!) { rewardItem: RewardItem ->
            rewardCallback?.getReward(
                rewardItem.amount, rewardItem.type
            )
        }
    }

    private inner class GoogleRewardCallback : FullScreenContentCallback() {
        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
            onShowError()
        }

        override fun onAdDismissedFullScreenContent() {
            onClosed()
        }

        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
            onShowSuccess()
        }
    }
}