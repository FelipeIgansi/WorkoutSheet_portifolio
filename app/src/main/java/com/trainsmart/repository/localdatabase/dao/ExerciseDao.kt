package com.trainsmart.repository.localdatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.trainsmart.repository.localdatabase.entity.Exercise

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercise")
    suspend fun getAll(): MutableList<Exercise>

    @Query("SELECT * FROM exercise WHERE fkIdSheet IN (:exercicioIds)")
    suspend fun loadAllByFKId(exercicioIds: String): List<Exercise>
    @Query("SELECT * FROM exercise WHERE idExercise IN (:exercicioIds)")
    suspend fun loadAllByID(exercicioIds: String): Exercise

    @Query("SELECT * FROM exercise WHERE name LIKE (:exercicioIds) LIMIT 1")
    suspend fun loadExerciseByName(exercicioIds: String): Exercise
    @Query("SELECT * FROM exercise")
    suspend fun loadAll(): List<Exercise>

    @Insert
    suspend fun insertAll(vararg exercises: Exercise)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWithReplace(vararg exercises: Exercise)


    @Update
    suspend fun update(exercise: Exercise)

    @Update
    suspend fun updateAll(exercises: List<Exercise>)


    @Delete
    suspend fun delete(exercise: Exercise)

    @Query("delete from exercise")
    suspend fun deleteAll()

}