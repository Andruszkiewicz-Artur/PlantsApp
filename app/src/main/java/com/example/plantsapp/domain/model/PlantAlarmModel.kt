package com.example.plantsapp.domain.model

import android.net.Uri
import java.time.LocalDateTime

data class PlantAlarmModel(
    val id: Int? = null,
    val plantName: String = "",
    val plantDescription: String = "",
    val photo: Uri? = null,
    val isActive: Boolean = false,
    val repeating: Int = 1,
    val basicDate: LocalDateTime = LocalDateTime.now(),
    val nextDate: LocalDateTime = LocalDateTime.now()
)
