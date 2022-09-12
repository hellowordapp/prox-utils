package com.proxglobal.proxads.adsv2.ads

import android.app.Activity
import android.util.Log
import com.proxglobal.proxads.adsv2.callback.AdsCallback
import com.proxglobal.proxads.adsv2.callback.LoadCallback
import androidx.annotation.CallSuper

abstract class BaseAds<T> protected constructor(
    protected var mActivity: Activity,
    protected var adId: String
) : Ads() {
    @JvmField
    protected var ads: T? = null

    // Callback
    private var mCallback: AdsCallback? = null
    private var mLoadCallback: LoadCallback? = null
    private var inLoading = false
    private var autoReload = true
    private var isShowing = false
    override fun load(): BaseAds<T> {
        if (isAvailable || inLoading) return this
        inLoading = true
        Log.i(TAG, "load: ")
        specificLoadAdsMethod()
        return this
    }

    override fun show(activity: Activity?) {
        if (isShowing) return
        if (!isAvailable) {
            if (autoReload) load()
            onShowError()
            return
        }
        specificShowAdsMethod(activity)
        ads = null
    }

    override fun show(activity: Activity?, callback: AdsCallback?) {
        setAdsCallback(callback)
        show(activity)
    }

    @CallSuper
    override fun onShowSuccess() {
        isShowing = true
        if (mCallback == null) return
        mCallback!!.onShow()
    }

    @CallSuper
    override fun onClosed() {
        isShowing = false
        if (mCallback == null) {
            if (autoReload) load()
            return
        }
        mCallback!!.onClosed()
        if (autoReload) load()
    }

    @CallSuper
    override fun onShowError() {
        isShowing = false
        if (mCallback == null) return
        mCallback!!.onError()
    }

    @CallSuper
    override fun onLoadSuccess() {
        inLoading = false
        if (mLoadCallback == null) return
        mLoadCallback!!.onLoadSuccess()
    }

    @CallSuper
    override fun onLoadFailed() {
        inLoading = false
        if (mLoadCallback == null) return
        mLoadCallback!!.onLoadFailed()
    }

    fun setLoadCallback(callback: LoadCallback?): BaseAds<T> {
        mLoadCallback = callback
        return this
    }

    fun setAdsCallback(callback: AdsCallback?): BaseAds<T> {
        mCallback = callback
        return this
    }

    fun clearAllCallback() {
        mCallback = null
        mLoadCallback = null
    }

    fun turnOnAutoReload() {
        autoReload = true
    }

    fun turnOffAutoReload() {
        autoReload = false
    }

    val isAvailable: Boolean
        get() = ads != null

    abstract fun specificLoadAdsMethod()
    abstract fun specificShowAdsMethod(activity: Activity?)
}