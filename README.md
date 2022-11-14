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
	implementation "prox-lib:prox-utils-max:2.4.4"
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

If you use Ad Review, add the following to your build.gradle files:
```
//Additions to Root-Level build.gradle File
buildscript {
    repositories {
        maven { url 'https://artifacts.applovin.com/android' }
    }
    dependencies {
        classpath "com.applovin.quality:AppLovinQualityServiceGradlePlugin:+"
    }
}

//Additions to App-Level build.gradle File
apply plugin: 'applovin-quality-service'
applovin {
       apiKey "YOUR_AD_REVIEW_KEY_HERE"
}
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
ProxUtils.INSTANCE.initFirebaseRemoteConfig(
	activity = this,
	appVersionCode = BuildConfig.VERSION_CODE,
	isDebug = BuildConfig.DEBUG,
	iconAppId = R.drawable.ic_app,
	appName = getString(R.string.app_name)
)
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
ProxSurveyConfig().showSurveyIfNecessary(
	activity = this,
	isDebug = BuildConfig.DEBUG
)
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
ProxRateDialog.init()
ProxRateDialog.setConfig(config)
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
AppOpenManager.instance.disableOpenAds()/AppOpenManager.instance.enableOpenAds()
```

Banner Ads
======================
```
ProxAds.instance.showBannerAds(
	activity: Activity,
	container: FrameLayout,
	adId: String,
	callback: AdsCallback
)
```

Interstitial Ads
======================
Load interstitial ads
```
ProxAds.instance.initInterstitialAds(
	activity: Activity,
	adId: String,
	numberReload: Int = 1
)
```

Show interstitial ads
```
ProxAds.instance.showInterstitialAds(
	activity: Activity,
	adsId: String,
	callback: AdsCallback
)
```

Load and show Splash ads
```
ProxAds.instance.showSplashAds(
	activity: Activity,
	adsId: String,
	timeout: Int,
	callback: AdsCallback
)
```

Native Ads
======================
Load and show native ads
```
ProxAds.instance.showNativeAds(
	activity: Activity,
	container: FrameLayout,
	adId: String,
	@NativeStyle style: Int,
	callback: AdsCallback, 
	numberReload: Int = 1
)
```

Style native small
Style | Demo | Style | Demo
--- | --- | --- | ---
SMALL_15 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Small%20Ad%20Style%2015.png"> | SMALL_16 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Small%20Ad%20Style%2016.png">
SMALL_21 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Small%20Ad%20Style%2021.png"> | SMALL_22 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Small%20Ad%20Style%2022.png">

Style native medium
Style | Demo | Style | Demo
--- | --- | --- | ---
MEDIUM_19 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Medium%20Ad%20Style%2019.png"> | MEDIUM_20 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Medium%20Ad%20Style%2020.png">

Style native big
Style | Demo | Style | Demo | Style | Demo
--- | --- | --- | --- | --- | ---
BIG_1 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%201.png"> | BIG_2 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%202.png"> | BIG_3 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%203.png">
BIG_4 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%204.png"> | BIG_5 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%205.png"> | BIG_6 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%206.png">
BIG_7 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%207.png"> | BIG_9 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%209.png"> | BIG_10 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%2010.png">
BIG_11 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%2011.png"> | BIG_12 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%2012.png"> | BIG_13 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%2013.png">
BIG_14 | <img src="https://github.com/ntduc-let/image_readme_github/blob/master/Native%20Big%20Ad%20Style%2014.png"> |

Load and show custom native ads
```
ProxAds.instance.showCustomNative(
	activity: Activity,
	container: FrameLayout,
	@LayoutRes layoutAdId: Int,
	adId: String,
	callback: AdsCallback, 
	numberReload: Int = 1
)
```

Load native ads but not showing immediately
```
//Load Native
ProxAds.instance.initNativeAds(
        activity: Activity,
        adsId: String,
        @NativeStyle style: Int,
        preloads: Int = 1
)

//Load Custom Native
ProxAds.instance.initCustomNativeAds(
        activity: Activity,
        @LayoutRes layoutAdId: Int,
        adsId: String,
        preloads: Int = 1
)

//Show
ProxAds.instance.showNativeAds(
	activity: Activity,
	container: FrameLayout,
	callback: AdsCallback
)

//Remove
ProxAds.instance.removeNativeAds(adId: String)
```

