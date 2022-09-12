package com.proxglobal.lib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.proxglobal.lib.databinding.ActivityMain9Binding
import com.proxglobal.proxads.ProxUtils
import com.proxglobal.proxads.adsv2.callback.AdsCallback
import com.proxglobal.proxads.adsv2.remote_config.ProxAdsConfig

class Click3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityMain9Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain9Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ProxAdsConfig.instance.showBannerIfNecessary(
            this,
            binding.bannerContainer,
            "banner_click_3",
            ProxUtils.TEST_BANNER_ID,
            object : AdsCallback() {}
        )

        ProxAdsConfig.instance.showNativeIfNecessary(
            this,
            binding.adContainer,
            "native_click_3",
            ProxUtils.TEST_NATIVE_ID,
            object : AdsCallback() {}
        )
    }
}