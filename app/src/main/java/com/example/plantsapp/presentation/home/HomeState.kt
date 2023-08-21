package com.example.plantsapp.presentation.home

import com.example.plantsapp.domain.model.PlantAlarmModel

data class HomeState(
    val plantsAlarm: List<PlantAlarmModel> = emptyList()
)
