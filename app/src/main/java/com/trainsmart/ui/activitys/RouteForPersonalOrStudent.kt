package com.trainsmart.ui.activitys

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.trainsmart.repository.clouddatabase.model.UserModel
import com.trainsmart.repository.sharedpreferences.LocalUserData
import com.trainsmart.repository.sharedpreferences.SessionManager
import com.trainsmart.systemsettings.Constants
import com.trainsmart.systemsettings.Routes
import com.trainsmart.systemsettings.findEmailUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun RouteForPersonalOrStudent(
    cloudDB: FirebaseFirestore,
    navController: NavController,
    localUserData: LocalUserData

) {
    var user: UserModel? by remember { mutableStateOf(null) }
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(key1 = Unit) {
        val querySnapshot = withContext(Dispatchers.IO) {
            findEmailUser(cloudDB, localUserData.get(Constants.EMAIL))
        }
        user = if (!querySnapshot.isEmpty) querySnapshot.documents[0].toObject(UserModel::class.java) else UserModel()
        isLoading = false
    }
    if (isLoading){
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(50.dp))
        }
    }else {
        when (user?.papel) {
            Constants.STUDENT, Constants.PERSONAL -> localUserData.save(
                Constants.ROLE,
                user!!.papel
            )
        }


        val session = SessionManager(LocalContext.current)
        when (user?.papel?.lowercase()) {
            (Constants.STUDENT).lowercase() -> {
                session.saveAuthenticationStage(Routes.WorkoutSheetScreen.route)
                navController.navigate(Routes.WorkoutSheetScreen.route)
            }

            (Constants.PERSONAL).lowercase() -> {
                session.saveAuthenticationStage(Routes.StudentScreen.route)
                navController.navigate(Routes.StudentScreen.route)
            }
        }
    }
}
