package com.proxglobal.proxads.adsv2.admax;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.view.ContextThemeWrapper;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdRevenueListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAd;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
import com.proxglobal.proxads.R;
import com.proxglobal.proxads.adsv2.ads.NativeAds;

public class MaxNativeAds extends NativeAds<MaxNativeAdView> {
    private int layoutAdId;
    private int styleBtnAds;
    private boolean isCustomStyle;

    public MaxNativeAds(Activity activity, FrameLayout container, String adId, int layoutAdId) {
        super(activity, container, adId);
        this.layoutAdId = layoutAdId;
        isCustomStyle = false;
    }

    public MaxNativeAds(Activity activity, FrameLayout container, String adId, int layoutAdId, int styleBtnAds) {
        super(activity, container, adId);
        this.layoutAdId = layoutAdId;
        this.styleBtnAds = styleBtnAds;
        isCustomStyle = true;
    }

    @Override
    public void specificLoadAdsMethod() {
        View layoutAd = LayoutInflater.from(mActivity).inflate(layoutAdId, mContainer, false);
        Button adCallToAction;

        try {
            if (isCustomStyle) {
                layoutAd.findViewById(R.id.ad_call_to_action).setVisibility(View.GONE);
                adCallToAction = new Button(new ContextThemeWrapper(mActivity, styleBtnAds), null, styleBtnAds);
                adCallToAction.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                adCallToAction.setId(View.generateViewId());
                ((LinearLayout) layoutAd.findViewById(R.id.linear)).addView(adCallToAction);
            } else {
                layoutAd.findViewById(R.id.ad_call_to_action).setVisibility(View.VISIBLE);
                adCallToAction = layoutAd.findViewById(R.id.ad_call_to_action);
            }
        } catch (Exception e) {
            layoutAd.findViewById(R.id.ad_call_to_action).setVisibility(View.VISIBLE);
            adCallToAction = layoutAd.findViewById(R.id.ad_call_to_action);
        }

        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(layoutAd)
                .setTitleTextViewId(R.id.ad_headline)
                .setBodyTextViewId(R.id.ad_body)
                .setAdvertiserTextViewId(R.id.ad_advertiser)
                .setIconImageViewId(R.id.ad_app_icon)
                .setMediaContentViewGroupId(R.id.ad_media)
                .setOptionsContentViewGroupId(R.id.ad_options_view)
                .setCallToActionButtonId(adCallToAction.getId())
                .build();

        ads = new MaxNativeAdView(binder, mActivity);

        MaxNativeAdLoader nativeAdLoader = new MaxNativeAdLoader(adId, mActivity);
        nativeAdLoader.setRevenueListener(new MaxAdRevenueListener() {
            @Override
            public void onAdRevenuePaid(MaxAd ad) {
                Log.d("ntduc", "NativeMax Revenue: "+ad.getRevenue());
                Log.d("ntduc", "NativeMax NetworkName: "+ad.getNetworkName());
                Log.d("ntduc", "NativeMax AdUnitId: "+ad.getAdUnitId());
                Log.d("ntduc", "NativeMax Placement: "+ad.getPlacement());
                Log.d("ntduc", "-------------------------------------------");
            }
        });
        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
            @Override
            public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                onLoadSuccess();
                if (mActivity.isDestroyed()) {
                    nativeAdLoader.destroy();
                } else {
                    onShowSuccess();

                    populateNativeAdView(ad.getNativeAd(), nativeAdView);
                    mContainer.removeAllViews();
                    mContainer.addView(nativeAdView);
                }
            }

            @Override
            public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                onLoadFailed();
                if (mActivity.isDestroyed()) {
                    nativeAdLoader.destroy();
                } else {
                    ads = new MaxNativeAdView(binder, mActivity);
                    nativeAdLoader.loadAd(ads);
                }
            }

            @Override
            public void onNativeAdClicked(final MaxAd ad) {
                if (mActivity.isDestroyed()) {
                    nativeAdLoader.destroy();
                } else {
                    ads = new MaxNativeAdView(binder, mActivity);
                    nativeAdLoader.loadAd(ads);
                }
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
