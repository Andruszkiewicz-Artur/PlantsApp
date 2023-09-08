package com.example.plantsapp.domain.repository

import com.example.plantsapp.domain.model.PlantAlarmDto
import kotlinx.coroutines.flow.Flow

interface PlantAlarmRepository {

    fun getAllPlantsAlarm(): List<PlantAlarmDto>

    fun getAllPlantsAlarmFlow(): Flow<List<PlantAlarmDto>>

    suspend fun getPlantAlarm(id: Int): PlantAlarmDto?

    suspend fun upsertPlantAlarm(plantAlarmDto: PlantAlarmDto)

    suspend fun deletePlantAlarm(plantAlarmDto: PlantAlarmDto)

}