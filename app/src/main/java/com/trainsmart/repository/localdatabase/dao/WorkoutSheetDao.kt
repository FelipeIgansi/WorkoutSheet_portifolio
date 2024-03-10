package com.trainsmart.repository.localdatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.trainsmart.repository.localdatabase.entity.WorkoutSheet

@Dao
interface WorkoutSheetDao {
    @Query("SELECT * FROM workout_sheet")
    suspend fun getAll(): MutableList<WorkoutSheet>

    @Query("SELECT * FROM workout_sheet WHERE idSheet IN (:sheetIds)")
    suspend fun loadAllByIds(sheetIds: List<String>): List<WorkoutSheet>

    @Query("SELECT * FROM workout_sheet WHERE idSheet like (:sheetIds)")
    suspend fun loadById(sheetIds: String): WorkoutSheet

    @Query("SELECT * FROM workout_sheet WHERE studentName LIKE :nome LIMIT 1")
    suspend fun loadByName(nome: String): WorkoutSheet

    @Insert
    suspend fun insertAll(vararg planilhas: WorkoutSheet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWithReplace(vararg planilhas: WorkoutSheet)


    @Update
    suspend fun update(planilha: WorkoutSheet)

    @Update
    suspend fun updateAll(planilha: List<WorkoutSheet>)

    @Delete
    suspend fun delete(planilha: WorkoutSheet)
    @Query("delete from workout_sheet")
    suspend fun deleteAll()
}