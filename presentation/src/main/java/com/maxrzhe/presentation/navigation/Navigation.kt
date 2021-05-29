package com.maxrzhe.presentation.navigation

import androidx.navigation.NavOptions
import com.maxrzhe.presentation.R

val defaultNavOptions: NavOptions
    get() {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in)
            .setExitAnim(R.anim.fade_out)
            .setPopEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.slide_out)
            .build()
    }