package com.example.plantsapp.domain.use_case

import com.example.plantsapp.data.mappers.toPlantAlarmDto
import com.example.plantsapp.domain.model.PlantAlarmModel
import com.example.plantsapp.domain.repository.PlantAlarmRepository
import javax.inject.Inject

class UpsertPlantAlarmUseCase @Inject constructor(
    private val repository: PlantAlarmRepository
) {

    suspend fun invoke(plantAlarmModel: PlantAlarmModel) {
        return repository.upsertPlantAlarm(plantAlarmModel.toPlantAlarmDto())
    }

}