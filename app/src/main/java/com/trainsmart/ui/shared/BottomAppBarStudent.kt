package com.trainsmart.ui.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.trainsmart.R
import com.trainsmart.systemsettings.Routes

@Composable
fun BottomAppBarAdm(navController: NavController) {
    BottomAppBar(
        containerColor = Color.Black,
        modifier = Modifier.height(100.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ButtomListStudents(navController)
//            ButtomMessage(navController)
            ButtomPerson(navController)
        }
    }
}

@Composable
fun BottomAppBarStudent(navController: NavController) {
    BottomAppBar(
        containerColor = Color.Black,
        modifier = Modifier.height(100.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ButtomTable(navController)
//            ButtomMessage(navController)
            ButtomPerson(navController)
        }
    }
}

@Composable
private fun ButtomPerson(navController: NavController) {
    IconButton(onClick = { navController.navigate(Routes.ProfileScreen.route) }) {
        Icon(
            Icons.Filled.Person,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
private fun ButtomMessage(navController: NavController) {
    IconButton(onClick = { navController.navigate(Routes.MessageScreen.route) },) {
        Icon(
            painterResource(id = R.drawable.ic_message),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
private fun ButtomTable(navController: NavController) {
    IconButton(onClick = { navController.navigate(Routes.WorkoutSheetScreen.route) },) {
        Icon(
            painterResource(id = R.drawable.ic_table),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
private fun ButtomListStudents(navController: NavController) {
    IconButton(onClick = { navController.navigate(Routes.StudentScreen.route) },) {
        Icon(
            painterResource(id = R.drawable.people),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
    }
}