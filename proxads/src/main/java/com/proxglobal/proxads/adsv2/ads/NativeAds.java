package com.proxglobal.proxads.adsv2.ads;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.facebook.shimmer.ShimmerFrameLayout;

public abstract class NativeAds<T> extends BaseAds<T> {
    protected FrameLayout mContainer;

    protected NativeAds(Activity activity, FrameLayout container, String adId) {
        super(activity, adId);
        this.mContainer = container;
        turnOffAutoReload();
    }

    public void enableShimmer(int shimmerLayoutId) {
        View view = mActivity.getLayoutInflater().inflate(shimmerLayoutId, mContainer);

        ShimmerFrameLayout shimmer;
        if(view instanceof ShimmerFrameLayout) {
            shimmer = (ShimmerFrameLayout) view;
        } else {
            shimmer = new ShimmerFrameLayout(mActivity);
            shimmer.setId(View.generateViewId());
            shimmer.setLayoutParams(
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT));

            mActivity.getLayoutInflater().inflate(shimmerLayoutId, shimmer);
        }

        shimmer.startShimmer();

        mContainer.removeAllViews();
        mContainer.addView(shimmer);
    }
}
