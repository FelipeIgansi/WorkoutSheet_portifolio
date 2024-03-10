package com.trainsmart.ui.activitys.administrator

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.trainsmart.repository.localdatabase.entity.Exercise
import com.trainsmart.systemsettings.Routes
import com.trainsmart.ui.viewmodel.ExerciseListViewModel
import java.util.Locale

@Composable
fun ADMListExercises(
    padding: PaddingValues,
    viewModel: ExerciseListViewModel,
    navController: NavController
) {
    val listExercises by viewModel.exercises.collectAsState(emptyList())

    LaunchedEffect(key1 = Unit) {
        viewModel.loadExercises()
    }

    LazyColumn(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
    ) {
        items(listExercises) { exercise ->
            ExerciseItem(exercise = exercise, onEdit = {
                viewModel.onEditExercise(exercise.idExercise.toString())
                navController.navigate(Routes.EditWorkoutSheetScree.route)
            }, onDelete = {
                viewModel.deleteExercise(exercise)
            })
        }
    }
}

@Composable
fun ExerciseItem(
    exercise: Exercise,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(
                width = 2.dp,
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
    ) {
        Text(text = exercise.type.uppercase(), fontSize = 18.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = exercise.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
                Text(text = "Peso: ${if (exercise.weight.isNotBlank()) "${exercise.weight} kg" else ""}")
                Text(text = "Repetições: ${exercise.repetitions}")
            }
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(5.dp))
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                }
            }
        }

    }
}