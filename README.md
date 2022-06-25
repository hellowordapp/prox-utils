# prox-utils

## implementation
```
dependencies {
	implementation 'com.github.hellowordapp:prox-utils:1.2.0'
}
```

## Usage

### load and show banner with AdsCallback
```sh
        ProxAds.getInstance().showBanner(Activity activity, FrameLayout adContainer, String adId, new AdsCallback() {
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
        ProxAds.getInstance().initInterstitial(Activity activity, String adId, String colonyZoneId, String tag);
```
### show interstitial with AdsCallback
```sh
        ProxAds.getInstance().showInterstitial(Activity activity, String tag, new AdsCallback() {
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
        ProxAds.getInstance().showSplash(Activity activity, new AdsCallback() {
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
            }, String adId, String colonyZoneId, int timeout);

```

### load and show native small ads with AdsCallback

```sh 
	ProxAds.getInstance().showSmallNativeMax(Activity activity, String adId, FrameLayout adContainer, new AdsCallback() {
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

### load and show native medium ads with AdsCallback
```sh 
        ProxAds.getInstance().showMediumNative(Activity activity, String adId, FrameLayout adContainer, new AdsCallback() {
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
        ProxAds.getInstance().showBigNative(Activity activity, String adId, FrameLayout adContainer, new AdsCallback() {
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
ProxAds.getInstance().showSmallNativeWithShimmer(Activity activity, String adId, FrameLayout adContainer, new AdsCallback() {...});
ProxAds.getInstance().showMediumNativeWithShimmer(Activity activity, String adId, FrameLayout adContainer, new AdsCallback() {...});
ProxAds.getInstance().showBigNativeWithShimmer(Activity activity, String adId, FrameLayout adContainer, new AdsCallback() {...});
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
