package com.proxglobal.proxads.adsv2.base;

import android.app.Activity;

import androidx.annotation.CallSuper;

import com.proxglobal.proxads.adsv2.callback.AdsCallback;
import com.proxglobal.proxads.adsv2.callback.LoadCallback;

public abstract class BaseAds extends Ads {
    // Callback
    protected AdsCallback mCallback;
    private LoadCallback mLoadCallback;

    protected boolean inLoading = false;
    protected boolean autoReload = true;

    protected Activity mActivity;

    protected BaseAds(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    @CallSuper
    public void onShowSuccess() {
        if(mCallback == null) return;
        mCallback.onShow();
    }

    @Override
    @CallSuper
    public void onClosed() {
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

    public final BaseAds setLoadCallback(LoadCallback callback) {
        this.mLoadCallback = callback;
        return this;
    }

    public final BaseAds setAdsCallback(AdsCallback callback) {
        this.mCallback = callback;
        return this;
    }

    public abstract boolean isAvailable();
}
