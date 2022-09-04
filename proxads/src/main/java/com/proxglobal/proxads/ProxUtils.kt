package com.proxglobal.proxads

import androidx.appcompat.app.AppCompatActivity
import com.proxglobal.proxads.remote_config.ProxRemoteConfig

class ProxUtils private constructor() {
    fun initFirebaseRemoteConfig(
        activity: AppCompatActivity?, appVersionCode: Int, isDebug: Boolean,
        iconAppId: Int, appName: String?
    ) {
        ProxRemoteConfig(iconAppId, appName).showRemoteConfigIfNecessary(
            activity,
            appVersionCode,
            isDebug
        )
    }

    companion object {
        var INSTANCE = ProxUtils()
        var TAG = "ProxUtils"
        const val TEST_OPEN_APP_ID = "ca-app-pub-3940256099942544/3419835294"
        const val TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"
        const val TEST_NATIVE_ID = "ca-app-pub-3940256099942544/2247696110"
        const val TEST_BANNER_ID = "ca-app-pub-3940256099942544/6300978111"
        const val TEST_REWARD_ID = "ca-app-pub-3940256099942544/5224354917"
    }
}