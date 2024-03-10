package com.trainsmart.systemsettings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.trainsmart.repository.localdatabase.database.AppDatabase
import com.trainsmart.repository.sharedpreferences.LocalUserData
import com.trainsmart.repository.sharedpreferences.RegisterLocalData
import com.trainsmart.repository.sharedpreferences.SessionManager
import com.trainsmart.ui.activitys.RouteForPersonalOrStudent
import com.trainsmart.ui.activitys.administrator.ADMEditExercise
import com.trainsmart.ui.activitys.administrator.ADMListExercises
import com.trainsmart.ui.activitys.administrator.ADMListMember
import com.trainsmart.ui.activitys.administrator.ADMRegisterObjective
import com.trainsmart.ui.activitys.administrator.ADMRegisterWorkoutSheets
import com.trainsmart.ui.activitys.user.UserListWorkoutSheets
import com.trainsmart.ui.login.LoginScreen
import com.trainsmart.ui.register.EmailRegisterScreen
import com.trainsmart.ui.register.PasswordCreationScreen
import com.trainsmart.ui.register.PhoneRegisterScreen
import com.trainsmart.ui.register.RoleRegisterScreen
import com.trainsmart.ui.shared.TopAppBar
import com.trainsmart.ui.viewmodel.EditExerciseViewModel
import com.trainsmart.ui.viewmodel.ExerciseListViewModel
import com.trainsmart.ui.viewmodel.ListMemberViewModel
import com.trainsmart.ui.viewmodel.ListWorkoutSheetsViewModel
import com.trainsmart.ui.viewmodel.LoginScreenViewModel
import com.trainsmart.ui.viewmodel.RegisterObjectiveViewModel
import com.trainsmart.ui.viewmodel.RegisterWorkoutSheetsViewModel

class CallScaffold(
    private val navController: NavHostController,
    private val auth: FirebaseAuth,
    private val cloudDB: FirebaseFirestore,
    private val localDB: AppDatabase,
    private val localUserData: LocalUserData,
    private val sessionManager: SessionManager,
    private val registerObjectiveViewModel: RegisterObjectiveViewModel,
    private val registerWorkoutSheetsViewModel: RegisterWorkoutSheetsViewModel,
    private val listWorkoutSheetsViewModel: ListWorkoutSheetsViewModel,
    private val userRole: String,
    private val registerLocalData: RegisterLocalData,
    private val exerciseListViewModel: ExerciseListViewModel,
    private val loginScreenViewModel: LoginScreenViewModel,
    private val editExerciseViewModel: EditExerciseViewModel,
    private val listMemberViewModel: ListMemberViewModel

) {
    @Composable
    fun buildScreen(screen: String): PaddingValues {
        val topAppBar = TopAppBar(sessionManager, navController, auth)
        Scaffold(
            topBar = {
                when (screen) {
                    Routes.WorkoutSheetScreen.route -> topAppBar.TopAppBarDefault()
                    Routes.EmailRegisterScreen.route -> topAppBar.TopAppBarRegisterPhases(navController = navController)
                    Routes.PhoneRegisterScreen.route -> topAppBar.TopAppBarRegisterPhases(navController = navController)
                    Routes.PasswordCreationScreen.route -> topAppBar.TopAppBarRegisterPhases(navController = navController)

                    Routes.ProfileScreen.route -> topAppBar.TopAppBarDefault()
                    Routes.StudentScreen.route -> topAppBar.TopAppBarDefault()
                    Routes.MessageScreen.route -> topAppBar.TopAppBarDefault()

                    Routes.RegisterWorkoutSheetScreen.route -> topAppBar.TopAppBarRegisterWorkoutSheet(navController = navController)
                    Routes.ObjectiveRegisterScreen.route -> topAppBar.TopAppBarRegisterWorkoutSheet(navController = navController)
                    Routes.ListWorkoutSheet.route -> topAppBar.TopAppBarRegisterWorkoutSheet(navController = navController)
                    Routes.EditWorkoutSheetScree.route -> topAppBar.TopAppBarRegisterWorkoutSheet(navController = navController)

                    //TODO Apagar no futuro
                    Routes.RoleRegisterScreen.route -> topAppBar.TopAppBarRegisterPhases(
                        navController = navController
                    )
                }
            },
            /*bottomBar = {
                when (screen) {
                    Routes.WorkoutSheetScreen.route -> BottomAppBarStudent(navController)
                    Routes.ProfileScreen.route -> BottomAppBarAdm(navController)
                    Routes.StudentScreen.route -> BottomAppBarAdm(navController)
//                    Routes.MessageScreen.route -> BottomAppBarStudent(navController)
                }
            },*/
            content = { paddingValues ->
                when (screen) {
                    Routes.EmailLoginScreen.route -> LoginScreen(navController, sessionManager, loginScreenViewModel)
                    Routes.PasswordCreationScreen.route -> PasswordCreationScreen(paddingValues, navController, registerLocalData)
                    Routes.WorkoutSheetScreen.route -> UserListWorkoutSheets(paddingValues, listWorkoutSheetsViewModel, localUserData, cloudDB)
                    Routes.EmailRegisterScreen.route -> EmailRegisterScreen(paddingValues, navController, registerLocalData)
                    Routes.PhoneRegisterScreen.route -> PhoneRegisterScreen(paddingValues, navController, registerLocalData)
                    Routes.StudentScreen.route -> ADMListMember(paddingValues, listMemberViewModel)
                    Routes.RegisterWorkoutSheetScreen.route -> ADMRegisterWorkoutSheets(paddingValues, registerWorkoutSheetsViewModel)


                    // TODO apagar no futuro
                    Routes.RoleRegisterScreen.route -> RoleRegisterScreen(cloudDB, auth, paddingValues, navController, registerLocalData)
                    Routes.RouteForPersonalOrStudent.route -> RouteForPersonalOrStudent(cloudDB, navController, localUserData)
                    Routes.ObjectiveRegisterScreen.route -> ADMRegisterObjective(paddingValues, registerObjectiveViewModel, cloudDB, localUserData)
                    Routes.ListWorkoutSheet.route -> ADMListExercises(paddingValues, exerciseListViewModel, navController)
                    Routes.EditWorkoutSheetScree.route -> ADMEditExercise(paddingValues, navController, localUserData, editExerciseViewModel)
                }
                return@Scaffold
            }
        )
        return PaddingValues()
    }
}
