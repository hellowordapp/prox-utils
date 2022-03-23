package com.proxglobal.proxads.adsv2.adgoogle;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.proxglobal.proxads.R;
import com.proxglobal.proxads.adsv2.base.BaseAds;
import com.proxglobal.proxads.adsv2.callback.AdsCallback;

public class GoogleBannerAds extends BaseAds {
    private AdView mAdView;
    private String adId;
    private FrameLayout mContainer;

    public GoogleBannerAds(Activity activity, FrameLayout container, String adId) {
        super(activity);
        this.adId = adId;
        this.mContainer = container;

        enableShimmer(R.layout.shimmer_banner);
    }

    @Override
    public GoogleBannerAds load() {
        mAdView = new AdView(mActivity);
        mAdView.setAdSize(getAdSize());
        mAdView.setAdUnitId(adId);

        mContainer.addView(mAdView);

        return this;
    }

    @Override
    public void show(Activity activity) {
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                onLoadSuccess();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                onClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                onLoadFailed();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                onShowSuccess();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void show(Activity activity, AdsCallback callback) {
        setAdsCallback(callback);

        show(activity);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mActivity, adWidth);
    }

    public void enableShimmer(int shimmerLayoutId) {
        View view = mActivity.getLayoutInflater().inflate(shimmerLayoutId, mContainer);

        ShimmerFrameLayout shimmer;
        if(view instanceof ShimmerFrameLayout) {
            shimmer = (ShimmerFrameLayout) view;
        } else {
            shimmer = new ShimmerFrameLayout(mActivity);
            shimmer.setId(View.generateViewId());
            shimmer.setLayoutParams(
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT));

            mActivity.getLayoutInflater().inflate(shimmerLayoutId, shimmer);
        }

        shimmer.startShimmer();

        mContainer.removeAllViews();
        mContainer.addView(shimmer);
    }
}
