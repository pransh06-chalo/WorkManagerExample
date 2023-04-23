package com.example.workmanagerexample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay

class ForegroundWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    private val notificationManager = appContext.getSystemService(NotificationManager::class.java)

    private val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
        .setContentTitle("Important background job")

    private var isDownloadCompleted = false

    override suspend fun doWork(): Result {
        Log.e(TAG, "Start job")

        createNotificationChannel()
        val notification = notificationBuilder.build()
        val foregroundInfo = ForegroundInfo(NOTIFICATION_ID, notification)
        setForeground(foregroundInfo)

        /*for (i in 0..100) {
            // we need it to get progress in UI
            setProgress(workDataOf(ARG_PROGRESS to i))
            // update the notification progress
            //showProgress(i)
            delay(DELAY_DURATION)
        }*/
       var counter = 0
       while (isDownloadCompleted.not()){
           //Log.e(TAG, "doWork: isDownloadCompleted value =$isDownloadCompleted")
           counter++
           Log.e(TAG, "doWork: $counter with INFINITE LOOP")
       }

        Log.e(TAG, "Finish job")
        return Result.success()
    }

   /* private suspend fun showProgress(progress: Int) {
        val notification = notificationBuilder
            .setProgress(100, progress, false)
            .build()
        val foregroundInfo = ForegroundInfo(NOTIFICATION_ID, notification)
        setForeground(foregroundInfo)
    }*/

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = notificationManager?.getNotificationChannel(CHANNEL_ID)
            if (notificationChannel == null) {
                notificationManager?.createNotificationChannel(
                    NotificationChannel(
                        CHANNEL_ID, TAG, NotificationManager.IMPORTANCE_LOW
                    )
                )
            }
        }
    }

    companion object {

        const val TAG = "ForegroundWorkerInExample"
        const val NOTIFICATION_ID = 12
        const val CHANNEL_ID = "Job Worker progress"
        const val ARG_PROGRESS = "Progress"
        private const val DELAY_DURATION = 100L // ms
    }
}