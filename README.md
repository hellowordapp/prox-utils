Prox Utils
==================
[![Prox Global](https://github.com/ntduc-let/image_readme_github/blob/master/prox_global_2.jpg)](https://github.com/hellowordapp/prox-utils)

## Index
* [Setup](#Setup)
* [Push Update](#Push-Update)
* [Survey](#Survey)
* [Push Rate](#Push-Rate)
* [In App Purchase](#In-App-Purchase)
* [Open App Ads](#Open-App-Ads)
* [Banner Ads](#Banner-Ads)
* [Interstitial Ads](#Interstitial-Ads)
* [Native Ads](#Native-Ads)
* [Reward Ads](#Reward-Ads)
* [Remote Config Ads](#Remote-Config-Ads)

Setup
======================
Add the following to your project's root build.gradle file
```
allprojects {
	repositories {
		...
		jcenter()
		maven { url 'https://jitpack.io' }
		maven { url "https://android-sdk.is.com" }
		maven{
			url "${artifactory_contextUrl}"
			credentials{
				username = "${artifactory_user}"
				password = "${artifactory_password}"
			}
		}
	}
}
```

Add the following to your project's gradle.properties file
```
artifactory_user=<user>
artifactory_password=<password>
artifactory_contextUrl=<contextUrl>
```

Add the following to your project's build.gradle file
```
dependencies {
	implementation "prox-lib:prox-utils-max:2.2.8"
}
```
Add the configuration to your application attribute in your application manifest AndroidManifest.xml
```
<?xml version="1.0" encoding="utf-8"?>
<manifest … >
    <application android:networkSecurityConfig="@xml/network_security_config"
                    … >
        ⋮
    </application>
</manifest>
```

In the res/xml/network_security_config.xmlfile, add sections that permit cleartext traffic for the networks that require this:
```
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>

    <!-- For AdColony, this permits all cleartext traffic: -->
    <base-config cleartextTrafficPermitted="true">
        <trust-anchors>
            <certificates src="system"/>
        </trust-anchors>
    </base-config>
    <!-- End AdColony section -->

    <domain-config cleartextTrafficPermitted="true">

        <!-- For Meta Audience Network, this permits cleartext traffic to localhost: -->
        <domain includeSubdomains="true">127.0.0.1</domain>
        <!-- End Meta Audience Network section -->

    </domain-config>
</network-security-config>
```

Add the following \<meta-data\> element to your AndroidManifest.xml inside the \<application\> element:
```
<meta-data android:name="applovin.sdk.key"
    android:value="YOUR_SDK_KEY_HERE"/>
```

If you use the mediation with Admob, add APPLICATION_ID of Admob to AndroidManifest.xml like this:
```
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="YOUR_APP_ID_HERE" />
```

Initialize the SDK inside your application
```
ProxAds.instance.initMax(this)
```

Push Update
======================
Setting on Remote Config
```
Parameter name (key): config_update_version
Data type: String
Default value:
	{  
  		"version_code": 3,  
  		"version_name": "Version 1.0.2",  
  		"title": "Update New Version",
  		"status": true,
  		"message": "Updated version with many performance improvements and new features. Update your apps now!",
  		"version_code_required": [
  			1  
  		],  
  		"required": false,
  		"new_package": ""
	}
```

Need to add this line to your MainActivity to push users to update your app:
```
ProxUtils.INSTANCE.initFirebaseRemoteConfig(AppCompatActivity activity, int appVersionCode, boolean isDebug, int iconAppId, String appName)
```

Survey
======================
Setting on Remote Config
```
Parameter name (key): config_survey
Data type: String
Default value:
	{      
  		"style": 1,    
  		"status": true,    
  		"survey_name": "Survey name", 
		
  		"question_1": "Question 1",      
  		"hint_answer_1": "Hint answer 1",      
  		"option_1_1": "Option 1_1",      
  		"option_1_2": "Option 1_2",      
  		"option_1_3": "Option 1_3",      
  		"option_1_4": "Option 1_4",      
		
  		"question_2": "Question 2",      
  		"hint_answer_2": "Hint answer 2",      
  		"option_2_1": "Option 2_1",      
  		"option_2_2": "Option 2_2",      
  		"option_2_3": "Option 3_3",      
  		"option_2_4": "Option 4_4" 
	}
```

Add this line to your Activity to ask users to survey your app:
```
ProxSurveyConfig().showSurveyIfNecessary(activity: AppCompatActivity, isDebug: Boolean)
```

Style Survey
Style | Demo | Style | Demo | Style | Demo
--- | --- | --- | --- | --- | ---
SUV 1 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/SUV1.png"> | SUV 4 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/SUV4.png"> | SUV 7 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/SUV7.png">
SUV 2 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/SUV2.png"> | SUV 5 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/SUV5.png"> | SUV 8 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/SUV8.png">
SUV 3 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/SUV3.png"> | SUV 6 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/SUV6.png"> | 

## Change background color survey
```
<color name="survey_main_color">#5D9EFF</color>
<color name="survey_secondary_color">#385D9EFF</color>
```

Push Rate
======================
Initialize dialog rate
```
val config = ProxRateConfig()
config.listener = object : RatingDialogListener() {
	override fun onRated() {
                `//TO-DO`
	}

	override fun onSubmitButtonClicked(rate: Int, comment: String) {
                `//TO-DO`
	}

	override fun onLaterButtonClicked() {
                `//TO-DO`
	}

	override fun onChangeStar(rate: Int) {
                `//TO-DO`
	}

	override fun onDone() {
                `//TO-DO`
	}
}
ProxRateDialog.init(config)
```

Show dialog rate
```
ProxRateDialog.showAlways(FragmentManager fm)
ProxRateDialog.showIfNeed(Context context, FragmentManager fm)
```

In App Purchase
======================
Put this into application:
```
ProxPurchase.getInstance().initBilling(
	this,
	listOf(BuildConfig.id_test1_purchase),
	listOf(BuildConfig.id_test2_subs,BuildConfig.id_test3_subs)
)
```

Purchase/Subscribe
```
ProxPurchase.getInstance().purchase(activity, purchase_id)
ProxPurchase.getInstance().subscribe(activity, subscription_id)
```

Get price of purchase/sub
```
ProxPurchase.getInstance().getPrice(purchase_id) 	// for purchase
ProxPurchase.getInstance().getPriceSub(sub_id) 		// for sub
```

Check app is in purchase
```
ProxPurchase.getInstance().checkPurchased() 	// with cache after call ProxPurchase.getInstance().syncPurchaseState()
ProxPurchase.getInstance().isPurchased() 	// realtime but low performance
```

Purchase/Subscribe
```
ProxPurchase.getInstance().purchase(activity, purchase_id)
ProxPurchase.getInstance().subscribe(activity, subscription_id)
```

Open App Ads
======================
Initialize
```
class OpenAdsApp: MaxOpenAdsApplication() {
	override fun getOpenAdsId(): String = "open-ads-id"
}
```
Don't show ads at specific class
```
disableOpenAdsAt(MainActivity::class.java)
```
Disable/Enable open ads
```
MaxOpenAds.instance.disableOpenAds()/MaxOpenAds.instance.enableOpenAds()
```

Banner Ads
======================
```
ProxAds.instance.showBannerMax(activity: Activity, container: FrameLayout?, adId: String?, object : AdsCallback() {
	override fun onShow() {
		super.onShow()
                `//TO-DO`
	}

	override fun onClosed() {
                super.onClosed()
                `//TO-DO`
	}

	override fun onError() {
                super.onError()
                `//TO-DO`
	}
})
```

Interstitial Ads
======================
Load interstitial ads
```
ProxAds.instance.initInterstitialMax(activity: Activity, adId: String, tag: String)
```

Show interstitial ads
```
ProxAds.instance.showInterstitialMax(activity: Activity, tag: String, object : AdsCallback() {
	override fun onShow() {
		super.onShow()
                `//TO-DO`
	}

	override fun onClosed() {
                super.onClosed()
                `//TO-DO`
	}

	override fun onError() {
                super.onError()
                `//TO-DO`
	}
})
```

Load and show Splash ads
```
ProxAds.instance.showSplashMax(activity: Activity, object : AdsCallback() {
	override fun onShow() {
		super.onShow()
                `//TO-DO`
	}

	override fun onClosed() {
                super.onClosed()
                `//TO-DO`
	}

	override fun onError() {
                super.onError()
                `//TO-DO`
	}
}, adId: String, timeout: Int)
```

Native Ads
======================
Load and show native small ads
```
ProxAds.instance.showSmallNativeMaxWithShimmerStyle<select_style>(activity: Activity, adId: String?, adContainer: FrameLayout?, object : AdsCallback() {
	override fun onShow() {
		super.onShow()
                `//TO-DO`
	}

	override fun onClosed() {
                super.onClosed()
                `//TO-DO`
	}

	override fun onError() {
                super.onError()
                `//TO-DO`
	}
})
```

Style native small
Style | Demo | Style | Demo
--- | --- | --- | ---
Native small 15 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Small%20Ad%20Style%2015.png"> | Native small 16 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Small%20Ad%20Style%2016.png">
Native small 21 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Small%20Ad%20Style%2021.png"> | Native small 22 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Small%20Ad%20Style%2022.png">

Load and show native medium ads
```
ProxAds.instance.showMediumNativeMaxWithShimmerStyle<select_style>(activity: Activity, adId: String, adContainer: FrameLayout, object : AdsCallback() {
	override fun onShow() {
		super.onShow()
                `//TO-DO`
	}

	override fun onClosed() {
                super.onClosed()
                `//TO-DO`
	}

	override fun onError() {
                super.onError()
                `//TO-DO`
	}
})
```

Style native medium
Style | Demo | Style | Demo
--- | --- | --- | ---
Native medium 19 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Medium%20Ad%20Style%2019.png"> | Native medium 20 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Medium%20Ad%20Style%2020.png">

Load and show native big ads
```
ProxAds.instance.showBigNativeMaxWithShimmerStyle<select_style>(activity: Activity, adId: String, adContainer: FrameLayout, object : AdsCallback() {
	override fun onShow() {
		super.onShow()
                `//TO-DO`
	}

	override fun onClosed() {
                super.onClosed()
                `//TO-DO`
	}

	override fun onError() {
                super.onError()
                `//TO-DO`
	}
})
```

Style native big
Style | Demo | Style | Demo | Style | Demo
--- | --- | --- | --- | --- | ---
Native big 1 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%201.png"> | Native big 2 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%202.png"> | Native big 3 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%203.png">
Native big 4 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%204.png"> | Native big 5 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%205.png"> | Native big 6 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%206.png">
Native big 7 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%207.png"> | Native big 9 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%209.png"> | Native big 10 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%2010.png">
Native big 11 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%2011.png"> | Native big 12 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%2012.png"> | Native big 13 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%2013.png">
Native big 14 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%2014.png"> |

## Customize all native ads
```
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

Reward Ads
======================
Load reward ads
```
ProxAds.instance.initRewardMax(activity: Activity, adsId: String, tag: String)
```

Show reward ads
```
ProxAds.instance.showRewardMax(
	activity: Activity,
	tag: String,
	object : AdsCallback() {
        	override fun onShow() {
                	`//TO-DO`
             	}

          	override fun onClosed() {
                        `//TO-DO`
           	}

         	override fun onError() {
                        `//TO-DO`
     		}
    	}, object : RewardCallback() {
         	override fun getReward(amount: Int, type: String) {
                        `//TO-DO`
              	}
    	}
)
```

Remote Config Ads
======================
Setting on Remote Config
```
Parameter name (key): config_ads
Data type: String
Default value:
	{
  		"status": true,
  		"status_open_ads": true,
  		"splash": {
    			"status": true,
    			"timeout": 10000
  		},
  		"banners": [
    			{
      				"description": "Banner màn Test Splash và màn Test Interstitial Click 1",
      				"id_show_ads": [
      					"banner_splash",
      					"banner_click_"
      				],
      				"status": true
    			},
    			{
      				"description": "Banner màn Test Interstitial Click 2 và 3",
      				"id_show_ads": [
      					"banner_click_2",
        				"banner_click_3"
      				],
      				"status": false
    			}
  		],
  		"interstitials": [
    			{
      				"description": "Interstitial khi click vào Test Interstitial Click 1 và 3",
      				"id_show_ads": [
        				"inter_click_1"
      				],
      				"count_click": 3,
      				"status": true
    			},
    			{
      				"description": "Interstitial khi click vào Test Interstitial Click 2",
      				"id_show_ads": [
        				"inter_click_2",
        				"inter_click_3"
      				],
      				"count_click": 5,
      				"status": true
    			}
  		],
  		"natives": [
    			{
      				"description": "Native màn Test Splash và màn Test Interstitial Click 1",
      				"id_show_ads": [
        				"native_splash",
        				"native_click_1"
      				],
      				"style": 1,
      				"status": true
    			},
    			{
      				"description": "Native màn Test Interstitial Click 2",
      				"id_show_ads": [
        				"native_click_2"
      				],
      				"style": 15,
      				"status": true
    			},
    			{
      				"description": "Native màn Test Interstitial Click 3",
      				"id_show_ads": [
        				"native_click_3"
      				],
      				"style": 19,
      				"status": true
    			}
  		]
	}
```

Need to add this line to your first Activity to set up ads:
```
ProxAdsConfig.instance.init()
//ProxAds.instance.initInterstitialMax(activity: Activity, adId: String, tag: String)
//...
```

Show Splash Ads
```
ProxAdsConfig.instance.showSplashIfNecessary(activity: Activity, adId: String, callback: AdsCallback)
```

Show Banner Ads
```
ProxAdsConfig.instance.showBannerIfNecessary(activity: Activity, container: FrameLayout, id_show_ads: String, adId: String, callback: AdsCallback)
```

Show Interstitial Ads
```
ProxAdsConfig.instance.showInterstitialIfNecessary(activity: Activity, id_show_ads: String, tag: String, callback: AdsCallback)
```

Show Native Ads
```
ProxAdsConfig.instance.showNativeIfNecessary(activity: Activity, container: FrameLayout, id_show_ads: String, adId: String, callback: AdsCallback)
```
