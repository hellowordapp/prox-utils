package com.proxglobal.lib

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.proxglobal.proxads.ProxUtils
import com.proxglobal.proxads.ads.callback.AdClose
import com.proxglobal.proxads.ads.callback.NativeAdCallback
import com.proxglobal.rate.ProxRateDialog
import com.proxglobal.rate.ProxRateDialog.Config
import com.proxglobal.rate.RatingDialogListener

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

        findViewById<Button>(R.id.test_native_shimmer).setOnClickListener {
            ProxUtils.INSTANCE.createBigNativeAdWithShimmer(this, ProxUtils.TEST_NATIVE_ID,
                findViewById<FrameLayout>(R.id.ad_container)).load(
                NativeAdCallback {

                })
        }

        findViewById<Button>(R.id.test_native_medium_shimmer).setOnClickListener {
            ProxUtils.INSTANCE.createMediumNativeAdWithShimmer(this, ProxUtils.TEST_NATIVE_ID,
                findViewById<FrameLayout>(R.id.ad_container)).load(
                NativeAdCallback {

                })
        }

        val config = Config()

        config.setListener(object : RatingDialogListener {
            override fun onSubmitButtonClicked(rate: Int, comment: String?) {
                Toast.makeText(this@MainActivity, "Submit", Toast.LENGTH_SHORT).show()
            }

            override fun onLaterButtonClicked() {
                Toast.makeText(this@MainActivity, "Later", Toast.LENGTH_SHORT).show()
            }

            override fun onChangeStar(rate: Int) {
                Toast.makeText(this@MainActivity, "Star change", Toast.LENGTH_SHORT).show()
            }
        })

        ProxRateDialog.init(this@MainActivity, config)

        findViewById<View>(R.id.btn_show_rate).setOnClickListener { v: View? -> ProxRateDialog.showAlways(supportFragmentManager) }
    }

}