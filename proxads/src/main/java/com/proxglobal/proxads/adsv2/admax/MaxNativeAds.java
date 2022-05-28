package com.proxglobal.proxads.adsv2.admax;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAd;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.proxglobal.proxads.R;
import com.proxglobal.proxads.adsv2.ads.NativeAds;

public class MaxNativeAds extends NativeAds<MaxNativeAdView> {
    private int layoutAdId;

    public MaxNativeAds(Activity activity, FrameLayout container, String adId, int layoutAdId) {
        super(activity, container, adId);
        this.layoutAdId = layoutAdId;
    }

    @Override
    public void specificLoadAdsMethod() {
        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(layoutAdId)
                .setTitleTextViewId(R.id.ad_headline)
                .setBodyTextViewId(R.id.ad_body)
                .setAdvertiserTextViewId(R.id.ad_advertiser)
                .setIconImageViewId(R.id.ad_app_icon)
                .setMediaContentViewGroupId(R.id.ad_media)
                .setOptionsContentViewGroupId(R.id.ad_options_view)
                .setCallToActionButtonId(R.id.ad_call_to_action)
                .build();

        ads = new MaxNativeAdView(binder, mActivity);

        MaxNativeAdLoader nativeAdLoader = new MaxNativeAdLoader(adId, mActivity);
        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
            @Override
            public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                Log.d("ntduc", "onNativeAdLoaded");
                onLoadSuccess();
                if (!mActivity.isDestroyed()) {
                    onShowSuccess();

                    populateNativeAdView(ad.getNativeAd(), nativeAdView);
                    mContainer.removeAllViews();
                    mContainer.addView(nativeAdView);
                }
            }

            @Override
            public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                Log.d("ntduc", "onNativeAdLoadFailed");
                onLoadFailed();
                ads = new MaxNativeAdView(binder, mActivity);
                nativeAdLoader.loadAd(ads);
            }

            @Override
            public void onNativeAdClicked(final MaxAd ad) {
                Log.d("ntduc", "onNativeAdClicked");
                ads = new MaxNativeAdView(binder, mActivity);
                nativeAdLoader.loadAd(ads);
            }
        });

        nativeAdLoader.loadAd(ads);
    }

    @Override
    public void specificShowAdsMethod(Activity activity) {

    }

    public void populateNativeAdView(MaxNativeAd nativeAd, MaxNativeAdView adView) {
        // The headline is guaranteed to be in every UnifiedNativeAd.
        try {
            adView.getTitleTextView().setText(nativeAd.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        try {
            if (nativeAd.getBody() == null) {
                adView.getBodyTextView().setVisibility(View.INVISIBLE);
            } else {
                adView.getBodyTextView().setVisibility(View.VISIBLE);
                adView.getBodyTextView().setText(nativeAd.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (nativeAd.getAdvertiser() == null) {
                adView.getAdvertiserTextView().setVisibility(View.INVISIBLE);
            } else {
                adView.getAdvertiserTextView().setVisibility(View.VISIBLE);
                adView.getAdvertiserTextView().setText(nativeAd.getAdvertiser());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (nativeAd.getIcon() == null) {
                adView.getIconImageView().setVisibility(View.GONE);
            } else {
                adView.getIconImageView().setVisibility(View.VISIBLE);
                adView.getIconImageView().setImageURI(nativeAd.getIcon().getUri());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (nativeAd.getMediaView() == null) {
                adView.getMediaContentViewGroup().setVisibility(View.INVISIBLE);
            } else {
                adView.getMediaContentViewGroup().setVisibility(View.VISIBLE);
                adView.getMediaContentViewGroup().removeAllViews();
                adView.getMediaContentViewGroup().addView(nativeAd.getMediaView());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (nativeAd.getOptionsView() == null) {
                adView.getOptionsContentViewGroup().setVisibility(View.INVISIBLE);
            } else {
                adView.getOptionsContentViewGroup().setVisibility(View.VISIBLE);
                adView.getOptionsContentViewGroup().removeAllViews();
                adView.getOptionsContentViewGroup().addView(nativeAd.getOptionsView());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (nativeAd.getCallToAction() == null) {
                adView.getCallToActionButton().setVisibility(View.INVISIBLE);
            } else {
                adView.getCallToActionButton().setVisibility(View.VISIBLE);
                adView.getCallToActionButton().setText(nativeAd.getCallToAction());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
