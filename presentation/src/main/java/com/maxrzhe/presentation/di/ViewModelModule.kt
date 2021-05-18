package com.maxrzhe.presentation.di

import com.maxrzhe.presentation.viewmodel.impl.ContactDetailViewModel
import com.maxrzhe.presentation.viewmodel.impl.ContactListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<ContactListViewModel> { ContactListViewModel(get(), get()) }
    viewModel<ContactDetailViewModel> { ContactDetailViewModel(get(), get(), get()) }
}