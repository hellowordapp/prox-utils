package com.proxglobal.proxads.adsv2.adgoogle;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.proxglobal.proxads.adsv2.ads.RewardAds;
import com.proxglobal.proxads.adsv2.callback.AdsCallback;
import com.proxglobal.proxads.adsv2.callback.RewardCallback;

public class GoogleRewardAds extends RewardAds<RewardedAd> {
    private RewardCallback rewardCallback;
    private GoogleRewardCallback mListener;

    private FullScreenContentCallback getMListener() {
        if (mListener == null) {
            mListener = new GoogleRewardCallback();
        }

        return mListener;
    }

    public GoogleRewardAds(Activity activity, String adId) {
        super(activity, adId);
    }

    public void show(Activity activity, AdsCallback adCallback, RewardCallback rewardCallback) {
        setAdsCallback(adCallback);
        this.rewardCallback = rewardCallback;

        show(activity);
    }

    @Override
    public void specificLoadAdsMethod() {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(mActivity, adId, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                rewardedAd.setFullScreenContentCallback(getMListener());

                GoogleRewardAds.this.ads = rewardedAd;
                GoogleRewardAds.this.onLoadSuccess();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                GoogleRewardAds.this.onLoadFailed();
            }
        });
    }

    @Override
    public void specificShowAdsMethod(Activity activity) {
        ads.show(activity, rewardItem -> {
            if (rewardCallback != null) rewardCallback.getReward(rewardItem);
        });
    }

    private class GoogleRewardCallback extends FullScreenContentCallback {
        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            GoogleRewardAds.this.onShowError();
        }

        @Override
        public void onAdDismissedFullScreenContent() {
            GoogleRewardAds.this.onClosed();
        }

        @Override
        public void onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent();
            GoogleRewardAds.this.onShowSuccess();
        }

        @Override
        public void onAdImpression() {
            super.onAdImpression();
        }
    }
}
