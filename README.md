# prox-utils

## Add the JitPack repository to your build file
```
allprojects {
	repositories {
		...
		jcenter()
		maven { url 'https://jitpack.io' }
	}
}
```

## Add the dependency
```
dependencies {
        implementation 'com.github.hellowordapp:prox-utils:2.1.2-max'
}
```

## Push update

### Need to add this line to your MainActivity to push users to update the app:

```sh
ProxUtils.INSTANCE.initFirebaseRemoteConfig(Activity, appVersionCode, iconAppId, appName);
```

## Usage

### Add the following \<meta-data\> element to your AndroidManifest.xml inside the \<application\> element:

```sh
<meta-data android:name="applovin.sdk.key"
    android:value="YOUR_SDK_KEY_HERE"/>
```

> #### If you use the mediation with Admob, add APPLICATION_ID of Admob to AndroidManifest.xml like this:

```sh
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="YOUR_APP_ID_HERE" />
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

### Customize all native ads
```sh 
<color name="ads_text_color">@color/black</color>
<color name="ads_border_color">#C4CDE0</color>

<color name="ads_btn_background_tint">#2471ff</color>
<color name="ads_btn_text_color">#ffffffff</color>
<color name="ads_btn_ripple_color">#ffffffff</color>

<dimen name="ads_btn_radius">0dp</dimen>
<dimen name="ads_btn_padding_left">16dp</dimen>
<dimen name="ads_btn_padding_right">16dp</dimen>
<dimen name="ads_btn_padding_top">4dp</dimen>
<dimen name="ads_btn_padding_bottom">4dp</dimen>
```

### Open App Ads

```sh
    class OpenAdsApp: MaxOpenAdsApplication() {
        override fun getOpenAdsId(): String = "open-ads-id"
        
    }
```

> #### don't show ads at specific class

	disableOpenAdsAt(MainActivity::class.java)

> #### disable/enable open ads

	MaxOpenAds.getInstance().disableOpenAds()/MaxOpenAds.getInstance().enableOpenAds()

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

