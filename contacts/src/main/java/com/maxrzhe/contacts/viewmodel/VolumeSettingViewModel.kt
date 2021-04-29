package com.maxrzhe.contacts.viewmodel

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel

class VolumeSettingViewModel : ViewModel() {

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