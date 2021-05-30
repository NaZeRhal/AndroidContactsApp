package com.maxrzhe.presentation.viewmodel.impl

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.maxrzhe.presentation.navigation.Router
import com.maxrzhe.presentation.viewmodel.base.BaseViewModel

class VolumeSettingViewModel(router: Router) : BaseViewModel(router) {

    val volumeValue = ObservableInt(0)
    val currentVolume = object : ObservableField<String>(volumeValue) {
        override fun get(): String {
            return "current volume ${volumeValue.get()} %"
        }
    }

    fun setVolumeValue(value: Int) {
        volumeValue.set(value)
    }
}