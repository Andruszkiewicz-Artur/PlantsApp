package com.example.plantsapp.domain.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "plants_alarm")
data class PlantAlarmDto(
    @ColumnInfo(name = "id")
    @PrimaryKey val id: Int? = null,

    @ColumnInfo(name = "plantName")
    val plantName: String,

    @ColumnInfo(name = "plantDescription")
    val plantDescription: String,

    @ColumnInfo(name = "photo")
    val photo: String?,

    @ColumnInfo(name = "isActive")
    val isActive: Boolean,

    @ColumnInfo(name = "repeating")
    val repeating: Int,

    @ColumnInfo(name = "basicDate")
    val basicDate: String,

    @ColumnInfo(name = "nextDate")
    val nextDate: String,

    @ColumnInfo(name = "isWatering")
    val isWatering: Boolean
)
