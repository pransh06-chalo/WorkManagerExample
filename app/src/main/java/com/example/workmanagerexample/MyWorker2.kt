package com.example.workmanagerexample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.*

class MyWorker2(val context: Context,parameters: WorkerParameters):CoroutineWorker(context,parameters) {

    private var isDownloadCompleted = false

    /*override suspend fun doWork(): Result {
        showNotification()
        return Result.success()
    }*/

    //todo Unlike Worker, CoroutineWorker code does not run on the Executor specified in your Configuration. Instead, it defaults to Dispatchers.Default - this default dispatcher is used for CPU intensive tasks, any thing that takes too long to run on the main thread should run on the default
    override suspend fun doWork(): Result {

        //showNotification()

        setForeground(createForegroundInfo("Hello Testing..."))     //for long running work

        Log.e(TAG, "doWork: isDownloadCompleted.not() value = before loop = ${isDownloadCompleted.not()}")
        var counter = 0
        while (isDownloadCompleted.not()){
            //Log.e(TAG, "doWork: isDownloadCompleted value =$isDownloadCompleted")
            counter++
            Log.e(TAG, "doWork: $counter with INFINITE LOOP")
            //runEvery2mins(counter)

           /* Timer().schedule(120000) {
                Log.e(TAG, "doWork: $counter with INFINITE LOOP after 60sec")
            }*/
        }

        //runEvery2minsNew()
        //setForeground(createForegroundInfo("Hello Testing..."))
        return Result.success()
    }

    private fun createForegroundInfo(progress: String): ForegroundInfo {
        val channelId = CHANNEL_ID
        val title = "New Discoverer Notification"
        val cancel = "Cancel Notification"
        // This PendingIntent can be used to cancel the worker
        val intent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(getId())

        // Create a Notification channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(progress)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setOngoing(true)
            // Add the cancel action to the notification which can
            // be used to cancel the worker
            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()

        return ForegroundInfo(NOTIFICATION_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = "Channel Name"
        val descriptionText = "Channel Description"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channelId = CHANNEL_ID
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun foreverWhileLoop(){
        var counter = 0
        while (isDownloadCompleted.not()){
            counter++
            Log.e(TAG, "doWork: $counter with INFINITE LOOP")
            //runEvery2mins(counter)
        }
    }

   /* private fun runEvery2mins(counter: Int) {
        Log.e(TAG, "runEvery2mins: with counter value = $counter")
        Handler(Looper.getMainLooper()).postDelayed({
            Log.e(TAG, "inside postDelayed: $counter")
            showNotification()
        }, 120000)
    }*/

    private fun runEvery2minsNew() {
        Handler(Looper.getMainLooper()).postDelayed({
            //code
            foreverWhileLoop()
            showNotification()
        }, 120000)  //2mins delay
    }

    private fun showNotification() {

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent =  PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(
            applicationContext,
            CHANNEL_ID
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