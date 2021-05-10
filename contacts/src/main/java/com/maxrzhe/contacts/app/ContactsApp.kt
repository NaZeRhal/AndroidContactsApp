package com.maxrzhe.contacts.app

import android.app.Application
import com.maxrzhe.contacts.dao.ContactDao
import com.maxrzhe.contacts.database.ContactRoomDatabase
import com.maxrzhe.contacts.remote.ContactsApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ContactsApp : Application() {

    lateinit var contactsApi: ContactsApi
    lateinit var contactDao: ContactDao

    override fun onCreate() {
        super.onCreate()
        configureRetrofit()
        configureRoom()
    }

    private fun configureRoom() {
        contactDao = ContactRoomDatabase.getInstance(this).contactDao()
    }

    private fun configureRetrofit() {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://contacts-app-ef170-default-rtdb.europe-west1.firebasedatabase.app/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        contactsApi = retrofit.create(ContactsApi::class.java)
    }
}