package com.example.plantsapp.di

import android.app.Application
import androidx.room.Room
import com.example.plantsapp.core.Static
import com.example.plantsapp.data.data_source.PlantsAlarmDatabase
import com.example.plantsapp.data.repository.PlantAlarmRepositoryImpl
import com.example.plantsapp.domain.repository.PlantAlarmRepository
import com.example.plantsapp.domain.use_case.DeletePlantAlarmUseCase
import com.example.plantsapp.domain.use_case.GetAllPlantAlarmUseCase
import com.example.plantsapp.domain.use_case.GetPlantAlarmUseCase
import com.example.plantsapp.domain.use_case.PlantAlarmUseCases
import com.example.plantsapp.domain.use_case.UpsertPlantAlarmUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePlantAlarmDatabase(app: Application): PlantsAlarmDatabase {
        return Room.databaseBuilder(
            app,
            PlantsAlarmDatabase::class.java,
            Static.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providePlantAlarmRepository(db: PlantsAlarmDatabase): PlantAlarmRepository {
        return PlantAlarmRepositoryImpl(db.plantAlarmDao)
    }

    @Provides
    @Singleton
    fun providePlantAlarmUseCases(repository: PlantAlarmRepository): PlantAlarmUseCases {
        return PlantAlarmUseCases(
            getAllPlantAlarmUseCase = GetAllPlantAlarmUseCase(repository),
            getPlantAlarmUseCase = GetPlantAlarmUseCase(repository),
            upsertPlantAlarmUseCase = UpsertPlantAlarmUseCase(repository),
            deletePlantAlarmUseCase = DeletePlantAlarmUseCase(repository)
        )
    }
}