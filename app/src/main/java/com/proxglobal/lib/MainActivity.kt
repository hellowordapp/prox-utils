package com.proxglobal.lib

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.proxglobal.lib.databinding.ActivityMainBinding
import com.proxglobal.proxads.ProxUtils
import com.proxglobal.proxads.ads.openads.AppOpenManager
import com.proxglobal.proxads.adsv2.callback.AdsCallback
import com.proxglobal.proxads.adsv2.ads.ProxAds
import com.proxglobal.proxads.adsv2.callback.RewardCallback
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

        ProxAds.instance.showBanner(
            this,
            binding.bannerContainer,
            ProxUtils.TEST_BANNER_ID,
            object : AdsCallback() {
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

        ProxAds.instance.configure(this, "appbe67360c55654f97b2", "vz3ebfacd56a34480da8")
        ProxAds.instance.initInterstitial(this, ProxUtils.TEST_INTERSTITIAL_ID, "vz3ebfacd56a34480da8", "inter")
        binding.testInterstitial.setOnClickListener {
            ProxAds.instance.showInterstitial(this, "inter", object : AdsCallback() {
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
        }

        binding.testInterstitialSplash.setOnClickListener {
            ProxAds.instance.showSplash(this, object : AdsCallback() {
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

        binding.testNativeSmallWithShimmer.setOnClickListener {
            ProxAds.instance.showSmallNativeWithShimmerStyle15(
                this,
                ProxUtils.TEST_NATIVE_ID,
                binding.adContainer,
                object : AdsCallback() {
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

        binding.testNativeMediumWithShimmer.setOnClickListener {
            ProxAds.instance.showMediumNativeWithShimmerStyle19(
                this,
                ProxUtils.TEST_NATIVE_ID,
                binding.adContainer,
                object : AdsCallback() {
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

        binding.testNativeBigWithShimmer.setOnClickListener {
            ProxAds.instance.showBigNativeWithShimmerStyle1(
                this,
                ProxUtils.TEST_NATIVE_ID,
                binding.adContainer,
                object : AdsCallback() {
                    override fun onShow() {
                        Toast.makeText(this@MainActivity, "Show", Toast.LENGTH_SHORT).show()
                    }

                    override fun onClosed() {
                        Toast.makeText(this@MainActivity, "Close", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError() {
                        Toast.makeText(this@MainActivity, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        ProxAds.instance.initRewardAds(this, ProxUtils.TEST_REWARD_ID, "reward")
        binding.testReward.setOnClickListener {
            ProxAds.instance.showRewardAds(
                this,
                "reward",
                object : AdsCallback() {
                    override fun onShow() {
                        Toast.makeText(this@MainActivity, "Show", Toast.LENGTH_SHORT).show()
                    }

                    override fun onClosed() {
                        Toast.makeText(this@MainActivity, "Closed", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError() {
                        Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                }, object : RewardCallback() {
                    override fun getReward(amount: Int, type: String) {
                        Toast.makeText(
                            this@MainActivity,
                            "Reward: $amount $type",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

        binding.btnSurvey.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainActivity4::class.java))
        }

        val config = ProxRateDialog.Config()
        config.isCanceledOnTouchOutside = true
        config.setListener(object : RatingDialogListener() {
            override fun onRated() {
                Toast.makeText(this@MainActivity, "Rated", Toast.LENGTH_SHORT).show()
            }

            override fun onSubmitButtonClicked(rate: Int, comment: String) {
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
            if (a) AppOpenManager.getInstance().disableOpenAds()
            else AppOpenManager.getInstance().enableOpenAds()
            a = !a

            Toast.makeText(this@MainActivity, "OpenAds: $a", Toast.LENGTH_SHORT).show()
        }

        binding.btnTestIap.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainActivity3::class.java))
        }
    }
}