package com.maxrzhe.presentation.util

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.PluralsRes

interface AppResources {
    @ColorInt
    fun getColor(@ColorRes colorId: Int): Int

    fun getQuantityString(@PluralsRes plurals: Int, resultCount: Int): String
}