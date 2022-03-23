package com.proxglobal.proxads.adsv2.base;

import android.app.Activity;
import android.content.Context;

public abstract class BaseInterAds extends BaseAds {
    protected BaseInterAds(Activity activity) {
        super(activity);
    }

    public final void turnOnAutoReload() {
        this.autoReload = true;
    }

    public final void turnOffAutoReload() {
        this.autoReload = false;
    }
}
