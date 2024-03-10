package com.trainsmart.repository.localdatabase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercise",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSheet::class,
            parentColumns = ["idSheet"],
            childColumns = ["fkIdSheet"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Exercise(
    @PrimaryKey(autoGenerate = true) val idExercise: Long = 0L,
    @ColumnInfo(name = "type") var type: String = "",
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "weight") var weight: String = "",
    @ColumnInfo(name = "repetitions") var repetitions: String = "",
    @ColumnInfo(name = "fkIdSheet") var fkIdSheet: String =""
)