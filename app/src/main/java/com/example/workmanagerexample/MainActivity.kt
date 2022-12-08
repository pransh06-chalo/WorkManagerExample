package com.example.workmanagerexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.*
import com.example.workmanagerexample.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.oneTimeBtn.setOnClickListener {
            setupOneTimeWorker()
        }

        binding.periodicTimeBtn.setOnClickListener {
            setupPeriodicWorker()
        }

        binding.cancelTasks.setOnClickListener {

            cancelAllTasks()
        }
    }

    private fun cancelAllTasks() {

        WorkManager.getInstance(this).cancelAllWorkByTag(PERIODIC_TASK_TAG)
    }

    private fun setupPeriodicWorker() {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(true)
            .build()

        val request = PeriodicWorkRequestBuilder<MyWorker>(15, TimeUnit.MINUTES)
                // Additional configuration
            .setConstraints(constraints)
            .addTag(PERIODIC_TASK_TAG)
            .build()

        WorkManager.getInstance(this).enqueue(request)

        /*  WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "my_id",
                ExistingPeriodicWorkPolicy.KEEP,
                myRequest
            )*/
    }

    private fun setupOneTimeWorker() {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(true)
            .build()

        val request: WorkRequest = OneTimeWorkRequestBuilder<MyWorker>().setConstraints(constraints).build()

        WorkManager.getInstance(this).enqueue(request)
    }

    companion object{
        private const val TAG = "MainActivity"
        const val PERIODIC_TASK_TAG = "PERIODIC_WORKER"
    }
}