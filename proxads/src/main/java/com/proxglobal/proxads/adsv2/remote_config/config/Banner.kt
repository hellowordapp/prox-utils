package com.proxglobal.proxads.adsv2.remote_config.config

import com.google.gson.annotations.SerializedName

class Banner {
    @SerializedName("status")
    var status = false

    @SerializedName("id_show_ads")
    var id_show_ads: Array<String>? = null

    @SerializedName("description")
    var description: String? = null
}