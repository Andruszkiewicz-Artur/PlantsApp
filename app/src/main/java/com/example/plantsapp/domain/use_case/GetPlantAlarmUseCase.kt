package com.example.plantsapp.domain.use_case

import com.example.plantsapp.data.mappers.toPlantAlarmModel
import com.example.plantsapp.domain.model.PlantAlarmModel
import com.example.plantsapp.domain.repository.PlantAlarmRepository
import javax.inject.Inject

class GetPlantAlarmUseCase @Inject constructor(
    private val repository: PlantAlarmRepository
) {

    suspend fun invoke(id: Int): PlantAlarmModel? {
        return repository.getPlantAlarm(id)?.toPlantAlarmModel()
    }

}