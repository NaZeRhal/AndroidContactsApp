package com.maxrzhe.presentation.util

import android.content.Context
import android.content.res.Resources
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

class AppResourcesImpl(private val appContext: Context) : AppResources {

    private val resources: Resources = appContext.resources

    @ColorInt
    override fun getColor(@ColorRes colorId: Int): Int =
        ContextCompat.getColor(appContext, colorId)

    override fun getQuantityString(@PluralsRes plurals: Int, resultCount: Int): String =
        resources.getQuantityString(plurals, resultCount, resultCount)

    override fun getString(@StringRes stringId: Int): String = resources.getString(stringId)

}