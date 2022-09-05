package com.proxglobal.proxads.adsv2.callback

abstract class RewardCallback {
    open fun getReward(amount: Int, type: String) {}
}