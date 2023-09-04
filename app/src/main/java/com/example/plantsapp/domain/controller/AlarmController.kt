package com.example.plantsapp.domain.controller

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class AlarmController(
    context: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {

    companion object {
        private const val TAG = "AlarmController"
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "success")
        return Result.success()
    }

}