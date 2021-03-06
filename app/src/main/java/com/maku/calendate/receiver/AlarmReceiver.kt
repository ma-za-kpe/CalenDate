package com.maku.calendate.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.maku.calendate.utils.sendNotification


/***
 * AlarmReceiver is triggered by the AlarmManager to send the notification when the user-defined date and time is up
 */
class AlarmReceiver: BroadcastReceiver() {

    private var mNotificationManager: NotificationManager? = null
    private val NOTIFICATION_ID = 0

    // Notification channel ID.
    private val PRIMARY_CHANNEL_ID = "primary_notification_channel"

    override fun onReceive(context: Context, intent: Intent?) {
        mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        deliverNotification(context)
    }

    private fun deliverNotification(context: Context) {
        val notificationManager =
                ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager
            notificationManager.sendNotification("message", context)
    }



}