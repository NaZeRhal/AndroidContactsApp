package com.maxrzhe.domain.di

import com.maxrzhe.domain.usecases.*
import org.koin.dsl.module

val domainModule = module {
    factory { GetContactsUseCase(get()) }
    factory { FindByIdUseCase(get()) }
    factory { AddContactUseCase(get()) }
    factory { UpdateContactUseCase(get()) }
    factory { DeleteContactUseCase(get()) }
    factory { AddContactAfterPushNotificationUseCase(get()) }
}