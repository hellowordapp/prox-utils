package com.proxglobal.proxads.adsv2.ads

import android.app.Activity
import com.proxglobal.proxads.adsv2.callback.AdsCallback

abstract class Ads {
    @JvmField
    protected val TAG = "proxads"

    /**
     * load ads
     * @return
     */
    abstract fun load(): Ads?

    /**
     * show available ads
     */
    protected abstract fun show(activity: Activity?)

    /**
     * show available ads with callback
     */
    abstract fun show(activity: Activity?, callback: AdsCallback?)

    /**
     * this callback for ads show successful
     */
    open fun onShowSuccess() {}

    /**
     * this callback for close ads after show
     */
    open fun onClosed() {}

    /**
     * this callback for ads show error (ex: ads null,..)
     */
    open fun onShowError() {}

    /**
     * this callback for load ads successful
     */
    open fun onLoadSuccess() {}

    /**
     * this callback for load ads failed
     */
    open fun onLoadFailed() {}
}