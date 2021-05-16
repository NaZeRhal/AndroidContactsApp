package com.maxrzhe.presentation.adapters

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.max

class ZoomOutPageTransformer : ViewPager.PageTransformer {
    private val minScale = 0.85f
    private val minAlpha = 0.5f

    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            val pageHeight = height
            when {
                position < -1 -> {
                    alpha = 0f
                }
                position <= 1 -> {
                    val scaleFactor = max(minScale, 1 - Math.abs(position))
                    val vertMargin = pageHeight * (1 - scaleFactor) / 2
                    val horzMargin = pageWidth * (1 - scaleFactor) / 2
                    translationX = if (position < 0) {
                        horzMargin - vertMargin / 2
                    } else {
                        horzMargin + vertMargin / 2
                    }

                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    alpha = (minAlpha +
                            (((scaleFactor - minScale) / (1 - minScale)) * (1 - minAlpha)))
                }
                else -> {
                    alpha = 0f
                }
            }
        }
    }
}