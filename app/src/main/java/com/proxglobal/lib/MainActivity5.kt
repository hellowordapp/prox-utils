package com.proxglobal.lib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.proxglobal.lib.databinding.ActivityMain5Binding
import com.proxglobal.proxads.ProxUtils
import com.proxglobal.proxads.adsv2.ads.ProxAds
import com.proxglobal.proxads.adsv2.callback.AdsCallback
import com.proxglobal.proxads.adsv2.remote_config.ProxAdsConfig

class MainActivity5 : AppCompatActivity() {
    private lateinit var binding: ActivityMain5Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ProxAds.instance.initInterstitial(this, ProxUtils.TEST_INTERSTITIAL_ID, null, "inter_1")
        ProxAds.instance.initInterstitial(this, ProxUtils.TEST_INTERSTITIAL_ID, null, "inter_2")
        ProxAdsConfig.instance.init(this)

        binding.testSplash.setOnClickListener {
            ProxAdsConfig.instance.showSplashIfNecessary(
                this,
                ProxUtils.TEST_INTERSTITIAL_ID,
                object : AdsCallback() {
                    override fun onClosed() {
                        super.onClosed()
                        Toast.makeText(this@MainActivity5, "Closed", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MainActivity5, ClickSplashActivity::class.java))
                    }

                    override fun onError() {
                        super.onError()
                        Toast.makeText(this@MainActivity5, "Error", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MainActivity5, ClickSplashActivity::class.java))
                    }
                })
        }

        binding.testInterstitialClick1.setOnClickListener {
            ProxAdsConfig.instance.showInterstitialIfNecessary(
                this,
                "inter_click_1",
                "inter_1",
                object : AdsCallback() {
                    override fun onClosed() {
                        super.onClosed()
                        Toast.makeText(this@MainActivity5, "Closed", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MainActivity5, Click1Activity::class.java))
                    }

                    override fun onError() {
                        super.onError()
                        Toast.makeText(this@MainActivity5, "Error", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MainActivity5, Click1Activity::class.java))
                    }
                })
        }

        binding.testInterstitialClick2.setOnClickListener {
            ProxAdsConfig.instance.showInterstitialIfNecessary(
                this,
                "inter_click_2",
                "inter_1",
                object : AdsCallback() {
                    override fun onClosed() {
                        super.onClosed()
                        Toast.makeText(this@MainActivity5, "Closed", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MainActivity5, Click2Activity::class.java))
                    }

                    override fun onError() {
                        super.onError()
                        Toast.makeText(this@MainActivity5, "Error", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MainActivity5, Click2Activity::class.java))
                    }
                })
        }

        binding.testInterstitialClick3.setOnClickListener {
            ProxAdsConfig.instance.showInterstitialIfNecessary(
                this,
                "inter_click_3",
                "inter_2",
                object : AdsCallback() {
                    override fun onClosed() {
                        super.onClosed()
                        Toast.makeText(this@MainActivity5, "Closed", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MainActivity5, Click3Activity::class.java))
                    }

                    override fun onError() {
                        super.onError()
                        Toast.makeText(this@MainActivity5, "Error", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MainActivity5, Click3Activity::class.java))
                    }
                })
        }
    }
}