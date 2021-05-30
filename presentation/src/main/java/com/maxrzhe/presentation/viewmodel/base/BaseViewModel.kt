package com.maxrzhe.presentation.viewmodel.base

import androidx.lifecycle.ViewModel
import com.maxrzhe.presentation.navigation.Router

abstract class BaseViewModel(val router: Router) : ViewModel()