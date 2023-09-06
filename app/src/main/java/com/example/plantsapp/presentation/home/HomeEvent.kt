package com.example.plantsapp.presentation.home

import com.example.plantsapp.domain.model.PlantAlarmModel

sealed class HomeEvent {

    data class ChangeActiveAlarm(val alarm: PlantAlarmModel): HomeEvent()
    data class DeleteAlarm(val alarm: PlantAlarmModel): HomeEvent()
    data class AddAlarm(val alarm: PlantAlarmModel): HomeEvent()
    data class ClickCheckBox(
        val checked: Boolean,
        val plant: PlantAlarmModel
    ): HomeEvent()

    object ShowDialog: HomeEvent()
    object HideDialog: HomeEvent()
    object CheckAll: HomeEvent()

}
