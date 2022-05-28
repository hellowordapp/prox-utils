package com.proxglobal.proxads.adsv2.admax;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdkUtils;
import com.proxglobal.proxads.R;
import com.proxglobal.proxads.adsv2.ads.NativeAds;

public class MaxBannerAds extends NativeAds<MaxAdView> {
    public MaxBannerAds(Activity activity, FrameLayout container, String adId) {
        super(activity, container, adId);
        this.adId = adId;
        this.mContainer = container;

        enableShimmer(R.layout.shimmer_banner_max);
    }

    @Override
    public void specificLoadAdsMethod() {
        ads = new MaxAdView(adId, mActivity);

        ads.setLayoutParams(getAdSize());
        mContainer.addView(ads);
    }

    @Override
    public void specificShowAdsMethod(Activity activity) {
        ads.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd ad) {
                Log.d("ntduc", "onAdExpanded");

            }

            @Override
            public void onAdCollapsed(MaxAd ad) {
                Log.d("ntduc", "onAdCollapsed");
            }

            @Override
            public void onAdLoaded(MaxAd ad) {
                Log.d("ntduc", "onAdLoaded");
                onLoadSuccess();
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                Log.d("ntduc", "onAdDisplayed");
                if (shimmer != null){
                    mContainer.removeView(shimmer);
                }
                onShowSuccess();
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                Log.d("ntduc", "onAdHidden");

            }

            @Override
            public void onAdClicked(MaxAd ad) {
                Log.d("ntduc", "onAdClicked");
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                Log.d("ntduc", "onAdLoadFailed");
                onShowError();
                onLoadFailed();
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                Log.d("ntduc", "onAdDisplayFailed");
                onShowError();
            }
        });

        ads.loadAd();
    }

    private ViewGroup.LayoutParams getAdSize() {
        // Set the height of the banner ad based on the device type.
        final boolean isTablet = AppLovinSdkUtils.isTablet(mActivity);
        final int heightPx = AppLovinSdkUtils.dpToPx(mActivity, isTablet ? 90 : 50 );
        // Banner width must match the screen to be fully functional.
        return new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx);
    }
}
