package com.maxrzhe.pushnotifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.maxrzhe.domain.usecases.AddContactUseCase
import com.maxrzhe.presentation.navigation.RouteSection
import com.maxrzhe.presentation.ui.ContactsListActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ContactsFbMessagingService : FirebaseMessagingService() {

    private val addUseCase: AddContactUseCase by inject()

    override fun onNewToken(refreshedToken: String) {
        Log.i("DBG", "Refreshed token: $refreshedToken")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("DBG", "${remoteMessage.data}")

            if (remoteMessage.data.isNotEmpty()) {
                val contact =
            }

            remoteMessage.notification?.let {
                createNotification(it.title, it.body)
                Log.i("DBG", "Message title: ${it.title}")
                Log.i("DBG", "Message title: ${it.body}")
            }
        }
    }

    private fun createNotification(title: String?, message: String?) {
        createNotificationChannel()

        val intent = Intent(this, ContactsListActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notification =
            NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.ic_baseline_person_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.GREEN)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build()

        with(NotificationManagerCompat.from(this)) {
            notify(1, notification)
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val desc = getString(R.string.channel_desc)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channelId = getString(R.string.default_notification_channel_id)
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = desc
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}