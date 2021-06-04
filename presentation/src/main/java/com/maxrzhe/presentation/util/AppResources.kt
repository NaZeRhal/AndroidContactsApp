package com.maxrzhe.presentation.util

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

interface AppResources {
    @ColorInt
    fun getColor(@ColorRes colorId: Int): Int

    fun getQuantityString(@PluralsRes plurals: Int, resultCount: Int): String

    fun getString(@StringRes stringId: Int): String
}