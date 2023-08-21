package com.example.plantsapp.presentation.addEditAlarm

import android.net.Uri
import java.time.LocalDateTime

sealed class AddEditEvent {

    data class EnteredPlantName(val value: String): AddEditEvent()
    data class ChoosePhoto(val uri: Uri): AddEditEvent()
    data class EnteredPlantDescription(val value: String): AddEditEvent()
    data class ChooseAlarmTime(val days: Int): AddEditEvent()

    object Save: AddEditEvent()

}
