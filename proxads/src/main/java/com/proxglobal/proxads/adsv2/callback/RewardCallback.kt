package com.proxglobal.proxads.adsv2.callback

import com.google.android.gms.ads.rewarded.RewardItem

abstract class RewardCallback {
    open fun getReward(rewardItem: RewardItem) {}
}