package com.proxglobal.proxads.adsv2.callback

abstract class AdsCallback {
    open fun onShow() {}
    open fun onClosed() {}
    open fun onError() {}
}