package com.proxglobal.proxads.adsv2.admax.openads

import android.app.Application

abstract class MaxOpenAdsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MaxOpenAds.instance.init(this, getOpenAdsId())
    }

    protected fun disableOpenAdsAt(vararg clss: Class<*>?) {
        for (cls in clss) {
            MaxOpenAds.instance.registerDisableOpenAdsAt(cls!!)
        }
    }

    abstract fun getOpenAdsId(): String
}