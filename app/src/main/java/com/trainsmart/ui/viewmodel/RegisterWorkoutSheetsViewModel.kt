package com.trainsmart.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.trainsmart.repository.clouddatabase.model.WorkoutSheetModel
import com.trainsmart.repository.localdatabase.database.AppDatabase
import com.trainsmart.repository.localdatabase.entity.Exercise
import com.trainsmart.repository.sharedpreferences.LocalUserData
import com.trainsmart.repository.sharedpreferences.SessionManager
import com.trainsmart.systemsettings.Constants
import com.trainsmart.systemsettings.Routes
import com.trainsmart.systemsettings.fetchWorkoutSheets
import com.trainsmart.systemsettings.saveSheetInCloud
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterWorkoutSheetsViewModel(
    val localDB: AppDatabase,
    val navController: NavController,
    val sessionManager: SessionManager,
    val cloudDB: FirebaseFirestore,
    val localUserData: LocalUserData
) : ViewModel() {

    var isDialogOpen = mutableStateOf(false)
    var selectedExerciseType = mutableStateOf("")
    var exerciseName = mutableStateOf("")
    var exerciseRepetitions = mutableStateOf("")
    var exerciseWeight = mutableStateOf("")

    private var listWorkoutSheets = mutableStateOf<WorkoutSheetModel?>(null)
    private val scope = CoroutineScope(Dispatchers.IO)

    private val exerciseDao = localDB.exerciseDao()
    private val workoutSheetDao = localDB.workoutSheetDao()


    fun setName(value: String) {
        exerciseName.value = value
    }

    fun setReps(value: String) {
        exerciseRepetitions.value = value
    }

    fun setWeight(value: String) {
        exerciseWeight.value = value
    }

    // Função para abrir o diálogo
    private fun openDialog() {
        isDialogOpen.value = true
    }

    // Função para fechar o diálogo
    fun closeDialog() {
        isDialogOpen.value = false
    }

    fun confirmAction() {
        // Limpar os campos
        exerciseName.value = ""
        exerciseRepetitions.value = ""
        exerciseWeight.value = ""
        closeDialog()
    }
    private suspend fun haveExercisesInCloud(){
        listWorkoutSheets.value = fetchWorkoutSheets(cloudDB, localUserData)
        if (listWorkoutSheets.value!!.exercises.isEmpty()){
            saveSheetInCloud(workoutSheetDao, exerciseDao, localUserData, cloudDB)
        }
    }
    private  suspend fun ifHaveConflictSaveFromLocalDatabaseToCloud(){
        listWorkoutSheets.value = fetchWorkoutSheets(cloudDB, localUserData)
        if (listWorkoutSheets.value!!.exercises != exerciseDao.getAll()){
            saveSheetInCloud(workoutSheetDao, exerciseDao, localUserData, cloudDB)
        }
    }


    private fun verifyIfHaveExercisesSavedInCloud() {
        scope.launch {
            haveExercisesInCloud()
        }
    }

    private fun verifyIfHaveConflict() {
        scope.launch {
            ifHaveConflictSaveFromLocalDatabaseToCloud()
        }
    }

    fun navigateToListExercises() {
        verifyIfHaveExercisesSavedInCloud()
        verifyIfHaveConflict()
        navController.navigate(Routes.ListWorkoutSheet.route)
    }

    fun dismissAction(destination: String) {
        closeDialog()
        verifyIfHaveExercisesSavedInCloud()
        verifyIfHaveConflict()
        sessionManager.saveAuthenticationStage(destination)
        navController.navigate(destination)
    }

    /**Esse supress é pq está sendo exibido um alerta dizendo que o existingExercise nunca é null, mas isso acontece sim***/
    @Suppress("SENSELESS_COMPARISON")
    fun cadastrarExercicio() {
        val fkUser = localUserData.get(Constants.Database.FKID_USER)
        val exercise = Exercise(
            idExercise = 0,
            type = selectedExerciseType.value.lowercase(),
            name = exerciseName.value.lowercase(),
            weight = exerciseWeight.value,
            repetitions = exerciseRepetitions.value.lowercase(),
            fkIdSheet = fkUser
        )
        if (localUserData.get(Constants.SELECTED_EMAIL_STUDENT) != "") {
            if (selectedExerciseType.value.isNotEmpty() && exerciseName.value.isNotEmpty() && exerciseRepetitions.value.isNotEmpty()) {
                viewModelScope.launch {
                    val existingExercise = exerciseDao.loadExerciseByName(exerciseName.value.lowercase())
                    if (existingExercise == null) exerciseDao.insertAll(exercise)
                    else exerciseDao.update(exercise)
                    Log.i("information", "cadastrarExercicio: ${exerciseDao.loadAll()}")
                }
                openDialog()
            }
        }
    }
}