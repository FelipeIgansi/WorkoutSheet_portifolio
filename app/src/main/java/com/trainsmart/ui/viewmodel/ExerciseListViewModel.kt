package com.trainsmart.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trainsmart.repository.localdatabase.database.AppDatabase
import com.trainsmart.repository.localdatabase.entity.Exercise
import com.trainsmart.repository.sharedpreferences.LocalUserData
import com.trainsmart.systemsettings.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExerciseListViewModel(localDB: AppDatabase, private val localUserData: LocalUserData) : ViewModel() {
    private val exerciseDao = localDB.exerciseDao()

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises

    fun loadExercises() {
        viewModelScope.launch {
            _exercises.value = exerciseDao.loadAllByFKId(localUserData.get(Constants.Database.FKID_USER))
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseDao.delete(exercise)
            _exercises.value = exerciseDao.getAll()
        }
    }

    fun onEditExercise(exerciseId: String) {
        localUserData.save(Constants.Database.SELECTED_ID_FROM_EXERCISE_TO_EDIT, exerciseId)
    }
}