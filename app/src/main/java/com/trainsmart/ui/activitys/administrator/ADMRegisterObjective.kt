package com.trainsmart.ui.activitys.administrator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.trainsmart.repository.sharedpreferences.LocalUserData
import com.trainsmart.systemsettings.Constants
import com.trainsmart.systemsettings.findUserIDFromFirestore
import com.trainsmart.ui.theme.SystemRed
import com.trainsmart.ui.theme.outlinedCustomColors
import com.trainsmart.ui.viewmodel.RegisterObjectiveViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ADMRegisterObjective(
    padding: PaddingValues,
    registerObjectiveViewModel: RegisterObjectiveViewModel,
    cloudDB: FirebaseFirestore,
    localUserData: LocalUserData,
) {


    val name by registerObjectiveViewModel.name.collectAsState()
    val objective by remember { registerObjectiveViewModel.objective }
    val personal by registerObjectiveViewModel.personal.collectAsState()
    val selectedDate by remember { registerObjectiveViewModel.selectedDate }
    val showDatePickerDialog by remember { registerObjectiveViewModel.showDatePickerDialog }

    val focusManager = LocalFocusManager.current
    val datePickerState = rememberDatePickerState()
    val email = localUserData.get(Constants.SELECTED_EMAIL_STUDENT)
    var haveSheetSaved by rememberSaveable {
        mutableStateOf(registerObjectiveViewModel.haveSheetsSave() ?: false)
    }



    findUserIDFromFirestore(cloudDB, email, localUserData)
    registerObjectiveViewModel.initRegisterObjective()

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { registerObjectiveViewModel.setShowDatePickerDialog(false) },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            registerObjectiveViewModel.onDateSelected(millis)
                            registerObjectiveViewModel.setShowDatePickerDialog(false) // Movido para após a atualização da data
                        }
                    }
                ) {
                    Text(text = "Escolher data")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }


    Box(
        modifier = Modifier
            .padding(padding)
            .padding(10.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Informações do aluno(a):", fontSize = 23.sp)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = {},
                    label = { Text(text = "Aluno: ") },
                    readOnly = true,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedCustomColors()
                )
                Row {
                    OutlinedTextField(
                        value = personal,
                        onValueChange = { registerObjectiveViewModel.setPersonal(it) },
                        label = { Text(text = "Personal: ") },
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedCustomColors(),
                        readOnly = !haveSheetSaved,
                        trailingIcon = {
                            if (!haveSheetSaved) IconButton(onClick = {
                                haveSheetSaved = !haveSheetSaved
                                if (haveSheetSaved) {
                                    registerObjectiveViewModel.setPersonal("")
                                }
                            }) {
                                Icon(Icons.Filled.Edit, contentDescription = null)
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                OutlinedTextField(
                    value = (selectedDate),
                    onValueChange = { registerObjectiveViewModel.setSelectedDate(it) },
                    Modifier
                        .fillMaxWidth()
                        .onFocusEvent {
                            if (it.isFocused) {
                                registerObjectiveViewModel.setShowDatePickerDialog(true)
                                focusManager.clearFocus(force = true)
                            }
                        },
                    label = { Text("Data de inicio: *") },
                    readOnly = true,
                    colors = outlinedCustomColors()
                )

                OutlinedTextField(
                    value = (objective),
                    onValueChange = { registerObjectiveViewModel.setObjective(it) },
                    label = { Text(text = "Objetivo: *") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    colors = outlinedCustomColors()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        registerObjectiveViewModel.setName(name)
                        registerObjectiveViewModel.onNextButtonClick()
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SystemRed,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Proximo")
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                }
            }
        }
    }
}