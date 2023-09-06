package com.example.plantsapp.domain.use_case

import com.example.plantsapp.data.mappers.toPlantAlarmModel
import com.example.plantsapp.domain.model.PlantAlarmModel
import com.example.plantsapp.domain.repository.PlantAlarmRepository
import javax.inject.Inject

class GetAllPlantAlarmUseCase @Inject constructor(
    private val repository: PlantAlarmRepository
) {

    operator fun invoke(): List<PlantAlarmModel> {
        return repository.getAllPlantsAlarm().map{ it.toPlantAlarmModel() }
    }

}