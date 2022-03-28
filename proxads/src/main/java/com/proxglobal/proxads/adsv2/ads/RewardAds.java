package com.proxglobal.proxads.adsv2.ads;

import android.app.Activity;

public abstract class RewardAds<T> extends BaseAds<T> {
    protected RewardAds(Activity activity, String adId) {
        super(activity, adId);
    }
}
