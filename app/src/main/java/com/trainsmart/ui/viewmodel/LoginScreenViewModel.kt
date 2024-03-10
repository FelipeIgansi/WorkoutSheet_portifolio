package com.trainsmart.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.trainsmart.repository.sharedpreferences.LocalUserData
import com.trainsmart.systemsettings.Constants
import com.trainsmart.systemsettings.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.regex.Pattern

class LoginScreenViewModel(
    private val auth: FirebaseAuth,
    private val localUserData: LocalUserData,
    private val navController: NavController
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _msgError = MutableStateFlow("")
    val msgError: StateFlow<String> = _msgError

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }
    fun fazerLogin(email: String, password: String) {
        when {
            email.isBlank() || password.isBlank() -> _msgError.value =
                "E-mail ou senha não informados"

            !isValidEmail(email) -> _msgError.value = "E-mail informado é inválido"
            else -> authenticateUser(email, password)
        }
    }

    private fun authenticateUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnFailureListener { exception ->
                handleLoginFailure(exception)
            }
            .addOnSuccessListener {
                localUserData.save(Constants.EMAIL, email.lowercase())
                navController.navigate(Routes.RouteForPersonalOrStudent.route)
            }
    }

    private fun handleLoginFailure(exception: Exception) {
        val errorMessage = when (exception) {
            is FirebaseAuthInvalidCredentialsException,
            is FirebaseException -> "E-mail ou senha não está cadastrado!"
            is FirebaseAuthInvalidUserException -> "E-mail não cadastrado!"
            else -> "Erro ao logar usuário!"
        }
        _msgError.value = errorMessage
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Pattern.compile(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return emailRegex.matcher(email).matches()
    }
}