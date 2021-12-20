package com.proxglobal.lib

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import com.proxglobal.proxads.ProxUtils
import com.proxglobal.proxads.ads.callback.AdCallback
import com.proxglobal.proxads.ads.callback.NativeAdCallback
import com.proxglobal.proxads.ads.openads.AppOpenManager
import com.proxglobal.purchase.ProxPurchase
import com.proxglobal.rate.ProxRateDialog
import com.proxglobal.rate.ProxRateDialog.Config
import com.proxglobal.rate.RatingDialogListener

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        ProxPurchase.getInstance().syncPurchaseState()

        val inter = ProxUtils.INSTANCE.createInterstitialAd(this, ProxUtils.TEST_INTERSTITIAL_ID)
            .load()
        findViewById<Button>(R.id.test_interstitial).setOnClickListener(View.OnClickListener {
            inter.show(object : AdCallback() {
                override fun onAdClose() {
                    Toast.makeText(this@MainActivity, "Close", Toast.LENGTH_SHORT).show()
                }

                override fun onAdShow() {
                    super.onAdShow()
                    Toast.makeText(this@MainActivity, "Show", Toast.LENGTH_SHORT).show()
                }
            })
        })


        findViewById<Button>(R.id.test_interstitial_splash).setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }

        findViewById<Button>(R.id.test_native).setOnClickListener {
            ProxUtils.INSTANCE.createNativeAd(
                    this, ProxUtils.TEST_NATIVE_ID,
                    findViewById<FrameLayout>(R.id.ad_container), R.layout.ads_native_big
            ).load(
                    NativeAdCallback {

                    })
        }

        findViewById<Button>(R.id.test_native_big_shimmer).setOnClickListener {
            ProxUtils.INSTANCE.createBigNativeAdWithShimmer(
                    this, ProxUtils.TEST_NATIVE_ID,
                    findViewById<FrameLayout>(R.id.ad_container)
            ).load(
                    NativeAdCallback {

                    })
        }

        findViewById<Button>(R.id.test_native_medium_shimmer).setOnClickListener {
            ProxUtils.INSTANCE.createMediumNativeAdWithShimmer(
                    this, ProxUtils.TEST_NATIVE_ID,
                    findViewById<FrameLayout>(R.id.ad_container)
            ).load(
                    NativeAdCallback {

                    })
        }

        findViewById<Button>(R.id.test_native_shimmer).setOnClickListener {
            ProxUtils.INSTANCE.createNativeAdWithShimmer(
                    this, ProxUtils.TEST_NATIVE_ID,
                    findViewById<FrameLayout>(R.id.ad_container), R.layout.ads_native_big,
                    R.layout.layout_preloading_ads).load(
                    NativeAdCallback {

                    })
        }

        var a = true;
        findViewById<Button>(R.id.btn_toggle_open_ads).setOnClickListener {
            if(a) AppOpenManager.getInstance().disableOpenAds()
            else AppOpenManager.getInstance().enableOpenAds()

            a = !a;
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

        findViewById<View>(R.id.btn_show_rate).setOnClickListener { v: View? -> ProxRateDialog.showAlways(
                supportFragmentManager
        ) }

        ProxUtils.INSTANCE.initFirebaseRemoteConfig(this, BuildConfig.VERSION_CODE, BuildConfig.DEBUG,
                R.drawable.ic_launcher_background, getString(R.string.app_display_name))


        // test purchase
        findViewById<Button>(R.id.btn_test_iap).setOnClickListener {
            startActivity(Intent(this@MainActivity, MainActivity3::class.java))
        }
    }
}