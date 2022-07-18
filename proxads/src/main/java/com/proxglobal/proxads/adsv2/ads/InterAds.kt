package com.proxglobal.proxads.adsv2.ads

import android.app.Activity

abstract class InterAds<T> protected constructor(activity: Activity?, adId: String?) :
    BaseAds<T>(activity!!, adId!!)