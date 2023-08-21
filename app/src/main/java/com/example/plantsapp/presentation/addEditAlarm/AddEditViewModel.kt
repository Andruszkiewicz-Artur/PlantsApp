package com.example.plantsapp.presentation.addEditAlarm

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.plantsapp.domain.model.PlantAlarmModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(

): ViewModel() {

    private val _state = MutableStateFlow( PlantAlarmModel(
        isActive = true
    ) )
    val state = _state.asStateFlow()

    private val _sharedFlow = MutableSharedFlow<AddEditUiEvent>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun onEvent(event: AddEditEvent) {
        when (event) {
            is AddEditEvent.ChooseAlarmTime -> {
                _state.update { it.copy(
                    alarmTime = event.days
                ) }
            }
            is AddEditEvent.ChoosePhoto -> {
                Log.d("Check incoming value", "${event.uri}")
                _state.update { it.copy(
                    photo = event.uri
                ) }
                Log.d("Check update state", "${_state.value.photo}")
            }
            is AddEditEvent.EnteredPlantDescription -> {
                _state.update { it.copy(
                    plantName = event.value
                ) }
            }
            is AddEditEvent.EnteredPlantName -> {
                _state.update { it.copy(
                    plantName = event.value
                ) }
            }
            AddEditEvent.Save -> {

            }
        }
    }

}