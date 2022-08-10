package com.proxglobal.lib

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.proxglobal.lib.databinding.ActivityMainBinding
import com.proxglobal.proxads.ProxUtils
import com.proxglobal.proxads.adsv2.admax.openads.MaxOpenAds
import com.proxglobal.proxads.adsv2.ads.ProxAds
import com.proxglobal.proxads.adsv2.callback.AdsCallback
import com.proxglobal.rate.ProxRateDialog
import com.proxglobal.rate.RatingDialogListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ProxUtils.INSTANCE.initFirebaseRemoteConfig(
            this, BuildConfig.VERSION_CODE, BuildConfig.DEBUG,
            R.drawable.ic_launcher_background, getString(R.string.app_display_name)
        )

        binding.testMediation.setOnClickListener {
            ProxAds.instance.showMaxMediationDebug(this@MainActivity)
        }

        ProxAds.instance.showBannerMax(
            this,
            findViewById(R.id.banner_container),
            ProxUtils.TEST_BANNER_MAX_ID,
            object : AdsCallback() {
                override fun onShow() {
                    super.onShow()
                    Toast.makeText(this@MainActivity, "Show", Toast.LENGTH_SHORT).show()
                }

                override fun onClosed() {
                    super.onClosed()
                    Toast.makeText(this@MainActivity, "Close", Toast.LENGTH_SHORT).show()
                }

                override fun onError() {
                    super.onError()
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            })

        ProxAds.instance.initInterstitialMax(this, ProxUtils.TEST_INTERSTITIAL_MAX_ID, "max")
        binding.testInterstitial.setOnClickListener {
            ProxAds.instance.showInterstitialMax(this@MainActivity, "max", object : AdsCallback() {
                override fun onShow() {
                    super.onShow()
                    Toast.makeText(this@MainActivity, "Show", Toast.LENGTH_SHORT).show()
                }

                override fun onClosed() {
                    super.onClosed()
                    Toast.makeText(this@MainActivity, "Close", Toast.LENGTH_SHORT).show()
                }

                override fun onError() {
                    super.onError()
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.testInterstitialSplash.setOnClickListener {
            ProxAds.instance.showSplashMax(this@MainActivity, object : AdsCallback() {
                override fun onShow() {
                    super.onShow()
                    Toast.makeText(this@MainActivity, "Show", Toast.LENGTH_SHORT).show()
                }

                override fun onClosed() {
                    super.onClosed()
                    Toast.makeText(this@MainActivity, "Close", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MainActivity, MainActivity2::class.java))
                }

                override fun onError() {
                    super.onError()
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MainActivity, MainActivity2::class.java))
                }
            }, ProxUtils.TEST_INTERSTITIAL_MAX_ID, 12000)
        }

        binding.testNativeSmallShimmer.setOnClickListener {
            ProxAds.instance.showSmallNativeMaxWithShimmerStyle15(
                this@MainActivity, ProxUtils.TEST_NATIVE_MAX_ID,
                findViewById(R.id.ad_container), object : AdsCallback() {
                    override fun onShow() {
                        super.onShow()
                        Toast.makeText(this@MainActivity, "Show", Toast.LENGTH_SHORT).show()
                    }

                    override fun onClosed() {
                        super.onClosed()
                        Toast.makeText(this@MainActivity, "Close", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError() {
                        super.onError()
                        Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        binding.testNativeMediumShimmer.setOnClickListener {
            ProxAds.instance.showMediumNativeMaxWithShimmerStyle19(
                this@MainActivity, ProxUtils.TEST_NATIVE_MAX_ID,
                findViewById(R.id.ad_container), object : AdsCallback() {
                    override fun onShow() {
                        super.onShow()
                        Toast.makeText(this@MainActivity, "Show", Toast.LENGTH_SHORT).show()
                    }

                    override fun onClosed() {
                        super.onClosed()
                        Toast.makeText(this@MainActivity, "Close", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError() {
                        super.onError()
                        Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        binding.testNativeBigShimmer.setOnClickListener {
            ProxAds.instance.showBigNativeMaxWithShimmerStyle1(
                this@MainActivity, ProxUtils.TEST_NATIVE_MAX_ID,
                findViewById(R.id.ad_container), object : AdsCallback() {
                    override fun onShow() {
                        super.onShow()
                        Toast.makeText(this@MainActivity, "Show", Toast.LENGTH_SHORT).show()
                    }

                    override fun onClosed() {
                        super.onClosed()
                        Toast.makeText(this@MainActivity, "Close", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError() {
                        super.onError()
                        Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        binding.testSurvey.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainActivity4::class.java))
        }

        val config = ProxRateDialog.Config()
        config.isCanceledOnTouchOutside = true
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
                Toast.makeText(this@MainActivity, "Done", Toast.LENGTH_SHORT).show()
            }
        })
        ProxRateDialog.init(config)

        binding.btnShowRate.setOnClickListener {
            ProxRateDialog.showAlways(
                this,
                supportFragmentManager
            )
        }

        var a = true
        binding.btnToggleOpenAds.setOnClickListener {
            if (a) MaxOpenAds.instance.disableOpenAds()
            else MaxOpenAds.instance.enableOpenAds()
            a = !a

            Toast.makeText(this@MainActivity, "OpenAds: $a", Toast.LENGTH_SHORT).show()
        }

        binding.btnTestIap.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainActivity3::class.java))
        }
    }
}