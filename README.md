# prox-utils

## implementation
```
dependencies {
        implementation 'com.github.hellowordapp:prox-utils:1.1.5'
}
```

## Usage
### Add the following \<meta-data\> element to your AndroidManifest.xml inside the \<application\> element:
```sh
	<meta-data android:name="applovin.sdk.key"
           	android:value="YOUR_SDK_KEY_HERE"/>
```
### initialize the SDK inside your application
```sh
        ProxAds.getInstance().initMax(Context context);
```
### load and show banner with AdsCallback
```sh
        ProxAds.getInstance().showBannerMax(Activity activity, FrameLayout adContainer, String adId, new AdsCallback() {
            @Override
            public void onShow() {
                super.onShow();
                `//TO-DO`
            }

            @Override
            public void onClosed() {
                super.onClosed();
                `//TO-DO`
            }

            @Override
            public void onError() {
                super.onError();
                `//TO-DO`
            }
        });
```
### create and load interstitial
```sh
        ProxAds.getInstance().initInterstitialMax(Activity activity, String adId, String tag);
```
### show interstitial with AdsCallback
```sh
        ProxAds.getInstance().showInterstitialMax(Activity activity, String tag, new AdsCallback() {
                @Override
                public void onShow() {
                    super.onShow();
                    `//TO-DO`
                }

                @Override
                public void onClosed() {
                    super.onClosed();
                    `//TO-DO`
                }

                @Override
                public void onError() {
                    super.onError();
                    `//TO-DO`
                }
            });

```
### load and show splash with AdsCallback after specific times
```sh
        ProxAds.getInstance().showSplashMax(Activity activity, new AdsCallback() {
                @Override
                public void onShow() {
                    super.onShow();
                    `//TO-DO`
                }

                @Override
                public void onClosed() {
                    super.onClosed();
                    `//TO-DO`
                }

                @Override
                public void onError() {
                    super.onError();
                    `//TO-DO`
                }
            }, String adId, int timeout);

```
### load and show native medium ads with AdsCallback
```sh 
        ProxAds.getInstance().showMediumNativeMax(Activity activity, String adId, FrameLayout adContainer, new AdsCallback() {
                @Override
                public void onShow() {
                    super.onShow();
                    `//TO-DO`
                }

                @Override
                public void onClosed() {
                    super.onClosed();
                    `//TO-DO`
                }

                @Override
                public void onError() {
                    super.onError();
                    `//TO-DO`
                }
        });
```
### load and show native big ads with AdsCallback
```sh 
        ProxAds.getInstance().showBigNativeMax(Activity activity, String adId, FrameLayout adContainer, new AdsCallback() {
                @Override
                public void onShow() {
                    super.onShow();
                    `//TO-DO`
                }

                @Override
                public void onClosed() {
                    super.onClosed();
                    `//TO-DO`
                }

                @Override
                public void onError() {
                    super.onError();
                    `//TO-DO`
                }
        });
```
> #### load and show native ads with shimmer
> #### ads with already shimmer layout before loaded
```sh
        ProxAds.getInstance().showMediumNativeMaxWithShimmer(Activity activity, String adId, FrameLayout adContainer, new AdsCallback() {...});
	ProxAds.getInstance().showBigNativeMaxWithShimmer(Activity activity, String adId, FrameLayout adContainer, new AdsCallback() {...});
```
### Open App Ads
```sh
    class OpenAdsApp: ProxOpenAdsApplication() {
        override fun getOpenAdsId(): String = "open-ads-id"
        
    }
```

> #### don't show ads at specific class
	disableOpenAdsAt(MainActivity::class.java)
> #### disable/enable open ads 
	AppOpenManager.getInstance().disableOpenAds()/AppOpenManager.getInstance().enableOpenAds()

## In app purchase
### Init
put this into application:
```sh
	ProxPurchase.getInstance().initBilling(this, listOf(
			BuildConfig.id_test1_purchase
		), listOf(
			BuildConfig.id_test2_subs,
			BuildConfig.id_test3_subs
		))
```
### Purchase/Subscribe
```sh
	ProxPurchase.getInstance().purchase(activity, purchase_id)
	ProxPurchase.getInstance().subscribe(activity, subscription_id)
```

### Get price of purchase/sub
```sh
	ProxPurchase.getInstance().getPrice(purchase_id) // for purchase
	ProxPurchase.getInstance().getPriceSub(sub_id) // for sub
```

### Check app is in purchase
```sh
	ProxPurchase.getInstance().checkPurchased() // with cache after call ProxPurchase.getInstance().syncPurchaseState()
	ProxPurchase.getInstance().isPurchased() // realtime but low performance
```

### Purchase/Subscribe
```sh
	ProxPurchase.getInstance().purchase(activity, purchase_id)
	ProxPurchase.getInstance().subscribe(activity, subscription_id)
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
	    
	    override fun onDone() {
	    	//this method will call after dismiss tks dialog
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

## Ads version 2
```sh
    ProxAds.getInstance().<call method you want>
```
