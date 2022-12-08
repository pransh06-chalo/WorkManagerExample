package com.example.workmanagerexample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class MyWorker2(val context: Context,parameters: WorkerParameters):CoroutineWorker(context,parameters) {

    override suspend fun doWork(): Result {

        showNotification()
        return Result.success()
    }

    private fun showNotification() {

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent =  PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(
            applicationContext,
            MyWorker.CHANNEL_ID
        )
            .setSmallIcon(com.google.android.material.R.drawable.notification_template_icon_bg)
            .setContentTitle("New Task")
            .setContentText("Subscribe on the channel")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelName = "Channel Name"
            val channelDescription = "Channel Description"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {
                description = channelDescription
            }

            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)

            with(NotificationManagerCompat.from(applicationContext)) {
                notify(NOTIFICATION_ID, notification.build())
            }
        }
    }

    companion object{
        const val CHANNEL_ID = "channel_id"
        const val NOTIFICATION_ID = 1
        private const val TAG = "MyWorker"
    }
}