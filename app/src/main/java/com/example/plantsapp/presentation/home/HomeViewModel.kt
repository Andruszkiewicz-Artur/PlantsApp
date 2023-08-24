package com.example.plantsapp.presentation.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantsapp.domain.use_case.PlantAlarmUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val plantAlarmUseCases: PlantAlarmUseCases
): ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        plantAlarmUseCases.getAllPlantAlarmUseCase().onEach { plantsAlarms ->
            _state.update { it.copy(
                plantsAlarm = plantsAlarms
            ) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.ChangeActiveAlarm -> {
                viewModelScope.launch {
                    plantAlarmUseCases.upsertPlantAlarmUseCase.invoke(event.alarm.copy(
                        isActive = !event.alarm.isActive
                    ))

                    _state.update { it.copy(
                        plantsAlarm = it.plantsAlarm.map {
                            if (it == event.alarm) {
                                it.copy(
                                    isActive = it.isActive.not()
                                )
                            } else {
                                it
                            }
                        }
                    ) }
                }
            }
            is HomeEvent.DeleteAlarm -> {
                viewModelScope.launch {
                    plantAlarmUseCases.deletePlantAlarmUseCase.invoke(event.alarm)
                    _state.update { it.copy(
                        plantsAlarm = it.plantsAlarm.minus(event.alarm)
                    ) }
                }
            }
            is HomeEvent.AddAlarm -> {
                viewModelScope.launch {
                    plantAlarmUseCases.upsertPlantAlarmUseCase.invoke(event.alarm)
                    _state.update { it.copy(
                        plantsAlarm = it.plantsAlarm.plus(event.alarm)
                    ) }
                }
            }
        }
    }
}