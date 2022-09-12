package com.proxglobal.proxads.adsv2.remote_config.config

import com.google.gson.annotations.SerializedName

class Banner {
    @SerializedName("status")
    var status = false

    @SerializedName("locations")
    var locations: Array<String>? = null
}