package com.example.plantsapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantsapp.domain.use_case.PlantAlarmUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val plantAlarmUseCases: PlantAlarmUseCases,
): ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            plantAlarmUseCases.getAllPlantAlarmFlowUseCase.invoke().onEach { plantAlarms ->
                _state.update { it.copy(
                    plantsAlarm = plantAlarms
                ) }

                updatePlantsForToday()
            }.launchIn(viewModelScope)
        }
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
                            if (it == event.alarm) it.copy(isActive = it.isActive.not())
                            else it
                        }
                    ) }

                    updatePlantsForToday()
                }
            }
            is HomeEvent.DeleteAlarm -> {
                viewModelScope.launch {
                    plantAlarmUseCases.deletePlantAlarmUseCase.invoke(event.alarm)
                    _state.update { it.copy(
                        plantsAlarm = it.plantsAlarm.minus(event.alarm)
                    ) }

                    updatePlantsForToday()
                }
            }
            is HomeEvent.AddAlarm -> {
                viewModelScope.launch {
                    plantAlarmUseCases.upsertPlantAlarmUseCase.invoke(event.alarm)
                    _state.update { it.copy(
                        plantsAlarm = it.plantsAlarm.plus(event.alarm)
                    ) }

                    updatePlantsForToday()
                }
            }
            HomeEvent.HideDialog -> {
                _state.update { it.copy(
                    isDialog = false
                ) }
            }
            HomeEvent.ShowDialog -> {
                _state.update { it.copy(
                    isDialog = true
                ) }
            }
            is HomeEvent.ClickCheckBox -> {
                viewModelScope.launch {
                    val updatePlant = event.plant.copy(isWatering = event.checked)

                    plantAlarmUseCases.upsertPlantAlarmUseCase.invoke(updatePlant)
                    _state.update {it.copy(
                        plantsAlarm = _state.value.plantsAlarm.map {
                            if (it == event.plant) updatePlant
                            else it
                        }
                    ) }

                    updatePlantsForToday()
                }
            }
            HomeEvent.CheckAll -> {
                viewModelScope.launch {
                    val newList = _state.value.plantsForToday.map {
                        if (!it.isWatering) {
                            plantAlarmUseCases.upsertPlantAlarmUseCase.invoke(it)
                            it.copy(isWatering = true)
                        } else {
                            it
                        }
                    }

                    _state.update { it.copy(
                        plantsForToday = newList
                    ) }
                }
            }
        }
    }

    private fun updatePlantsForToday() {
        val currentDate = LocalDateTime.now()
        val beginningTheDay = LocalDateTime.of(currentDate.year, currentDate.monthValue, currentDate.dayOfMonth, 0, 0, 0)
        val endTheDay = LocalDateTime.of(currentDate.year, currentDate.monthValue, currentDate.dayOfMonth, 23, 59, 59)

        _state.update { it.copy(
            plantsForToday = _state.value.plantsAlarm.filter { it.basicDate >= beginningTheDay && it.basicDate <= endTheDay && it.isActive}
        ) }
    }
}