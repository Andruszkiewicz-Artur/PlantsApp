package com.example.plantsapp.data.mappers

import android.util.Log
import androidx.core.net.toUri
import com.example.plantsapp.domain.model.PlantAlarmDto
import com.example.plantsapp.domain.model.PlantAlarmModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

fun PlantAlarmModel.toPlantAlarmDto(): PlantAlarmDto {
    return PlantAlarmDto(
        id = id,
        plantName = plantName,
        plantDescription = plantDescription,
        photo = photo?.toString(),
        isActive = isActive,
        repeating = repeating,
        basicDate = basicDate.toString(),
        nextDate = nextDate.toString()
    )
}

fun PlantAlarmDto.toPlantAlarmModel(): PlantAlarmModel {
    return PlantAlarmModel(
        id = id,
        plantName = plantName,
        plantDescription = plantDescription,
        photo = photo?.toUri(),
        isActive = isActive,
        repeating = repeating,
        basicDate = LocalDateTime.parse(basicDate),
        nextDate = LocalDateTime.parse(nextDate)
    )
}

fun Flow<List<PlantAlarmDto>>.toListOfPlantAlarmModel(): Flow<List<PlantAlarmModel>> {
    return this.map { plantAlarmDtoList ->
        plantAlarmDtoList.map { dto ->
            dto.toPlantAlarmModel()
        }
    }
}