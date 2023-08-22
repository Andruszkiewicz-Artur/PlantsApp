package com.example.plantsapp.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.plantsapp.domain.model.PlantAlarmDto

@Database(
    entities = [PlantAlarmDto::class],
    version = 1
)
abstract class PlantsAlarmDatabase: RoomDatabase() {

    abstract val plantAlarmDao: PlantAlarmDao

}