package com.example.plantsapp.data.repository

import com.example.plantsapp.data.data_source.PlantAlarmDao
import com.example.plantsapp.domain.model.PlantAlarmDto
import com.example.plantsapp.domain.repository.PlantAlarmRepository
import kotlinx.coroutines.flow.Flow

class PlantAlarmRepositoryImpl(
    private val dao: PlantAlarmDao
): PlantAlarmRepository {

    override fun getAllPlantsAlarm(): Flow<List<PlantAlarmDto>> {
        return dao.getAllPlantsAlarm()
    }

    override suspend fun getPlantAlarm(id: Int): PlantAlarmDto {
        return dao.getPlantAlarm(id)
    }

    override suspend fun upsertPlantAlarm(plantAlarmDto: PlantAlarmDto) {
        return dao.upsertPlantAlarm(plantAlarmDto)
    }

    override suspend fun deletePlantAlarm(plantAlarmDto: PlantAlarmDto) {
        return dao.deletePlantAlarm(plantAlarmDto)
    }

}