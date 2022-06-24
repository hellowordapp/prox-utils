package com.proxglobal.lib

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.proxglobal.proxads.ProxUtils
import com.proxglobal.proxads.ads.callback.NativeAdCallback
import com.proxglobal.proxads.ads.openads.AppOpenManager
import com.proxglobal.proxads.adsv2.callback.AdsCallback
import com.proxglobal.proxads.adsv2.ads.ProxAds
import com.proxglobal.rate.ProxRateDialog
import com.proxglobal.rate.ProxRateDialog.Config
import com.proxglobal.rate.RatingDialogListener

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ProxAds.getInstance().showBanner(this, findViewById(R.id.banner_container), ProxUtils.TEST_BANNER_ID,
            object: AdsCallback() {
                override fun onShow() {
                    Toast.makeText(this@MainActivity, "Show", Toast.LENGTH_SHORT).show()
                }

                override fun onClosed() {
                    Toast.makeText(this@MainActivity, "Close", Toast.LENGTH_SHORT).show()
                }

                override fun onError() {
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        )

        ProxAds.getInstance().configure(this, "appbe67360c55654f97b2", "vz3ebfacd56a34480da8")
        ProxAds.getInstance().initInterstitial(this, "fjdlsafj", "vz3ebfacd56a34480da8", "inter");
//        ProxPurchase.getInstance().syncPurchaseState()
        findViewById<Button>(R.id.test_interstitial).setOnClickListener(View.OnClickListener {
            ProxAds.getInstance().showInterstitial(this, "inter", object: AdsCallback() {
                override fun onShow() {
                    Toast.makeText(this@MainActivity, "Show", Toast.LENGTH_SHORT).show()
                }

                override fun onClosed() {
                    Toast.makeText(this@MainActivity, "Close", Toast.LENGTH_SHORT).show()
                }

                override fun onError() {
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                }

            })
        })

        findViewById<Button>(R.id.test_interstitial_splash).setOnClickListener {
            ProxAds.getInstance().showSplash(this, object : AdsCallback() {
                override fun onShow() {
                    Toast.makeText(this@MainActivity, "Show", Toast.LENGTH_SHORT).show()
                }
                override fun onClosed() {
                    Toast.makeText(this@MainActivity, "Close", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MainActivity, MainActivity2::class.java))
                }
                override fun onError() {
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MainActivity, MainActivity2::class.java))
                }
            }, ProxUtils.TEST_INTERSTITIAL_ID, "vz3ebfacd56a34480da8", 12000)
        }

        findViewById<Button>(R.id.test_native_small).setOnClickListener {
            ProxAds.getInstance().showSmallNative(
                this, ProxUtils.TEST_NATIVE_ID,
                findViewById<FrameLayout>(R.id.ad_container), object: AdsCallback() {
                    override fun onShow() {
                        Toast.makeText(this@MainActivity, "Show", Toast.LENGTH_SHORT).show()
                    }

                    override fun onClosed() {
                        Toast.makeText(this@MainActivity, "Close", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError() {
                        Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        findViewById<Button>(R.id.test_native_medium).setOnClickListener {
            ProxAds.getInstance().showMediumNative(
                    this, ProxUtils.TEST_NATIVE_ID,
                findViewById<FrameLayout>(R.id.ad_container), object: AdsCallback() {
                    override fun onShow() {
                        Toast.makeText(this@MainActivity, "Show", Toast.LENGTH_SHORT).show()
                    }

                    override fun onClosed() {
                        Toast.makeText(this@MainActivity, "Close", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError() {
                        Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        findViewById<Button>(R.id.test_native_big).setOnClickListener {
            ProxAds.getInstance().showBigNative(
                    this, ProxUtils.TEST_NATIVE_ID,
                findViewById<FrameLayout>(R.id.ad_container), object : AdsCallback() {
                    override fun onShow() {
                        Toast.makeText(this@MainActivity, "Show", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError() {
                        Toast.makeText(this@MainActivity, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        findViewById<Button>(R.id.test_native_small_with_shimmer).setOnClickListener {
            ProxAds.getInstance().showSmallNativeWithShimmer(
                this, ProxUtils.TEST_NATIVE_ID,
                findViewById<FrameLayout>(R.id.ad_container), object: AdsCallback() {
                    override fun onShow() {
                        Toast.makeText(this@MainActivity, "Show", Toast.LENGTH_SHORT).show()
                    }

                    override fun onClosed() {
                        Toast.makeText(this@MainActivity, "Close", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError() {
                        Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        findViewById<Button>(R.id.test_native_medium_with_shimmer).setOnClickListener {
            ProxAds.getInstance().showMediumNativeWithShimmer(
                this, ProxUtils.TEST_NATIVE_ID,
                findViewById<FrameLayout>(R.id.ad_container), object: AdsCallback() {
                    override fun onShow() {
                        Toast.makeText(this@MainActivity, "Show", Toast.LENGTH_SHORT).show()
                    }

                    override fun onClosed() {
                        Toast.makeText(this@MainActivity, "Close", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError() {
                        Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        findViewById<Button>(R.id.test_native_big_with_shimmer).setOnClickListener {
            ProxAds.getInstance().showBigNativeWithShimmer(
                this, ProxUtils.TEST_NATIVE_ID,
                findViewById<FrameLayout>(R.id.ad_container), object : AdsCallback() {
                    override fun onShow() {
                        Toast.makeText(this@MainActivity, "Show", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError() {
                        Toast.makeText(this@MainActivity, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        var a = true;
        findViewById<Button>(R.id.btn_toggle_open_ads).setOnClickListener {
            if(a) AppOpenManager.getInstance().disableOpenAds()
            else AppOpenManager.getInstance().enableOpenAds()

            a = !a;
        }

        val config = Config()

        config.setListener(object : RatingDialogListener() {
            override fun onRated() {
                super.onRated()
                Toast.makeText(this@MainActivity, "Rated", Toast.LENGTH_SHORT).show()
            }
            override fun onSubmitButtonClicked(rate: Int, comment: String?) {
                Toast.makeText(this@MainActivity, "Submit", Toast.LENGTH_SHORT).show()
            }

            override fun onLaterButtonClicked() {
                Toast.makeText(this@MainActivity, "Later", Toast.LENGTH_SHORT).show()
            }

            override fun onChangeStar(rate: Int) {
                Toast.makeText(this@MainActivity, "Star change", Toast.LENGTH_SHORT).show()
            }

            override fun onDone() {
                TODO("Not yet implemented")
            }
        })

        config.setForegroundIcon(ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground))
        ProxRateDialog.init(config)

        findViewById<View>(R.id.btn_show_rate).setOnClickListener { v: View? -> ProxRateDialog.showIfNeed(this,
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