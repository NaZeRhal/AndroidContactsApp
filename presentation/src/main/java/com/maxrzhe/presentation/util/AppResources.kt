package com.maxrzhe.presentation.util

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

interface AppResources {
    @ColorInt
    fun getColor(@ColorRes colorId: Int): Int
}