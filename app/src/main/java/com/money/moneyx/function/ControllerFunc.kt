package com.money.moneyx.function

import android.app.Activity
import android.graphics.Color
import com.iamauttamai.avloading.AVLoadingIndicatorView.TypeIndicator
import com.iamauttamai.avloading.ui.AVLoading

fun loadingScreen(mContext: Activity, ) {
    AVLoading.loadingParamsBuilder(
        mContext,
        TypeIndicator.BallScaleIndicator, Color.parseColor("#FF8686")
    )
    AVLoading.initializeAVLoading()
}
