package com.example.workmanagerexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.*
import com.example.workmanagerexample.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //disable darktheme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

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

        //PeriodicWorkRequest, as the name suggests is used to run tasks that need to be called periodically(time to time) until cancelled.
        val request = PeriodicWorkRequestBuilder<MyWorker2>(15, TimeUnit.MINUTES)                                   //The first execution happens immediately after the mentioned constraints are met and the next execution occurs after the mentioned period of time

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

        //Constraints in WorkManager are specifications about the requirements that must be met
        // before the work request is executed. For our use case, if we need to show notification to
        // our users, having network availability is not required.

        //One work request can have multiple constraints defined on it. In case of multiple constraints, the work is executed only when all the constraints are met. If any constraint is not satisfied, the work is stopped and resumed only when that constraint is met.

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(true)
            .build()

        //OneTimeWorkRequest is a concrete implementation of the WorkRequest class which is used to run WorkManager tasks that are to be executed once
        val request: WorkRequest = OneTimeWorkRequestBuilder<MyWorker2>().setConstraints(constraints).build()

        //In order to run this work request, we need to call enqueue() method on an instance of WorkManager and pass this WorkRequest as shown below:
        WorkManager.getInstance(this).enqueue(request)      //enqueue()- method enqueues one or more WorkRequests to be run in the background.
    }

    companion object{
        private const val TAG = "MainActivity"
        const val PERIODIC_TASK_TAG = "PERIODIC_WORKER"
    }
}