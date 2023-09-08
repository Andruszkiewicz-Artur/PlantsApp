package com.example.plantsapp

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.plantsapp.domain.controller.AlarmController
import com.example.plantsapp.domain.use_case.PlantAlarmUseCases
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PlantsApp: Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: AlarmControllerFactory

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .setWorkerFactory(workerFactory)
            .build()

}

class AlarmControllerFactory @Inject constructor(
    private val useCases: PlantAlarmUseCases,
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat
): WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = AlarmController(useCases, notificationBuilder, notificationManager, appContext, workerParameters)

}
