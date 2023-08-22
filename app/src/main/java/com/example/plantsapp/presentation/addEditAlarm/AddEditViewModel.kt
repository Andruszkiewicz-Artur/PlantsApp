package com.example.plantsapp.presentation.addEditAlarm

import android.app.Application
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantsapp.R
import com.example.plantsapp.domain.model.PlantAlarmModel
import com.example.plantsapp.domain.use_case.PlantAlarmUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val application: Application,
    private val plantAlarmUseCases: PlantAlarmUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(AddEditState(
        alarmModel = PlantAlarmModel(
            isActive = true
        ),
        outputDirectory = getOutputDirectory(application),
        cameraExecutor = Executors.newSingleThreadExecutor()
    ) )
    val state = _state.asStateFlow()

    private val _sharedFlow = MutableSharedFlow<AddEditUiEvent>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>("plantAlarmId")?.let { plantAlarmId ->
            if (plantAlarmId != -1) {
                viewModelScope.launch {
                    plantAlarmUseCases.getPlantAlarmUseCase.invoke(plantAlarmId)?.also { plantAlarm ->
                        _state.update { it.copy(
                            alarmModel = plantAlarm
                        ) }
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditEvent) {
        when (event) {
            is AddEditEvent.ChooseAlarmTime -> {
                _state.update { it.copy(
                    alarmModel = it.alarmModel.copy(
                        repeating = event.days
                    )
                ) }
            }
            is AddEditEvent.ChoosePhoto -> {
                _state.update { it.copy(
                    alarmModel = it.alarmModel.copy(
                        photo = event.uri
                    )
                ) }
            }
            is AddEditEvent.EnteredPlantDescription -> {
                _state.update { it.copy(
                    alarmModel = it.alarmModel.copy(
                        plantDescription = event.value
                    )
                ) }
            }
            is AddEditEvent.EnteredPlantName -> {
                _state.update { it.copy(
                    alarmModel = it.alarmModel.copy(
                        plantName = event.value
                    )
                ) }
            }
            AddEditEvent.Save -> {
                viewModelScope.launch {
                    if (noneError()) {
                        plantAlarmUseCases.upsertPlantAlarmUseCase.invoke(_state.value.alarmModel)
                        if (_state.value.alarmModel.id == null) _sharedFlow.emit(AddEditUiEvent.Toast(R.string.AddNewAlarm))
                        else _sharedFlow.emit(AddEditUiEvent.Toast(R.string.SaveAlarm))

                        _sharedFlow.emit(AddEditUiEvent.Save)
                    }
                }
            }
            AddEditEvent.HideCamera -> {
                _state.update {it.copy(
                    showCamera = false
                ) }
            }
            AddEditEvent.ShowCamera -> {
                _state.update {it.copy(
                    showCamera = true
                ) }
            }
        }
    }

    private fun getOutputDirectory(context: Context): File {
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, context.getString(R.string.app_name)).apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }

    private suspend fun noneError(): Boolean {
        if (_state.value.alarmModel.plantName.isBlank()) {
            _sharedFlow.emit(AddEditUiEvent.Toast(R.string.AddPlantName))
            return false
        } else if (_state.value.alarmModel.repeating < 1) {
            _sharedFlow.emit(AddEditUiEvent.Toast(R.string.EveryDayIsWrong))
            return false
        }

        return true
    }
}