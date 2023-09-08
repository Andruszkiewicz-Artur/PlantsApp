package com.example.plantsapp.domain.controller

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.plantsapp.core.Static.NOTIFICATION_ID
import com.example.plantsapp.domain.use_case.PlantAlarmUseCases
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDateTime

@OptIn(ExperimentalAnimationApi::class)
@HiltWorker
class AlarmController @AssistedInject constructor(
    private val useCases: PlantAlarmUseCases,
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat,
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {

    companion object {
        private const val TAG = "AlarmController"
    }

    override suspend fun doWork(): Result {
        return try {
            var data = useCases.getAllPlantAlarmUseCase.invoke()

            val currentDate = LocalDateTime.now()
            val today = LocalDateTime.of(currentDate.year, currentDate.monthValue, currentDate.dayOfMonth, 23, 59, 59)

            val newData = data.filter {
                it.nextDate <= today
            }.map {
                it.copy(
                    basicDate = today,
                    nextDate = today.plusDays(it.repeating.toLong()),
                    isWatering = false
                )
            }

            newData.forEach {
                useCases.upsertPlantAlarmUseCase.invoke(it)
            }

            Log.d(TAG, "Start")
            if (data.any { !it.isWatering && it.basicDate <= today}) {
                Log.d(TAG, "Work")
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
            }

            Result.success()
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
            Result.failure()
        }
    }
}