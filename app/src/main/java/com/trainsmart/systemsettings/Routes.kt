package com.trainsmart.systemsettings

sealed class Routes(val route: String) {
    data object EmailLoginScreen: Routes("emailloginscreen")
    data object EmailRegisterScreen: Routes("emailregisterscreen")
    data object PhoneRegisterScreen: Routes("phoneregisterscreen")
    data object PasswordCreationScreen: Routes("passwordcreationscreen")
    data object WorkoutSheetScreen: Routes("workoutsheetscreen")
    data object StudentScreen: Routes("studentscreen")
    data object RouteForPersonalOrStudent: Routes("routefropersonalorstudent")
    data object MessageScreen: Routes("messagescreen")
    data object ProfileScreen: Routes("profilescreen")
    data object RegisterWorkoutSheetScreen: Routes("register_workoutscreen")
    data object ObjectiveRegisterScreen : Routes("objectiveregisterscreen")
    data object ListWorkoutSheet : Routes("listworkoutsheet")
    data object RoleRegisterScreen: Routes("roleregisterscreen")// TODO apagar no futuro
    data object EditWorkoutSheetScree: Routes("editworkoutsheetscreen")// TODO apagar no futuro

}
