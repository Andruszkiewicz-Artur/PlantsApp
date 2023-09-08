package com.example.plantsapp.domain.use_case

data class PlantAlarmUseCases(
    val getAllPlantAlarmUseCase: GetAllPlantAlarmUseCase,
    val getPlantAlarmUseCase: GetPlantAlarmUseCase,
    val upsertPlantAlarmUseCase: UpsertPlantAlarmUseCase,
    val deletePlantAlarmUseCase: DeletePlantAlarmUseCase,
    val getAllPlantAlarmFlowUseCase: GetAllPlantAlarmFlowUseCase
)
