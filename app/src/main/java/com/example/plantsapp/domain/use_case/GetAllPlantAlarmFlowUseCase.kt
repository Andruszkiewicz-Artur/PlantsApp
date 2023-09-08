package com.example.plantsapp.domain.use_case

import android.util.Log
import com.example.plantsapp.data.mappers.toPlantAlarmModel
import com.example.plantsapp.domain.model.PlantAlarmModel
import com.example.plantsapp.domain.repository.PlantAlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllPlantAlarmFlowUseCase @Inject constructor(
    private val repository: PlantAlarmRepository
) {

    operator fun invoke(): Flow<List<PlantAlarmModel>> {
        return repository.getAllPlantsAlarmFlow()
            .map { plantAlarmDtoList ->
                plantAlarmDtoList.map { dto ->
                    dto.toPlantAlarmModel()
                }
            }

    }

}