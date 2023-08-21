package com.example.plantsapp.domain.model

import android.net.Uri

data class PlantAlarmModel(
    val id: Int? = null,
    val plantName: String = "",
    val photo: Uri? = null,
    val plantDescription: String = "",
    val alarmTime: Int = 1,
    val isActive: Boolean = false
)
