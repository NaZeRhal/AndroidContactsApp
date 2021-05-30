package com.maxrzhe.presentation.ui.impl.contacts

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import com.maxrzhe.presentation.R
import com.maxrzhe.presentation.navigation.listenToRouter
import com.maxrzhe.presentation.ui.base.CoreFragment
import com.maxrzhe.presentation.viewmodel.impl.contacts.SplashViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


class SplashFragment : CoreFragment() {

    val viewModel: SplashViewModel by viewModel()
    override fun layoutId(): Int = R.layout.fragment_splash

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenToRouter(viewModel.router)
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }

    override fun initView() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            viewModel.navigateToHomeFragment()
        }
    }

    private fun hideSystemUI() {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.apply {
                setDecorFitsSystemWindows(false)
                insetsController?.let {
                    it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars() or WindowInsets.Type.systemBars())
                    it.systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
        }
    }
}