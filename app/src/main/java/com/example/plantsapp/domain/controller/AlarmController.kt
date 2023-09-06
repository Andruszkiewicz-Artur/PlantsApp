package com.example.plantsapp.domain.controller

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.plantsapp.domain.use_case.PlantAlarmUseCases
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDateTime
import javax.inject.Inject

@HiltWorker
class AlarmController @AssistedInject constructor(
    private val useCases: PlantAlarmUseCases,
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

            data = data.filter {
                it.nextDate <= today
            }.map {
                it.copy(
                    basicDate = today,
                    nextDate = today.plusDays(it.repeating.toLong()),
                    isWatering = false
                )
            }

            data.forEach {
                useCases.upsertPlantAlarmUseCase.invoke(it)
                Log.d(TAG, "update: $it")
            }

            Result.success()
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
            Result.failure()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return super.getForegroundInfo()
    }
}