package com.proxglobal.proxads.adsv2.ads

import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import com.facebook.shimmer.ShimmerFrameLayout

abstract class NativeAds<T> protected constructor(
    activity: Activity?,
    protected var mContainer: FrameLayout?,
    adId: String?
) : BaseAds<T>(
    activity!!, adId!!
) {

    init {
        turnOffAutoReload()
    }

    protected var shimmer: ShimmerFrameLayout? = null
    fun enableShimmer(shimmerLayoutId: Int) {
        val view = mActivity.layoutInflater.inflate(shimmerLayoutId, mContainer)
        if (view is ShimmerFrameLayout) {
            shimmer = view
        } else {
            shimmer = ShimmerFrameLayout(mActivity)
            shimmer!!.id = View.generateViewId()
            shimmer!!.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            mActivity.layoutInflater.inflate(shimmerLayoutId, shimmer)
        }
        shimmer!!.startShimmer()
        mContainer!!.removeAllViews()
        mContainer!!.addView(shimmer)
    }
}