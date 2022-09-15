package com.proxglobal.proxads.adsv2.remote_config.config

import com.google.gson.annotations.SerializedName

class Ads {
    @SerializedName("status")
    var status = false

    @SerializedName("status_open_ads")
    var status_open_ads = false

    @SerializedName("splash")
    var splash: SplashInterstitial? = null

    @SerializedName("banners")
    var banners: Array<Banner>? = null

    @SerializedName("interstitials")
    var interstitials: Array<Interstitial>? = null

    @SerializedName("natives")
    var natives: Array<Native>? = null
}