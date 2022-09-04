package com.proxglobal.proxads.adsv2.ads

import android.app.Activity
import com.proxglobal.proxads.adsv2.ads.BaseAds

abstract class InterAds<T> protected constructor(activity: Activity?, adId: String?) :
    BaseAds<T>(activity!!, adId!!)