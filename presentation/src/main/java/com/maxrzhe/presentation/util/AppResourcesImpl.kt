package com.maxrzhe.presentation.util

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

class AppResourcesImpl(private val appContext: Context) : AppResources {

    @ColorInt
    override fun getColor(@ColorRes colorId: Int): Int =
        ContextCompat.getColor(appContext, colorId)
}