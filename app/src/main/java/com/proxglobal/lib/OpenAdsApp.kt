package com.proxglobal.lib

import android.util.Log
import com.proxglobal.proxads.ads.openads.ProxOpenAdsApplication

class OpenAdsApp: ProxOpenAdsApplication() {
    override fun onCreate() {
        super.onCreate()
        Log.d("vanh", "onCreate: init openads")

        disableOpenAdsAt(MainActivity::class.java)
    }
    override fun getOpenAdsId(): String = "ca-app-pub-3940256099942544/3419835294"


}