package com.proxglobal.lib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.proxglobal.proxads.ProxUtils;
import com.proxglobal.proxads.adsv2.ads.ProxAds;
import com.proxglobal.proxads.adsv2.callback.AdsCallback;

public class MaxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max);

        ProxAds.getInstance().showBannerMax(this, findViewById(R.id.banner_container), ProxUtils.TEST_BANNER_MAX_ID, new AdsCallback() {
            @Override
            public void onShow() {
                super.onShow();
                Toast.makeText(MaxActivity.this, "Show", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClosed() {
                super.onClosed();
                Toast.makeText(MaxActivity.this, "Close", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                super.onError();
                Toast.makeText(MaxActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        ProxAds.getInstance().initInterstitialMax(this, ProxUtils.TEST_INTERSTITIAL_MAX_ID, "max");
        findViewById(R.id.test_interstitial).setOnClickListener(v -> {
            ProxAds.getInstance().showInterstitialMax(MaxActivity.this, "max", new AdsCallback() {
                @Override
                public void onShow() {
                    super.onShow();
                    Toast.makeText(MaxActivity.this, "Show", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onClosed() {
                    super.onClosed();
                    Toast.makeText(MaxActivity.this, "Close", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError() {
                    super.onError();
                    Toast.makeText(MaxActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        });

        findViewById(R.id.test_interstitial_splash).setOnClickListener(v -> {
            ProxAds.getInstance().showSplashMax(MaxActivity.this, new AdsCallback() {
                @Override
                public void onShow() {
                    super.onShow();
                    Toast.makeText(MaxActivity.this, "Show", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onClosed() {
                    super.onClosed();
                    Toast.makeText(MaxActivity.this, "Close", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError() {
                    super.onError();
                    Toast.makeText(MaxActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }, ProxUtils.TEST_INTERSTITIAL_MAX_ID, 12000);
        });

        findViewById(R.id.test_native_medium).setOnClickListener(v -> ProxAds.getInstance().showMediumNativeMax(
                MaxActivity.this, ProxUtils.TEST_NATIVE_MAX_ID,
                findViewById(R.id.ad_container), new AdsCallback() {
                    @Override
                    public void onShow() {
                        super.onShow();
                        Toast.makeText(MaxActivity.this, "Show", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onClosed() {
                        super.onClosed();
                        Toast.makeText(MaxActivity.this, "Close", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        super.onError();
                        Toast.makeText(MaxActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
        ));

        findViewById(R.id.test_native_big).setOnClickListener(v -> ProxAds.getInstance().showBigNativeMax(
                MaxActivity.this, ProxUtils.TEST_NATIVE_MAX_ID,
                findViewById(R.id.ad_container), new AdsCallback() {
                    @Override
                    public void onShow() {
                        super.onShow();
                        Toast.makeText(MaxActivity.this, "Show", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onClosed() {
                        super.onClosed();
                        Toast.makeText(MaxActivity.this, "Close", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        super.onError();
                        Toast.makeText(MaxActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
        ));

        findViewById(R.id.test_native_medium_shimmer).setOnClickListener(v -> ProxAds.getInstance().showMediumNativeMaxWithShimmer(
                MaxActivity.this, ProxUtils.TEST_NATIVE_MAX_ID,
                findViewById(R.id.ad_container), new AdsCallback() {
                    @Override
                    public void onShow() {
                        super.onShow();
                        Toast.makeText(MaxActivity.this, "Show", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onClosed() {
                        super.onClosed();
                        Toast.makeText(MaxActivity.this, "Close", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        super.onError();
                        Toast.makeText(MaxActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
        ));

        findViewById(R.id.test_native_big_shimmer).setOnClickListener(v -> ProxAds.getInstance().showBigNativeMaxWithShimmer(
                MaxActivity.this, ProxUtils.TEST_NATIVE_MAX_ID,
                findViewById(R.id.ad_container), new AdsCallback() {
                    @Override
                    public void onShow() {
                        super.onShow();
                        Toast.makeText(MaxActivity.this, "Show", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onClosed() {
                        super.onClosed();
                        Toast.makeText(MaxActivity.this, "Close", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        super.onError();
                        Toast.makeText(MaxActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
        ));
    }
}