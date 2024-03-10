package com.trainsmart.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.trainsmart.repository.localdatabase.database.AppDatabase
import com.trainsmart.repository.localdatabase.entity.Exercise
import com.trainsmart.repository.sharedpreferences.LocalUserData
import com.trainsmart.systemsettings.Constants
import com.trainsmart.systemsettings.saveSheetInCloud
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditExerciseViewModel(
    private val localDB: AppDatabase,
    private val localUserData: LocalUserData,
    private val cloudDB: FirebaseFirestore
) : ViewModel() {

    private val _exercise = MutableStateFlow(Exercise())
    val exercise: StateFlow<Exercise> = _exercise

    private val exerciseDao = localDB.exerciseDao()
    private val workoutSheetDao = localDB.workoutSheetDao()

    fun loadExercise() {
        viewModelScope.launch(Dispatchers.IO) {
            val idExerciseToEdit = localUserData.get(Constants.Database.SELECTED_ID_FROM_EXERCISE_TO_EDIT)
            val loadedExercise = localDB.exerciseDao().loadAllByID(idExerciseToEdit)
            _exercise.value = loadedExercise
        }
    }

    fun onNameChange(newName: String) {
        _exercise.value = _exercise.value.copy(name = newName)
    }

    fun onRepetitionsChange(newRepetitions: String) {
        _exercise.value = _exercise.value.copy(repetitions = newRepetitions)
    }

    fun onWeightChange(newWeight: String) {
        _exercise.value = _exercise.value.copy(weight = newWeight)
    }

    fun updateExercise() {
        val exerciseToUpdate = _exercise.value.copy(
            idExercise = localUserData.get(Constants.Database.SELECTED_ID_FROM_EXERCISE_TO_EDIT).toLong(),
            fkIdSheet = localUserData.get(Constants.Database.FKID_USER)
        )
        viewModelScope.launch(Dispatchers.IO) {
            localDB.exerciseDao().update(exerciseToUpdate)
            saveSheetInCloud(workoutSheetDao, exerciseDao, localUserData, cloudDB)
        }
    }
}