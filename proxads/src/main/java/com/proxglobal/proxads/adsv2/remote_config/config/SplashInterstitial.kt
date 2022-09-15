package com.proxglobal.proxads.adsv2.remote_config.config

import com.google.gson.annotations.SerializedName

class SplashInterstitial {
    @SerializedName("status")
    var status = false

    @SerializedName("timeout")
    var timeout: Int? = null

    @SerializedName("description")
    var description: String? = null
}