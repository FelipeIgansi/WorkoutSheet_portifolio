package com.trainsmart.ui.activitys.administrator

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.trainsmart.R
import com.trainsmart.repository.sharedpreferences.LocalUserData
import com.trainsmart.systemsettings.Constants
import com.trainsmart.systemsettings.Routes
import com.trainsmart.ui.theme.SystemRed
import com.trainsmart.ui.theme.outlinedCustomColors
import com.trainsmart.ui.viewmodel.EditExerciseViewModel
import java.util.Locale

@Composable
fun ADMEditExercise(
    padding: PaddingValues,
    navController: NavController,
    localUserData: LocalUserData,
    editExerciseViewModel: EditExerciseViewModel
) {
    val exerciseState by editExerciseViewModel.exercise.collectAsState()

    LaunchedEffect(key1 = Unit) { editExerciseViewModel.loadExercise() }

    Column(
        Modifier
            .padding(padding)
            .padding(8.dp)
    ) {

        Text(text = "Exercicios:", fontSize = 23.sp)
        Spacer(modifier = Modifier.height(10.dp))

        Column(modifier = Modifier.fillMaxWidth()) {

            OutlinedTextField(
                value = exerciseState.type.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedCustomColors()
            )

            OutlinedTextField(
                value = exerciseState.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                onValueChange = { editExerciseViewModel.onNameChange(it) },
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
                    value = exerciseState.repetitions,
                    onValueChange = { editExerciseViewModel.onRepetitionsChange(it) },
                    label = { Text(text = "Repetições: *") },
                    modifier = Modifier
                        .weight(1f),
                    maxLines = 1,
                    colors = outlinedCustomColors()
                )
                Spacer(modifier = Modifier.width(6.dp))
                OutlinedTextField(
                    value = exerciseState.weight,
                    onValueChange = { editExerciseViewModel.onWeightChange(it) },
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
            onClick = {
                if (localUserData.get(Constants.SELECTED_EMAIL_STUDENT) != "") {
                    editExerciseViewModel.updateExercise()
                    navController.navigate(Routes.ListWorkoutSheet.route)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SystemRed,
                contentColor = Color.White
            )
        ) {
            Text(text = "Editar")
        }
    }
}