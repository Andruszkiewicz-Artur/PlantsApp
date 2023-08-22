package com.example.plantsapp.data.mappers

import androidx.core.net.toUri
import com.example.plantsapp.domain.model.PlantAlarmDto
import com.example.plantsapp.domain.model.PlantAlarmModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

fun PlantAlarmModel.toPlantAlarmDto(): PlantAlarmDto {
    return PlantAlarmDto(
        id = id,
        plantName = plantName,
        plantDescription = plantDescription,
        photo = photo.toString(),
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
    val plantAlarmList = mutableListOf<PlantAlarmModel>()

    this.map {
        it.forEach {
            plantAlarmList.add(it.toPlantAlarmModel())
        }
    }

    return flow { plantAlarmList }
}