package com.maxrzhe.presentation.di

import com.maxrzhe.presentation.util.AppResources
import com.maxrzhe.presentation.util.AppResourcesImpl
import com.maxrzhe.presentation.viewmodel.impl.ContactDetailViewModel
import com.maxrzhe.presentation.viewmodel.impl.ContactListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single<AppResources> { AppResourcesImpl(androidContext()) }

    viewModel<ContactListViewModel> { ContactListViewModel(get(), get(), get()) }
    viewModel<ContactDetailViewModel> { ContactDetailViewModel(get(), get(), get(), get()) }

}