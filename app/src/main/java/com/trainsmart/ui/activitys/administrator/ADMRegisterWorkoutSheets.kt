package com.trainsmart.ui.activitys.administrator

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trainsmart.R
import com.trainsmart.systemsettings.Constants
import com.trainsmart.systemsettings.Routes
import com.trainsmart.ui.theme.SystemRed
import com.trainsmart.ui.theme.outlinedCustomColors
import com.trainsmart.ui.viewmodel.RegisterWorkoutSheetsViewModel

@Composable
fun ADMRegisterWorkoutSheets(
    padding: PaddingValues,
    registerWorkoutSheetsViewModel: RegisterWorkoutSheetsViewModel
) {


    var expanded by remember { mutableStateOf(false) }
    val items = mapingDropDownOptions()
    var selectedIndex by remember { mutableIntStateOf(items.indexOf(Constants.ExercisesOptions.SELECT_WORKOUT)) }
    var selectedExerciseType by remember { registerWorkoutSheetsViewModel.selectedExerciseType }
    val exerciseName by remember { registerWorkoutSheetsViewModel.exerciseName }
    val exerciseRepetitions by remember { registerWorkoutSheetsViewModel.exerciseRepetitions }
    val exerciseWeight by remember { registerWorkoutSheetsViewModel.exerciseWeight }
    val dialogVisibility by remember { registerWorkoutSheetsViewModel.isDialogOpen }
    var otherCategory by remember { mutableStateOf("") }


    val destination = Routes.StudentScreen.route


    Column(
        Modifier
            .padding(padding)
            .padding(8.dp)
    ) {
        if (dialogVisibility) {
            AlertDialog(
                containerColor = Color.DarkGray,
                onDismissRequest = { registerWorkoutSheetsViewModel.closeDialog() },
                title = { Text(text = "Exercicio cadastrado!") },
                text = { Text("Gostaria de continuar cadastrando?") },
                confirmButton = {
                    Button(
                        onClick = { registerWorkoutSheetsViewModel.confirmAction() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SystemRed,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Sim")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { registerWorkoutSheetsViewModel.dismissAction(destination) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Não")
                    }
                }
            )
        }

        Text(text = "Exercicios:", fontSize = 23.sp)
        Spacer(modifier = Modifier.height(10.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            if (items[selectedIndex] == Constants.ExercisesOptions.Other) {
                OutlinedTextField(
                    value = otherCategory,
                    onValueChange = { otherCategory = it },
                    label = { Text(text = "Categoria") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedCustomColors()
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { expanded = true })
                        .height(60.dp)
                        .border(
                            color = if (!expanded) Color.Gray else SystemRed,
                            width = 2.dp,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(start = 15.dp, end = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(items[selectedIndex])
                }


                Box(modifier = Modifier.fillMaxWidth()) {
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .border(
                                color = Color.Gray,
                                width = 1.dp,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .width(390.dp)
                    ) {
                        for (index in 0..<items.size - 1) {
                            if (index != selectedIndex) {
                                DropdownMenuItem(
                                    onClick = {
                                        selectedIndex = index
                                        expanded = false
                                        selectedExerciseType = items[index]
                                    },
                                    text = { Text(text = items[index]) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }



            OutlinedTextField(
                value = exerciseName,
                onValueChange = { registerWorkoutSheetsViewModel.setName(it) },
                label = { Text(text = "Exercicio: *") },
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedCustomColors()
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(modifier = Modifier.width(2.dp))
                OutlinedTextField(
                    value = exerciseRepetitions,
                    onValueChange = { registerWorkoutSheetsViewModel.setReps(it) },
                    label = { Text(text = "Repetições: *") },
                    modifier = Modifier
                        .weight(1f),
                    maxLines = 1,
                    colors = outlinedCustomColors()
                )
                Spacer(modifier = Modifier.width(6.dp))
                OutlinedTextField(
                    value = exerciseWeight,
                    onValueChange = { newText ->
                        if (newText.all { it.isDigit() }) {
                            registerWorkoutSheetsViewModel.setWeight(newText)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(text = "Carga:") },
                    modifier = Modifier
                        .weight(1f),
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_kg),
                            contentDescription = null,
                            Modifier.size(20.dp)
                        )
                    },
                    maxLines = 1,
                    colors = outlinedCustomColors()
                )
                Spacer(modifier = Modifier.width(2.dp))
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { registerWorkoutSheetsViewModel.cadastrarExercicio() },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SystemRed,
                contentColor = Color.White
            )
        ) {
            Text(text = "Cadastrar")
        }
        OutlinedButton(
            onClick = { registerWorkoutSheetsViewModel.navigateToListExercises() },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White,
            )
        ) {
            Text(text = "Ver exercicios cadastrados")
        }
    }
}

@Composable
private fun mapingDropDownOptions() = Constants.ExercisesOptions::class.java.declaredFields
    .filter { it.type == String::class.java }
    .map { it.get(null) as String }
    .sorted()

