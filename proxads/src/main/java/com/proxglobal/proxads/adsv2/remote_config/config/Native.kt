package com.proxglobal.proxads.adsv2.remote_config.config

import com.google.gson.annotations.SerializedName

class Native {
    @SerializedName("status")
    var status = false

    @SerializedName("locations")
    var locations: Array<String>? = null

    @SerializedName("style")
    var style: Int? = null
}