Reward Ads
======================
Load reward ads
```
ProxAds.instance.initRewardAds(
	activity: Activity,
	adsId: String,
	numberReload: Int = 1
)
```

Show reward ads
```
ProxAds.instance.showRewardAds(
	activity: Activity,
	adsId: String,
	callback: AdsCallback,
	orewardCallback: RewardCallback
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
  "status_open_ads": false,
  "splash": {
    "status": false,
    "timeout": 10000
  },
  "banners": [
    {
      "description": "Banner Screen 1",
      "id_show_ads": [
      	"id_banner_screen_1"
      ],
      "status": true
    },
    {
      "description": "Banner Screen 2, 3",
      "id_show_ads": [
      	"id_banner_screen_2",
        "id_banner_screen_3"
      ],
      "status": true
    },
    {
      "description": "Banner Screen 4",
      "id_show_ads": [
      	"id_banner_screen_4"
      ],
      "status": false
    },
    {
      "description": "Banner Screen 5, 6",
      "id_show_ads": [
      	"id_banner_screen_5",
        "id_banner_screen_6"
      ],
      "status": false
    }
  ],
  "interstitials": [
    {
      "description": "Interstitial Screen 1",
      "id_show_ads": [
        "id_inter_screen_1"
      ],
      "count_click": 3,
      "first_show": false,
      "status": true
    },
    {
      "description": "Interstitial Screen 2, 3",
      "id_show_ads": [
        "id_inter_screen_2",
        "id_inter_screen_3"
      ],
      "count_click": 5,
      "first_show": true,
      "status": true
    }
  ],
  "natives": [
    {
      "description": "Native Screen 1",
      "id_show_ads": [
        "id_native_screen_1"
      ],
      "style": 1,
      "status": true
    },
    {
      "description": "Native Screen 2, 3",
      "id_show_ads": [
        "id_native_screen_2",
        "id_native_screen_3"
      ],
      "style": 15,
      "status": true
    },
    {
      "description": "Native Screen 4",
      "id_show_ads": [
        "id_native_screen_4"
      ],
      "style": 19,
      "status": false
    },
    {
      "description": "Native Screen 5, 6",
      "id_show_ads": [
        "id_native_screen_5",
        "id_native_screen_6"
      ],
      "style": 19,
      "status": true
    }
  ],
  "rewards": [
    {
      "description": "Reward Screen 4",
      "id_show_ads": [
        "id_reward_screen_4"
      ],
      "count_click": 3,
      "first_show": false,
      "status": true
    },
    {
      "description": "Reward Screen 5, 6",
      "id_show_ads": [
        "id_reward_screen_5",
        "id_reward_screen_6"
      ],
      "count_click": 5,
      "first_show": true,
      "status": true
    }
  ]
}
```

Need to add this line to your first Activity to set up ads:
```
ProxAdsConfig.instance.init()
//ProxAds.instance.initInterstitialAds(activity: Activity, adId: String)
//...
```

Show Splash Ads
```
ProxAdsConfig.instance.showSplashAds(
	activity: Activity,
	adId: String,
	callback: AdsCallback
)
```

Show Banner Ads
```
ProxAdsConfig.instance.showBannerAds(
	activity: Activity,
	container: FrameLayout,
	id_show_ads: String,
	adId: String,
	callback: AdsCallback
)
```

Show Interstitial Ads
```
ProxAdsConfig.instance.showInterstitialAds(
	activity: Activity,
	id_show_ads: String,
	adsId: String,
	callback: AdsCallback
)
```

Show Native Ads
```
ProxAdsConfig.instance.showNativeAds(
	activity: Activity,
	container: FrameLayout,
	id_show_ads: String,
	adId: String,
	callback: AdsCallback,
	numberReload: Int = 1,
	@LayoutRes layoutCustomAdId: Int? = null
)
```

Show Reward Ads
```
ProxAdsConfig.instance.showRewardAds(
	activity: Activity,
	id_show_ads: String,
	adsId: String,
	callback: AdsCallback,
	rewardCallback: RewardCallback
)
```
