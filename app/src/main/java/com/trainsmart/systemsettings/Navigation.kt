package com.trainsmart.systemsettings

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.trainsmart.repository.localdatabase.database.AppDatabase
import com.trainsmart.repository.sharedpreferences.LocalUserData
import com.trainsmart.repository.sharedpreferences.RegisterLocalData
import com.trainsmart.repository.sharedpreferences.SessionManager
import com.trainsmart.ui.viewmodel.EditExerciseViewModel
import com.trainsmart.ui.viewmodel.ExerciseListViewModel
import com.trainsmart.ui.viewmodel.ListMemberViewModel
import com.trainsmart.ui.viewmodel.ListWorkoutSheetsViewModel
import com.trainsmart.ui.viewmodel.LoginScreenViewModel
import com.trainsmart.ui.viewmodel.RegisterObjectiveViewModel
import com.trainsmart.ui.viewmodel.RegisterWorkoutSheetsViewModel

class Navigation {
    private lateinit var navController: NavHostController
    private lateinit var auth: FirebaseAuth
    private lateinit var cloudDB: FirebaseFirestore
    private lateinit var localDB: AppDatabase
    private lateinit var localUserData: LocalUserData
    private lateinit var sessionManager: SessionManager
    private lateinit var registerObjectiveViewModel: RegisterObjectiveViewModel
    private lateinit var registerWorkoutSheetsViewModel: RegisterWorkoutSheetsViewModel
    private lateinit var listWorkoutSheetsViewModel: ListWorkoutSheetsViewModel
    private lateinit var userRole: String
    private lateinit var registerLocalData: RegisterLocalData
    private lateinit var exerciseListViewModel: ExerciseListViewModel
    private lateinit var loginScreenViewModel: LoginScreenViewModel
    private lateinit var editExerciseViewModel: EditExerciseViewModel
    private lateinit var listMemberViewModel: ListMemberViewModel

    private fun NavGraphBuilder.composableScreen(route: String) {
        composable(route) {
            CallScaffold(
                navController = navController,
                auth = auth,
                cloudDB = cloudDB,
                localDB = localDB,
                localUserData = localUserData,
                sessionManager = sessionManager,
                registerObjectiveViewModel = registerObjectiveViewModel,
                registerWorkoutSheetsViewModel = registerWorkoutSheetsViewModel,
                userRole = userRole,
                listWorkoutSheetsViewModel = listWorkoutSheetsViewModel,
                registerLocalData = registerLocalData,
                exerciseListViewModel = exerciseListViewModel,
                loginScreenViewModel = loginScreenViewModel,
                editExerciseViewModel = editExerciseViewModel,
                listMemberViewModel = listMemberViewModel
            ).buildScreen(route)
        }
    }

    @Composable
    fun Create() {
        navController = rememberNavController()
        auth = FirebaseAuth.getInstance()
        cloudDB = FirebaseFirestore.getInstance()
        localDB = AppDatabase.getDatabase(LocalContext.current)
        localUserData = LocalUserData(LocalContext.current)
        sessionManager = SessionManager(LocalContext.current)
        registerObjectiveViewModel = RegisterObjectiveViewModel(
            navController = navController,
            localUserData = localUserData,
            localDB = localDB,
            cloudDB = cloudDB
        )
        registerWorkoutSheetsViewModel = RegisterWorkoutSheetsViewModel(
            localDB = localDB,
            navController = navController,
            sessionManager = sessionManager,
            cloudDB = cloudDB,
            localUserData = localUserData
        )
        listWorkoutSheetsViewModel = ListWorkoutSheetsViewModel(cloudDB, localUserData, localDB)
        registerLocalData = RegisterLocalData(LocalContext.current)
        exerciseListViewModel = ExerciseListViewModel(localDB, localUserData)
        loginScreenViewModel = LoginScreenViewModel(auth, localUserData, navController)
        editExerciseViewModel = EditExerciseViewModel(localDB, localUserData, cloudDB)
        listMemberViewModel = ListMemberViewModel(cloudDB, navController, localUserData)
        userRole = localUserData.get(Constants.ROLE)



        val startDestination = SessionManager(LocalContext.current).fetchAuthenticationStage()

        if (!startDestination.isNullOrEmpty()) {
            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {
                composableScreen(Routes.EmailLoginScreen.route)
                composableScreen(Routes.PasswordCreationScreen.route)
                composableScreen(Routes.WorkoutSheetScreen.route)
                composableScreen(Routes.EmailRegisterScreen.route)
                composableScreen(Routes.PhoneRegisterScreen.route)
                composableScreen(Routes.ProfileScreen.route)
                composableScreen(Routes.StudentScreen.route)
                composableScreen(Routes.MessageScreen.route)
                composableScreen(Routes.RegisterWorkoutSheetScreen.route)
                composableScreen(Routes.RouteForPersonalOrStudent.route)
                composableScreen(Routes.ObjectiveRegisterScreen.route)
                composableScreen(Routes.ListWorkoutSheet.route)
                composableScreen(Routes.EditWorkoutSheetScree.route)

                // TODO Apagar no futuro
                composableScreen(Routes.RoleRegisterScreen.route)
            }
        }
    }
}
