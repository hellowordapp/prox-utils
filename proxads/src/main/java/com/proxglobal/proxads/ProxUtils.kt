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
        const val TEST_INTERSTITIAL_MAX_ID = "d39a9f1ed149f427"
        const val TEST_NATIVE_MAX_ID = "aaced834bba86a77"
        const val TEST_BANNER_MAX_ID = "65c30933da2a6760"
    }
}