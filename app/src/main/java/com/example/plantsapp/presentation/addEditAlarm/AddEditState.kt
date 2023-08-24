package com.example.plantsapp.presentation.addEditAlarm

import com.example.plantsapp.domain.model.PlantAlarmModel
import java.io.File
import java.util.concurrent.ExecutorService

data class AddEditState(
    val basicAlarm: PlantAlarmModel = PlantAlarmModel(),
    val alarmModel: PlantAlarmModel,
    val showCamera: Boolean = false,
    val outputDirectory: File,
    val cameraExecutor: ExecutorService
    )
