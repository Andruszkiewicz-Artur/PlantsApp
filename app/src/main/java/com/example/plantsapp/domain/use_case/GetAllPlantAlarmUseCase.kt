package com.example.plantsapp.domain.use_case

import com.example.plantsapp.data.mappers.toListOfPlantAlarmModel
import com.example.plantsapp.domain.model.PlantAlarmModel
import com.example.plantsapp.domain.repository.PlantAlarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPlantAlarmUseCase @Inject constructor(
    private val repository: PlantAlarmRepository
) {

    operator fun invoke(): Flow<List<PlantAlarmModel>> {
        return repository.getAllPlantsAlarm().toListOfPlantAlarmModel()
    }

}