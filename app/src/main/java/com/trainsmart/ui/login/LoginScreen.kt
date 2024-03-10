package com.trainsmart.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.trainsmart.R
import com.trainsmart.repository.sharedpreferences.SessionManager
import com.trainsmart.systemsettings.Constants
import com.trainsmart.systemsettings.Routes
import com.trainsmart.ui.theme.SystemRed
import com.trainsmart.ui.theme.outlinedCustomColors
import com.trainsmart.ui.viewmodel.LoginScreenViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    sessionManager: SessionManager,
    loginScreenViewModel: LoginScreenViewModel
) {

    val email by loginScreenViewModel.email.collectAsState("")
    val password by loginScreenViewModel.password.collectAsState("")
    val msgErrorText by loginScreenViewModel.msgError.collectAsState("")

    val destination = Routes.EmailRegisterScreen.route

    Column(
        Modifier
            .padding(10.dp)
            .fillMaxSize()
    ) {
        Logo()
        Text(
            text = Constants.LOGIN,
            style = TextStyle(
                fontSize = 32.sp,
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center,
            color = Color.White
        )

        InputField(
            value = email,
            onValueChange = { loginScreenViewModel.onEmailChange(it) },
            label = Constants.EMAIL
        )
        PasswordField(
            value = password,
            onValueChange = { loginScreenViewModel.onPasswordChange(it) }
        )

        Text(
            text = msgErrorText,
            color = Color.Red,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LoginButton(
            onClick = { loginScreenViewModel.fazerLogin(email, password) }
        )

        Text(
            text = "Ou",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        RegisterButton(
            onClick = {
                sessionManager.saveAuthenticationStage(destination)
                navController.navigate(destination)
            }
        )
    }
}


@Composable
private fun Logo() {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painterResource(id = R.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier.size(200.dp),
            tint = SystemRed
        )
    }

}

@Composable
private fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email
        ),
        colors = outlinedCustomColors()
    )
}

@Composable
private fun PasswordField(value: String, onValueChange: (String) -> Unit) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = Constants.PASSWORD) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password
        ),
        colors = outlinedCustomColors(),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(
                onClick = { isPasswordVisible = !isPasswordVisible },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    painterResource(
                        id =
                        if (isPasswordVisible) (R.drawable.visibility)
                        else (R.drawable.invisible)
                    ),
                    contentDescription = "Toggle password visibility",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    )
}

@Composable
private fun LoginButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(SystemRed),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = Constants.ENTER, color = Color.White)
    }
}

@Composable
private fun RegisterButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(SystemRed),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = Constants.REGISTRATION, color = Color.White)
    }
}
