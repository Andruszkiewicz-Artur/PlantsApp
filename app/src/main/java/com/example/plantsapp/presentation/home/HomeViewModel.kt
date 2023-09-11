package com.example.plantsapp.presentation.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantsapp.core.Static
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
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val currentDate = LocalDateTime.now()
    private val beginningTheDay = LocalDateTime.of(currentDate.year, currentDate.monthValue, currentDate.dayOfMonth, 0, 0, 0)
    private val endTheDay = LocalDateTime.of(currentDate.year, currentDate.monthValue, currentDate.dayOfMonth, 23, 59, 59)

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    companion object {
        private const val TAG = "HomeViewModel"
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            plantAlarmUseCases.getAllPlantAlarmFlowUseCase.invoke().onEach { plantAlarms ->
                _state.update { it.copy(
                    plantsAlarm = plantAlarms
                ) }

                updatePlantsForToday()
            }.launchIn(viewModelScope)
        }

        savedStateHandle.get<String>(Static.PRESENT_WATERING)?.let { presentWatering ->
            _state.update {it.copy(
                isDialog = presentWatering.toBoolean()
            ) }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.ChangeActiveAlarm -> {
                viewModelScope.launch {
                    plantAlarmUseCases.upsertPlantAlarmUseCase.invoke(event.alarm.copy(
                        isActive = !event.alarm.isActive
                    ))
                }
            }
            is HomeEvent.DeleteAlarm -> {
                viewModelScope.launch {
                    plantAlarmUseCases.deletePlantAlarmUseCase.invoke(event.alarm)
                }
            }
            is HomeEvent.AddAlarm -> {
                viewModelScope.launch {
                    plantAlarmUseCases.upsertPlantAlarmUseCase.invoke(event.alarm)
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
                }
            }
            HomeEvent.CheckAll -> {
                viewModelScope.launch {
                    _state.value.plantsForToday.map {
                        if (!it.isWatering) plantAlarmUseCases.upsertPlantAlarmUseCase.invoke(it.copy(isWatering = true))
                    }
                }
            }
        }
    }

    private fun updatePlantsForToday() {
        _state.update { it.copy(
            plantsForToday = _state.value.plantsAlarm.filter { it.basicDate >= beginningTheDay && it.basicDate <= endTheDay && it.isActive}
        ) }
    }
}