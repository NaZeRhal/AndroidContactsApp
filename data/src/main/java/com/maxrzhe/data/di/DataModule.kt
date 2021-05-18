package com.maxrzhe.data.di

import android.content.Context
import androidx.room.Room
import com.example.data_api.repositories.ContactRepository
import com.maxrzhe.common.util.Constants
import com.maxrzhe.data.local.ContactDatabase
import com.maxrzhe.data.local.ContactDatabaseImpl
import com.maxrzhe.data.local.room.ContactDao
import com.maxrzhe.data.local.room.ContactRoomDatabase
import com.maxrzhe.data.remote.ContactApi
import com.maxrzhe.data.remote.ContactApiImpl
import com.maxrzhe.data.remote.ContactService
import com.maxrzhe.data.repository.ContactRepositoryImpl
import com.maxrzhe.domain.usecases.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<ContactService> { createContactService() }
    single<ContactDao> { createContactDao(androidContext()) }

    single<ContactApi> { ContactApiImpl(get()) }
    single<ContactDatabase> { ContactDatabaseImpl(get()) }
    single<ContactRepository> { ContactRepositoryImpl(get(), get()) }

    factory { GetContactsUseCase(get()) }
    factory { FindByIdUseCase(get()) }
    factory { AddContactUseCase(get()) }
    factory { UpdateContactUseCase(get()) }
    factory { DeleteContactUseCase(get()) }
}

fun createContactService(): ContactService {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(ContactService::class.java)
}

fun createContactDao(context: Context): ContactDao {
    val db = Room.databaseBuilder(
        context,
        ContactRoomDatabase::class.java,
        Constants.DATABASE_NAME
    ).build()
    return db.contactDao()
}