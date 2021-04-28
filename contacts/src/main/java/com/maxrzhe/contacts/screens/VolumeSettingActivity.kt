package com.maxrzhe.contacts.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.maxrzhe.contacts.databinding.ActivityVolumeSettingBinding

class VolumeSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVolumeSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVolumeSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbVolumeSettings)
    }
}