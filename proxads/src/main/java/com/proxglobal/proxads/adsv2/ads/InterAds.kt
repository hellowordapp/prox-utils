package com.proxglobal.proxads.adsv2.ads;

import android.app.Activity;

public abstract class InterAds<T> extends BaseAds<T> {
    protected InterAds(Activity activity, String adId) {
        super(activity, adId);
    }
}
