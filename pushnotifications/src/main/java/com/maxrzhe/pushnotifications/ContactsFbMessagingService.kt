package com.maxrzhe.pushnotifications

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.maxrzhe.common.util.Constants
import com.maxrzhe.common.util.Resource
import com.maxrzhe.domain.usecases.AddContactAfterPushNotificationUseCase
import com.maxrzhe.presentation.ui.ContactsListActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ContactsFbMessagingService : FirebaseMessagingService() {

    private val addAfterNotificationUseCase: AddContactAfterPushNotificationUseCase by inject()
    private val notificationManager: ContactNotificationManager by inject()
    private var fbId = MutableLiveData<String?>()

    override fun onNewToken(refreshedToken: String) {
        Log.i("DBG", "Refreshed token: $refreshedToken")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        CoroutineScope(Dispatchers.IO).launch {
            remoteMessage.notification?.let {
                if (remoteMessage.data.isNotEmpty()) {
                    saveAndSendNotification(remoteMessage.data, it.title, it.body)
                } else {
                    sendNotification(it.title, it.body)
                }
            }
        }
    }

    private suspend fun saveAndSendNotification(
        data: Map<String, String>,
        title: String?,
        message: String?
    ) {
        addAfterNotificationUseCase.execute(data).collect {
            when (it) {
                is Resource.Success -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        fbId.value = it.data?.fbId
                        sendNotification(title, "$message. Contact added to local database")
                    }
                }
                is Resource.Error -> {
                    sendErrorNotification(it.error.toString())
                }
            }
        }
    }

    private fun sendNotification(title: String?, message: String?) {
        val intent = Intent(this, ContactsListActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(Constants.FB_ID_FCM, fbId.value)
        }
        notificationManager.createNotification(title, message, intent)
    }

    private fun sendErrorNotification(message: String?) {
        notificationManager.createNotification(
            ContactNotificationManager.ERROR_NOTIFICATION_TITLE,
            message,
            null
        )
    }
}