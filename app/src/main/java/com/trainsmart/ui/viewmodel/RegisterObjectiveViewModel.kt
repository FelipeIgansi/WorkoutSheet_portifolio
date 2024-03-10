package com.trainsmart.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.trainsmart.repository.clouddatabase.model.WorkoutSheetModel
import com.trainsmart.repository.localdatabase.database.AppDatabase
import com.trainsmart.repository.localdatabase.entity.WorkoutSheet
import com.trainsmart.repository.sharedpreferences.LocalUserData
import com.trainsmart.systemsettings.Constants
import com.trainsmart.systemsettings.Routes
import com.trainsmart.systemsettings.fetchWorkoutSheets
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class RegisterObjectiveViewModel(
    private val navController: NavController,
    val localUserData: LocalUserData,
    localDB: AppDatabase,
    val cloudDB: FirebaseFirestore
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name
    val objective = mutableStateOf("")
    private val _personal = MutableStateFlow("")
    val personal: StateFlow<String> = _personal
    val selectedDate = mutableStateOf("")
    val showDatePickerDialog = mutableStateOf(false)
    private var listWorkoutSheets = mutableStateOf<WorkoutSheetModel?>(null)

    private val sheetDao = localDB.workoutSheetDao()


    fun haveSheetsSave() = listWorkoutSheets.value?.objetivo?.isNotEmpty()
    fun setName(value: String) {
        _name.value = value
    }

    fun setObjective(value: String) {
        objective.value = value
    }

    fun setPersonal(value: String) {
        _personal.value = value
    }

    fun setSelectedDate(value: String) {
        selectedDate.value = value
    }


    fun setShowDatePickerDialog(value: Boolean) {
        showDatePickerDialog.value = value
    }

    fun initRegisterObjective() {
        viewModelScope.launch {
            listWorkoutSheets.value = fetchWorkoutSheets(cloudDB, localUserData)
            initiateAllVariables()
        }
    }

    private fun initiateAllVariables() {
        if (listWorkoutSheets.value!!.objetivo.isNotEmpty()) {
            setName(listWorkoutSheets.value!!.nome)
            setObjective(listWorkoutSheets.value!!.objetivo)
            setPersonal(listWorkoutSheets.value!!.personal)
            setSelectedDate(listWorkoutSheets.value!!.data)
        }
    }


    fun onDateSelected(millis: Long) {
        // Criar um objeto Calendar para lidar com a data
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis

        // Adicionar 1 ao valor do dia para corrigir a discrepância
        calendar.add(Calendar.DAY_OF_MONTH, 1)

        // Formatar a data para o formato desejado
        val formattedDate = calendar.timeInMillis.toBrazilianDateFormat()

        // Atualizar a LiveData com a data corrigida
        selectedDate.value = formattedDate

        // Fechar o DatePicker após selecionar a data
        showDatePickerDialog.value = false
    }

    private fun Long.toBrazilianDateFormat(): String {
        val date = Date(this)
        val format = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        return format.format(date)
    }

    @Suppress("SENSELESS_COMPARISON")
    fun onNextButtonClick() {
        if (objective.value.isNotBlank() && selectedDate.value.isNotBlank()) {
            viewModelScope.launch {
                val fkID = localUserData.get(Constants.Database.FKID_USER)
                val existingSheet = sheetDao.loadByName(name.value)
                val sheet = WorkoutSheet(
                    fkID,
                    name.value,
                    selectedDate.value,
                    objective.value,
                    personal.value
                )
                if (existingSheet != null) sheetDao.update(sheet)
                else sheetDao.insertAll(sheet)

            }
//            sessionManager.saveAuthenticationStage(Routes.RegisterWorkoutSheetScreen.route)
            navController.navigate(Routes.RegisterWorkoutSheetScreen.route)
        }
    }

    // Other functions and logic specific to the ViewModel
}
