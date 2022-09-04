package com.proxglobal.proxads.adsv2.adgoogle;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.proxglobal.proxads.R;
import com.proxglobal.proxads.adsv2.ads.NativeAds;

public class GoogleBannerAds extends NativeAds<AdView> {
    public GoogleBannerAds(Activity activity, FrameLayout container, String adId) {
        super(activity, container, adId);
        this.adId = adId;
        this.mContainer = container;

        enableShimmer(R.layout.shimmer_banner);
    }

    @Override
    public void specificLoadAdsMethod() {
        ads = new AdView(mActivity);
        ads.setAdSize(getAdSize());
        ads.setAdUnitId(adId);

        mContainer.addView(ads);
    }

    @Override
    public void specificShowAdsMethod(Activity activity) {
        ads.setAdListener(new AdListener() {
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
                onShowError();
                onLoadFailed();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                onShowSuccess();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        ads.loadAd(adRequest);
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
}
