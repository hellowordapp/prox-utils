package com.proxglobal.proxads.adsv2.base;

import android.app.Activity;

public abstract class BaseInterAds<T> extends BaseAds<T>{
    protected BaseInterAds(Activity activity, String adId) {
        super(activity, adId);
    }
}
