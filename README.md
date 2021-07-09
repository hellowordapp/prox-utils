# prox-utils

## implementation
```
dependencies {
	implementation 'com.github.hellowordapp:prox-utils:0.0.3'
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

