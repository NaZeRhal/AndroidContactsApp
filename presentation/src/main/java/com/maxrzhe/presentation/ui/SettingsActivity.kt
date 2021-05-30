package com.maxrzhe.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.maxrzhe.presentation.databinding.ActivitySettingsBinding
import com.maxrzhe.presentation.navigation.listenToRouterOnNavHost
import com.maxrzhe.presentation.viewmodel.impl.settings.SettingsListViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    private val viewModel: SettingsListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(binding.root)
        listenToRouterOnNavHost(viewModel.router)
    }
}