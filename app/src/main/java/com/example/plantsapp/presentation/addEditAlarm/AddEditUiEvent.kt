package com.example.plantsapp.presentation.addEditAlarm

sealed class AddEditUiEvent {

    data class Toast(val value: Int): AddEditUiEvent()

    object Save: AddEditUiEvent()

}
