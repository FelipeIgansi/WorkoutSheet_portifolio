package com.trainsmart.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.trainsmart.repository.clouddatabase.model.WorkoutSheetModel
import com.trainsmart.repository.localdatabase.database.AppDatabase
import com.trainsmart.repository.localdatabase.entity.Exercise
import com.trainsmart.repository.localdatabase.entity.WorkoutSheet
import com.trainsmart.repository.sharedpreferences.LocalUserData
import com.trainsmart.systemsettings.Constants
import com.trainsmart.systemsettings.fetchWorkoutSheets
import kotlinx.coroutines.launch

class ListWorkoutSheetsViewModel(
    private val cloudDB: FirebaseFirestore,
    private val localUserData: LocalUserData,
    localDB: AppDatabase
) : ViewModel() {
    private var listWorkoutSheets = mutableStateOf<WorkoutSheetModel?>(null)
    private var _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> get() = _exercises

    private var _workoutSheet = MutableLiveData<List<WorkoutSheet>>()
    val workoutSheet: LiveData<List<WorkoutSheet>> get() = _workoutSheet

    private val exerciseDao = localDB.exerciseDao()
    private val workoutSheetDao = localDB.workoutSheetDao()

    fun initDataFetching() { // Quando coloca essa viewmodel para ser iniciada na navigarion essa parte será executada e acontecerá bug
        try {
            viewModelScope.launch {
                listWorkoutSheets.value = fetchWorkoutSheets(cloudDB, localUserData)
                setTableValues()
                _exercises.postValue(getExercisesFromLocalDB())
                _workoutSheet.postValue(getUserInfosFromLocalDB())
            }
        } catch (e: Exception) {
            Log.w("Erro", "initDataFetching: Ocorreu um erro ao tentar inicializar as variaveis")
        }
    }

    private suspend fun setTableValues(){
        setUserInfosInLocalDB(WorkoutSheet(
                studentName = listWorkoutSheets.value?.nome ?: "",
                startDate = listWorkoutSheets.value?.data ?: "",
                objective = listWorkoutSheets.value?.objetivo ?: "",
                personal = listWorkoutSheets.value?.personal ?: "",
                idSheet = if (listWorkoutSheets.value?.nome!!.isNotEmpty()) localUserData.get(
                    Constants.Database.FKID_USER
                ) else ""
            )
        )

        setExerciseInLocalDB(listWorkoutSheets.value?.exercises)
    }

    private suspend fun getUserInfosFromLocalDB() = workoutSheetDao.getAll()

    private suspend fun getExercisesFromLocalDB() = exerciseDao.getAll()


    private suspend fun setUserInfosInLocalDB(sheetInformations: WorkoutSheet?) {
        try {
            if (workoutSheetDao.getAll().isNotEmpty())workoutSheetDao.deleteAll()
            if (sheetInformations?.studentName!!.isNotEmpty()) {
                if (workoutSheetDao.getAll().isEmpty()) {
                    workoutSheetDao.insertAll(sheetInformations)
                } /*else workoutSheetDao.insertAllWithReplace(sheetInformations)*/
            }
        } catch (e: Exception) {
            Log.w(
                "Erro",
                "setUserInfosInLocalDB: Ocorreu um erro ao tentar inserir as WorkoutSheets"
            )
        }
    }

    private suspend fun setExerciseInLocalDB(exercises: List<Exercise>?) {
        try {// Todo(Gambiarra para fazer funcionar, pois quando é alterado de usuário está ocorrendo conflito de ids na tabela exercise)
            if (exerciseDao.getAll().isNotEmpty()) exerciseDao.deleteAll()
            exercises?.forEach { exercise ->
                exerciseDao.insertAllWithReplace(exercise)
            }
        } catch (e: Exception) {
            Log.w("Erro", "setUserInfosInLocalDB: Ocorreu um erro ao tentar inserir os exercicios")
        }

    }
}
