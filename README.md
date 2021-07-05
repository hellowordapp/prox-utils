# prox-utils

## implementation
```
dependencies {
	        implementation 'com.github.hellowordapp:prox-utils:Tag'
}
```

## Usage
```sh
//create interstitial
        ProxInterstitialAd inter = ProxAdUtils.INSTANCE.createInterstitialAd (Activity activity, String adId);

//load interstitial
        inter.load();

//show interstitial with Adclose call back
        inter.show(new AdClose() {
            @Override
            public void onAdClose() {
		`//TO-DO`
            }
        });

```

```sh
//create native ads
        ProxAdUtils.INSTANCE.createNativeAd (Activity activity, String adId, FrameLayout adContainer, int layoutAdId);
                .load(new NativeAdCallback() {
                    @Override
                    public void onNativeAdCallback() {
		    	`//TO-DO`
                    }
                });
```


