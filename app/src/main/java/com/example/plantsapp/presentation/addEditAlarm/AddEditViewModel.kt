package com.example.plantsapp.presentation.addEditAlarm

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.plantsapp.R
import com.example.plantsapp.domain.model.PlantAlarmModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val application: Application
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

    fun onEvent(event: AddEditEvent) {
        when (event) {
            is AddEditEvent.ChooseAlarmTime -> {
                _state.update { it.copy(
                    alarmModel = it.alarmModel.copy(
                        alarmTime = event.days
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
}