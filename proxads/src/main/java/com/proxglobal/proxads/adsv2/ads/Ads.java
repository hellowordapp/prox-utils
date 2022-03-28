package com.proxglobal.proxads.adsv2.ads;

import android.app.Activity;

import com.proxglobal.proxads.adsv2.callback.AdsCallback;

abstract class Ads {
    protected final String TAG = "proxads";

    /**
     * load ads
     * @return
     */
    public abstract Ads load();

    /**
     * show available ads
     */
    protected abstract void show(Activity activity);

    /**
     * show available ads with callback
     */
    public abstract void show(Activity activity, AdsCallback callback);

    /**
     * this callback for ads show successful
     */
    public void onShowSuccess() {

    }

    /**
     * this callback for close ads after show
     */
    public void onClosed() {

    }

    /**
     * this callback for ads show error (ex: ads null,..)
     */
    public void onShowError() {

    }

    /**
     * this callback for load ads successful
     */
    public void onLoadSuccess() {

    }


    /**
     * this callback for load ads failed
     */
    public void onLoadFailed() {

    }
}
