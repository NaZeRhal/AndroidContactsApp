package com.maxrzhe.presentation.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.maxrzhe.presentation.databinding.ActivityVolumeSettingBinding
import com.maxrzhe.presentation.viewmodel.VolumeSettingViewModel

class VolumeSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVolumeSettingBinding
    private val viewModel by viewModels<VolumeSettingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVolumeSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbVolumeSettings)
        binding.viewModel = viewModel

        viewModel.setVolumeValue(50)
    }
}