package com.example.plantsapp.presentation

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.plantsapp.core.unit.navigation.RootNavHost
import com.example.plantsapp.domain.controller.AlarmController
import com.example.plantsapp.ui.theme.PlantsAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPermissionsApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlantsAppTheme {
                val listOfPermission = mutableListOf(Manifest.permission.CAMERA)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) listOfPermission.add(Manifest.permission.FOREGROUND_SERVICE)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) listOfPermission.addAll(listOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.POST_NOTIFICATIONS
                ) )

                val permissions = rememberMultiplePermissionsState(permissions = listOfPermission)

                LaunchedEffect(key1 = Unit) {
                    if(!permissions.allPermissionsGranted) {
                        permissions.launchMultiplePermissionRequest()

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) setUpWorker()

                    } else {
                        setUpWorker()
                    }
                }

                RootNavHost()
            }
        }
    }

    private fun setUpWorker() {
        val workRequest = PeriodicWorkRequestBuilder<AlarmController>(
            repeatInterval = 6,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.LINEAR,
            duration = Duration.ofSeconds(15)
        ).build()

        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueueUniquePeriodicWork(
            "AlarmController",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}