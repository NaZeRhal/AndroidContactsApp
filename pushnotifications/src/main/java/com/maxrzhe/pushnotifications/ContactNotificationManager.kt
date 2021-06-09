package com.maxrzhe.pushnotifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.maxrzhe.presentation.ui.ContactsListActivity

class ContactNotificationManager(private val context: Context) {

    companion object {
        const val ERROR_NOTIFICATION_TITLE = "Error notification"
    }

    fun createNotification(title: String?, message: String?, intent: Intent?) {
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification =
            NotificationCompat.Builder(context, ContactsListActivity.CHANNEL_ID)
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

        with(NotificationManagerCompat.from(context)) {
            notify(1, notification)
        }
    }
}