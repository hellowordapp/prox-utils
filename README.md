# prox-utils

## implementation
```
dependencies {
        implementation 'com.github.hellowordapp:prox-utils:0.0.7'
}
```

## Usage

> ### create interstitial
```sh
        ProxInterstitialAd inter = ProxAdUtils.INSTANCE.createInterstitialAd (Activity activity, String adId);
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
        ProxAdUtils.INSTANCE.createNativeAd (Activity activity, String adId, FrameLayout adContainer, int layoutAdId);
                .load(new NativeAdCallback() {
                    @Override
                    public void onNativeAdCallback() {
		    	`//TO-DO`
                    }
                });
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
    ProxUtils.INSTANCE.initFirebaseRemoteConfig(Activity, appVersionCode);
```


