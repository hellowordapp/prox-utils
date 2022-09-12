package com.proxglobal.lib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.proxglobal.lib.databinding.ActivityMain8Binding
import com.proxglobal.proxads.ProxUtils
import com.proxglobal.proxads.adsv2.callback.AdsCallback
import com.proxglobal.proxads.adsv2.remote_config.ProxAdsConfig

class Click2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityMain8Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain8Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ProxAdsConfig.instance.showBannerIfNecessary(
            this,
            binding.bannerContainer,
            "banner_click_2",
            ProxUtils.TEST_BANNER_ID,
            object : AdsCallback() {}
        )

        ProxAdsConfig.instance.showNativeIfNecessary(
            this,
            binding.adContainer,
            "native_click_2",
            ProxUtils.TEST_NATIVE_ID,
            object : AdsCallback() {}
        )
    }
}