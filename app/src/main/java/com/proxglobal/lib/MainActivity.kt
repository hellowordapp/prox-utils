package com.proxglobal.lib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import com.proxglobal.ProxUtils
import com.proxglobal.proxads.ads.callback.AdClose
import com.proxglobal.proxads.ads.callback.NativeAdCallback

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val inter = ProxUtils.INSTANCE.createInterstitialAd(this, ProxUtils.TEST_INTERSTITIAL_ID)
            .load()
        findViewById<Button>(R.id.test_interstitial).setOnClickListener(View.OnClickListener {
            inter.show(AdClose {
                inter.load()
            })
        })

        findViewById<Button>(R.id.test_interstitial_splash).setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }

        findViewById<Button>(R.id.test_native).setOnClickListener {
            ProxUtils.INSTANCE.createNativeAd(this, ProxUtils.TEST_NATIVE_ID,
                findViewById<FrameLayout>(R.id.ad_container), R.layout.ads_native_big).load(
                NativeAdCallback {

                })
        }

    }

}