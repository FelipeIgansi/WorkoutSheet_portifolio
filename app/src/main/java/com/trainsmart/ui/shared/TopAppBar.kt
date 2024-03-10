package com.trainsmart.ui.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.trainsmart.R
import com.trainsmart.repository.sharedpreferences.SessionManager
import com.trainsmart.systemsettings.Constants
import com.trainsmart.systemsettings.Routes
import com.trainsmart.ui.theme.SystemRed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TopAppBar(
    private val sessionManager: SessionManager,
    private val navController: NavController,
    private val auth: FirebaseAuth,
) {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopAppBarDefault() {
        TopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(Color.Black),
            title = {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            },
            actions = {
                LogoutButton(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch { auth.signOut() }
                        sessionManager.delete(Constants.AUTHKEY)
                        sessionManager.saveAuthenticationStage(Routes.EmailLoginScreen.route)
                        navController.navigate(Routes.EmailLoginScreen.route)
                    }
                )
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopAppBarRegisterWorkoutSheet(navController: NavController) {
        CenterAlignedTopAppBar(
            navigationIcon = {
                val currentRoute = navController.currentDestination?.route
                BackButton(
                    onClick = {
                        currentRoute?.let { route ->
                            when (route) {
                                Routes.ObjectiveRegisterScreen.route -> navController.navigate(Routes.StudentScreen.route)
                                Routes.RegisterWorkoutSheetScreen.route -> navController.navigate(Routes.ObjectiveRegisterScreen.route)
                                Routes.ListWorkoutSheet.route -> navController.navigate(Routes.RegisterWorkoutSheetScreen.route)
                                Routes.EditWorkoutSheetScree.route -> navController.navigate(Routes.ListWorkoutSheet.route)
                            }
                        }
                    }
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(Color.Black),
            title = {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            },
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopAppBarRegisterPhases(navController: NavController) {
        val localContext = LocalContext.current
        val session = SessionManager(localContext)
        val currentSession = session.fetchAuthenticationStage()
        val icons = mapOf(
            Routes.EmailRegisterScreen.route to R.drawable.ic_profile,
            Routes.PhoneRegisterScreen.route to R.drawable.ic_smartphone,
            Routes.PasswordCreationScreen.route to R.drawable.ic_password
        )

        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(Color.Black),
            title = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    icons.forEach { (route, iconResId) ->
                        IconButton(onClick = {
                            session.saveAuthenticationStage(route)
                            navController.navigate(route)
                        }) {
                            Icon(
                                painterResource(id = iconResId),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = if (route == currentSession) SystemRed else Color.White
                            )
                        }
                    }
                }
            }
        )
    }

    @Composable
    private fun LogoutButton(onClick: () -> Unit) {
        IconButton(
            onClick = onClick
        ) {
            Icon(
                painterResource(id = R.drawable.logout),
                contentDescription = null,
                Modifier.size(30.dp),
                tint = Color.White
            )
        }
    }

    @Composable
    private fun BackButton(onClick: () -> Unit) {
        IconButton(
            onClick = onClick
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}
