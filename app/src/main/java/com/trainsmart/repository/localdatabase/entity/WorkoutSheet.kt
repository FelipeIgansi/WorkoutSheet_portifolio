package com.trainsmart.repository.localdatabase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_sheet")
data class WorkoutSheet(
    @PrimaryKey val idSheet: String = "",
    @ColumnInfo(name = "studentName") var studentName: String = "",
    @ColumnInfo(name = "startDate") var startDate: String = "",
    @ColumnInfo(name = "objective") var objective: String = "",
    @ColumnInfo(name = "administrator") var personal: String = "",
)