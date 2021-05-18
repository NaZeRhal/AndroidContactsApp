package com.maxrzhe.presentation.util

import android.content.Context
import android.content.res.Resources
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

class AppResourcesImpl(private val appContext: Context) : AppResources {

    private val resources: Resources = appContext.resources

    @ColorInt
    override fun getColor(@ColorRes colorId: Int): Int =
        // resources.getColor(colorId) DEPRECATED
        ContextCompat.getColor(appContext, colorId)
}