package com.example.plantsapp.presentation.home

import com.example.plantsapp.domain.model.PlantAlarmModel

sealed class HomeEvent {

    data class ChangeActiveAlarm(val alarm: PlantAlarmModel): HomeEvent()
    data class DeleteAlarm(val alarm: PlantAlarmModel): HomeEvent()

}
