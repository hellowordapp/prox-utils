package com.proxglobal.proxads.adsv2.base;

import android.util.Log;

import androidx.annotation.CallSuper;

import com.proxglobal.proxads.adsv2.callback.AdsCallback;
import com.proxglobal.proxads.adsv2.callback.LoadCallback;

public abstract class BaseInterAds extends Ads {
    // Callback
    protected AdsCallback mCallback;
    private LoadCallback mLoadCallback;

    protected boolean autoReload = true;
    protected boolean inLoading = false;

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
        Log.d(TAG, "onClosed: " + autoReload);
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

    public final BaseInterAds setLoadCallback(LoadCallback callback) {
        this.mLoadCallback = callback;
        return this;
    }

    public final BaseInterAds setAdsCallback(AdsCallback callback) {
        this.mCallback = callback;
        return this;
    }

    public final void turnOnAutoReload() {
        this.autoReload = true;
    }

    public final void turnOffAutoReload() {
        this.autoReload = false;
    }

    public abstract boolean isAvailable();
}
