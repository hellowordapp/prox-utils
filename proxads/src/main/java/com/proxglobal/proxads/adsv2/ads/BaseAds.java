package com.proxglobal.proxads.adsv2.ads;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.CallSuper;

import com.proxglobal.proxads.adsv2.callback.AdsCallback;
import com.proxglobal.proxads.adsv2.callback.LoadCallback;

abstract class BaseAds<T> extends Ads {
    protected T ads;
    // Callback
    private AdsCallback mCallback;
    private LoadCallback mLoadCallback;

    private boolean inLoading = false;
    private boolean autoReload = true;
    private boolean isShowing = false;

    protected Activity mActivity;

    protected String adId;

    protected BaseAds(Activity activity, String adId) {
        this.mActivity = activity;
        this.adId = adId;
    }

    @Override
    public BaseAds<T> load() {
        if (isAvailable() || inLoading) return this;
        inLoading = true;

        Log.i(TAG, "load: ");
        specificLoadAdsMethod();

        return this;
    }

    @Override
    protected void show(Activity activity) {
        if(isShowing) return;

        if(!isAvailable()) {
            if(autoReload) load();
            onShowError();
            return;
        }

        specificShowAdsMethod(activity);
        ads = null;
    }

    @Override
    public void show(Activity activity, AdsCallback callback) {
        setAdsCallback(callback);
        show(activity);
    }

    @Override
    @CallSuper
    public void onShowSuccess() {
        isShowing = true;
        if(mCallback == null) return;
        mCallback.onShow();
    }

    @Override
    @CallSuper
    public void onClosed() {
        isShowing = false;
        if(mCallback == null) {
            if(autoReload) load();
            return;
        }

        mCallback.onClosed();
        if(autoReload) load();
    }

    @Override
    @CallSuper
    public void onShowError() {
        isShowing = false;
        if(mCallback == null) return;
        mCallback.onError();
    }

    @Override
    @CallSuper
    public void onLoadSuccess() {
        inLoading = false;

        if(mLoadCallback == null) return;
        mLoadCallback.onLoadSuccess();
    }

    @Override
    @CallSuper
    public void onLoadFailed() {
        inLoading = false;

        if(mLoadCallback == null) return;
        mLoadCallback.onLoadFailed();
    }

    public final BaseAds<T> setLoadCallback(LoadCallback callback) {
        this.mLoadCallback = callback;
        return this;
    }

    public final BaseAds<T> setAdsCallback(AdsCallback callback) {
        this.mCallback = callback;
        return this;
    }

    public void clearAllCallback() {
        this.mCallback = null;
        this.mLoadCallback = null;
    }

    public final void turnOnAutoReload() {
        this.autoReload = true;
    }

    public final void turnOffAutoReload() {
        this.autoReload = false;
    }

    public final boolean isAvailable() {
        return (ads != null);
    }

    public abstract void specificLoadAdsMethod();
    public abstract void specificShowAdsMethod(Activity activity);
}
