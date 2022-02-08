# prox-utils

## implementation
```
dependencies {
        implementation 'com.github.hellowordapp:prox-utils:1.0.1'
}
```

## Usage

> ### create interstitial
```sh
        ProxInterstitialAd inter = ProxUtils.INSTANCE.createInterstitialAd (Activity activity, String adId);
```

### load interstitial
```sh
        inter.load();
```

### show interstitial with Adclose call back
```sh
        inter.show(new AdClose() {
            @Override
            public void onAdClose() {
		`//TO-DO`
            }
        });

```

### show interstitial with Adclose after specific times
```sh
> ads will show after times time, if not it's will invoke callback

        inter.show(new AdClose() {
            @Override
            public void onAdClose() {
		`//TO-DO`
            }
        }, times);

```

### load splash with Adclose call back
```sh
        inter.loadSplash(int timeout, new AdClose() {
            @Override
            public void onAdClose() {
		`//TO-DO`
            }
        });

```

> ### create native ads
```sh 
> Params:
	- adContainer: where ads will show
	- layoutAdId: view of ads 're going to show

        ProxUtils.INSTANCE.createNativeAd (Activity activity, String adId, FrameLayout adContainer, int layoutAdId);
                .load(new NativeAdCallback() {
                    @Override
                    public void onNativeAdCallback() {
		    	`//TO-DO`
                    }
                });
```

### create native ads with shimmer
#### ads with already shimmer layout before loaded
```sh
> Params: 
	- layoutShimmerId: layout will be shown with shimmer effect before ads loaded
	
        ProxUtils.INSTANCE.createNativeAdWithShimmer(
                activity, String adId,
                FrameLayout adContainer, int layoutAdId, int layoutShimmerId).load(
                NativeAdCallback {

                })
```

> ### Open App Ads
```sh
    class OpenAdsApp: ProxOpenAdsApplication() {
        override fun getOpenAdsId(): String = "open-ads-id"
        
    }
> don't show ads at specific class
    disableOpenAdsAt(MainActivity::class.java)
```

## Push Rate

```sh
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val config = ProxRateDialog.Config()
        
        config.setListener(object : RatingDialogListener{
            override fun onChangeStar(rate: Int) {

            }

            override fun onSubmitButtonClicked(rate: Int, comment: String?) {

            }

            override fun onLaterButtonClicked() {

            }
        })

        ProxRateDialog.init(this, config)

        txt.setOnClickListener {
            // ProxRateDialog.showAlways(supportFragmentManager)
            ProxRateDialog.showIfNeed(supportFragmentManager)
        }
    } 
```

## Push update 
```sh
    ProxUtils.INSTANCE.initFirebaseRemoteConfig(Activity, appVersionCode, iconAppId, appName);
```


