package com.example.plantsapp.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.plantsapp.domain.model.PlantAlarmDto
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantAlarmDao {

    @Query("SELECT * FROM plants_alarm ORDER BY id DESC")
    fun getAllPlantsAlarm(): List<PlantAlarmDto>

    @Query("SELECT * FROM plants_alarm ORDER BY id DESC")
    fun getAllPlantsAlarmFlow(): Flow<List<PlantAlarmDto>>

    @Query("SELECT * FROM plants_alarm WHERE id = :id")
    suspend fun getPlantAlarm(id: Int): PlantAlarmDto?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPlantAlarm(plantAlarm: PlantAlarmDto)

    @Delete
    suspend fun deletePlantAlarm(plantAlarm: PlantAlarmDto)
}