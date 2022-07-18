package com.proxglobal.proxads.adsv2.admax;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdRevenueListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdkUtils;
import com.proxglobal.proxads.R;
import com.proxglobal.proxads.adsv2.ads.NativeAds;
import com.proxglobal.proxads.adsv2.ads.ProxAds;

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
        MaxAdView adsTemp = ads;
        ads.setLayoutParams(getAdSize());
        ads.setRevenueListener(new MaxAdRevenueListener() {
            @Override
            public void onAdRevenuePaid(MaxAd ad) {
                Log.d("ntduc", "BannerMax Revenue: "+ad.getRevenue());
                Log.d("ntduc", "BannerMax NetworkName: "+ad.getNetworkName());
                Log.d("ntduc", "BannerMax AdUnitId: "+ad.getAdUnitId());
                Log.d("ntduc", "BannerMax Placement: "+ad.getPlacement());
                Log.d("ntduc", "-------------------------------------------");
            }
        });
        ads.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd ad) {
            }

            @Override
            public void onAdCollapsed(MaxAd ad) {
            }

            @Override
            public void onAdLoaded(MaxAd ad) {
                onLoadSuccess();
                if (ProxAds.isNetworkAvailable(mActivity)){
                    mContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                onShowSuccess();
                if (shimmer != null && !mActivity.isDestroyed()){
                    mContainer.removeView(shimmer);
                    shimmer = null;
                }
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                adsTemp.destroy();
            }

            @Override
            public void onAdClicked(MaxAd ad) {
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                onShowError();
                onLoadFailed();
                if (!ProxAds.isNetworkAvailable(mActivity)){
                    mContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                onShowError();
            }
        });

        ads.loadAd();
    }

    @Override
    public void specificShowAdsMethod(Activity activity) {
        mContainer.addView(ads);
    }

    private ViewGroup.LayoutParams getAdSize() {
        // Set the height of the banner ad based on the device type.
        final boolean isTablet = AppLovinSdkUtils.isTablet(mActivity);
        final int heightPx = AppLovinSdkUtils.dpToPx(mActivity, isTablet ? 90 : 50 );
        // Banner width must match the screen to be fully functional.
        return new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx);
    }
}