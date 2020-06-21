package com.maku.calendate.utils

import android.app.ListActivity
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.maku.calendate.R
import com.maku.calendate.receiver.SnoozeReceiver

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

// extension function to send messages (GIVEN)
/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity
    // Step 1. create intent
    val contentIntent = Intent(applicationContext, ListActivity::class.java)
    // Step 2. create PendingIntent,  To make an intent work outside your app, you need to create a new PendingIntent.
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // add style
    val Image = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.calendate
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(Image)
        .bigLargeIcon(null)

    //  add snooze action
    val snoozeIntent = Intent(applicationContext, ListActivity::class.java)
    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        snoozeIntent,
        FLAGS)

    //  get an instance of NotificationCompat.Builder
    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id)
    )

        // set title, text and icon to builder
        .setSmallIcon(R.drawable.calendate)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentText(messageBody)

        //  set content intent
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

        //  add style to builder
        .setStyle(bigPicStyle)
        .setLargeIcon(Image)

        //  add snooze action
        .addAction(
            R.drawable.ic_remove,
            applicationContext.getString(R.string.snooze),
            snoozePendingIntent
        )

        //  set priority
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    // call notify
    notify(NOTIFICATION_ID, builder.build())
}

// Cancel all notifications
/**
 * Cancels all notifications.
 * TO FIX THIS ==> If you set the timer, get a notification, and set the timer again, the previous notification stays on the status bar while the new timer is running.
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}
