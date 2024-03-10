package com.trainsmart.ui.activitys.user

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.trainsmart.repository.sharedpreferences.LocalUserData
import com.trainsmart.systemsettings.Constants
import com.trainsmart.systemsettings.findUserIDFromFirestore
import com.trainsmart.ui.theme.SystemGreen
import com.trainsmart.ui.viewmodel.ListWorkoutSheetsViewModel
import java.util.Locale

@Composable
fun UserListWorkoutSheets(
    padding: PaddingValues,
    viewmodelListWorkoutSheets: ListWorkoutSheetsViewModel,
    localUserData: LocalUserData,
    cloudDB: FirebaseFirestore
) {

    val email = localUserData.get(Constants.EMAIL)
    findUserIDFromFirestore(cloudDB, email, localUserData)

    viewmodelListWorkoutSheets.initDataFetching()
    val exercises by viewmodelListWorkoutSheets.exercises.observeAsState(initial = listOf())
    val sheetInformations by viewmodelListWorkoutSheets.workoutSheet.observeAsState(initial = listOf())
    var userInfoWasClicked by remember { mutableStateOf(false) }

    // Agrupar os exercícios pelo tipo e selecionar apenas um exercício de cada tipo
    val uniqueExercises = exercises.groupBy { it.type }.mapValues { it.value.first() }

    val savedState = remember { mutableStateMapOf<String, Boolean>() }
    exercises.forEach { exercise ->
        savedState[exercise.name] = localUserData.getClickedState(exercise.name)
    }

    val clickedRowMap = remember { savedState }
    // Mapa para armazenar a porcentagem de conclusão de cada categoria
    val categoryCompletionMap = remember { mutableMapOf<String, Float>() }

    fun lerpColor(startColor: Color, endColor: Color, fraction: Float): Color {
        fun clamp(value: Float, min: Float, max: Float): Float {
            return if (value < min) min else if (value > max) max else value
        }

        val clampedFraction = clamp(fraction, 0f, 1f)

        fun lerpComponent(start: Float, end: Float, fraction: Float): Float {
            return start + clampedFraction * (end - start)
        }

        return Color(
            lerpComponent(startColor.red, endColor.red, clampedFraction),
            lerpComponent(startColor.green, endColor.green, clampedFraction),
            lerpComponent(startColor.blue, endColor.blue, clampedFraction),
            lerpComponent(startColor.alpha, endColor.alpha, clampedFraction)
        )
    }


    DisposableEffect(Unit) {
        onDispose {
            // Salvar os estados de clique ao sair da tela
            clickedRowMap.forEach { (exerciseName, isChecked) ->
                localUserData.saveClickedState(exerciseName, isChecked)
            }
        }
    }

    Column(
        Modifier
            .padding(padding)
            .fillMaxSize()
    ) {
        if (sheetInformations.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Você ainda não possui fichas de treino! :(")
            }
        } else {
            sheetInformations.forEach { sheetInformation ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Aluno: ${sheetInformation.studentName}\n" +
                                "Personal: ${sheetInformation.personal}\n" +
                                "Objetivo: ${sheetInformation.objective}\n" +
                                "Inicio do treino: ${sheetInformation.startDate}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clickable { userInfoWasClicked = !userInfoWasClicked },
                        maxLines = if (userInfoWasClicked) Int.MAX_VALUE else 1
                    )
                }
            }


            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    uniqueExercises.forEach { (_, exercise) ->
                        val isExpanded = remember { mutableStateOf(false) }
                        val isChecked = clickedRowMap[exercise.name] ?: false


                        // Adicionando a categoria ao mapa se ainda não estiver presente
                        if (!categoryCompletionMap.containsKey(exercise.type)) {
                            categoryCompletionMap[exercise.type] = 0f
                        }

                        val completionPercentage = if (isChecked) {
                            val totalExercisesOfSameType =
                                exercises.count { it.type == exercise.type }
                            val checkedExercisesOfSameType = exercises.filter {
                                it.type == exercise.type && (clickedRowMap[it.name] ?: false)
                            }.size
                            checkedExercisesOfSameType.toFloat() / totalExercisesOfSameType.toFloat()
                        } else {
                            val totalExercisesOfSameType =
                                exercises.count { it.type == exercise.type }
                            val checkedExercisesOfSameType = exercises.filter {
                                it.type == exercise.type && (clickedRowMap[it.name] ?: false)
                            }.size
                            (checkedExercisesOfSameType.toFloat() - 1) / totalExercisesOfSameType.toFloat()
                        }

                        // Atualizando a porcentagem de conclusão da categoria
                        categoryCompletionMap[exercise.type] = completionPercentage


                        // Aplicando a cor de acordo com a porcentagem de conclusão
                        val categoryColor = lerpColor(
                            startColor = Color.LightGray,
                            endColor = Color.Green,
                            fraction = completionPercentage
                        )


                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(categoryColor)
                                    .clickable {
                                        isExpanded.value = !isExpanded.value
                                    },
                                contentAlignment = Alignment.Center,
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Icon(
                                        if (isExpanded.value) Icons.Filled.KeyboardArrowUp
                                        else Icons.Filled.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = Color.Black
                                    )
                                    Text(
                                        text = exercise.type.replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase(
                                                Locale.ROOT
                                            ) else it.toString()
                                        },
                                        fontSize = 23.sp,
                                        color = Color.Black
                                    )
                                    Icon(
                                        if (isExpanded.value) Icons.Filled.KeyboardArrowUp
                                        else Icons.Filled.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = Color.Black
                                    )
                                }
                            }

                            // Verificar se este registro está expandido e exibir os detalhes se necessário
                            if (isExpanded.value) {
                                val exercisesOfSameType =
                                    exercises.filter { it.type == exercise.type }
                                exercisesOfSameType.forEach { exercise ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 20.dp, end = 20.dp)
                                            .border(
                                                color = if (clickedRowMap[exercise.name] == true) SystemGreen
                                                else Color.Transparent,
                                                width = 1.dp,
                                                shape = RoundedCornerShape(5.dp)
                                            )
                                            .padding(10.dp)
                                            .clickable {
                                                // Definindo o clique na linha
                                                clickedRowMap[exercise.name] =
                                                    !(clickedRowMap[exercise.name] ?: false)
                                                localUserData.saveClickedState(
                                                    exercise.name,
                                                    (clickedRowMap[exercise.name] ?: false)
                                                )
                                            }
                                    ) {
                                        Checkbox(
                                            checked = clickedRowMap[exercise.name] ?: false,
                                            onCheckedChange = {
                                                clickedRowMap[exercise.name] = it
                                                localUserData.saveClickedState(exercise.name, it)
                                            },
                                            colors = CheckboxDefaults.colors(checkedColor = SystemGreen)
                                        )
                                        Text(
                                            text = exercise.name.replaceFirstChar {
                                                if (it.isLowerCase()) it.titlecase(
                                                    Locale.getDefault()
                                                ) else it.toString()
                                            },
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = exercise.repetitions,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = if (exercise.weight.isNotEmpty()) "${exercise.weight} kg" else "",
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}