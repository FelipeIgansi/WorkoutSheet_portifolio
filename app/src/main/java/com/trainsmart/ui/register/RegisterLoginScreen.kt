package com.trainsmart.ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.trainsmart.R
import com.trainsmart.repository.sharedpreferences.LocalUserData
import com.trainsmart.repository.sharedpreferences.RegisterLocalData
import com.trainsmart.repository.sharedpreferences.SessionManager
import com.trainsmart.systemsettings.Constants
import com.trainsmart.systemsettings.Routes
import com.trainsmart.ui.theme.SystemRed
import com.trainsmart.ui.theme.outlinedCustomColors

@Composable
fun EmailRegisterScreen(
    padding: PaddingValues,
    navController: NavController,
    registerLocalData: RegisterLocalData
) {
    var email by remember { mutableStateOf(registerLocalData.get(Constants.EMAIL)) }
    var nome by remember { mutableStateOf(registerLocalData.get(Constants.NAME)) }
    var msgError by remember { mutableStateOf("") }
    val destination = Routes.PhoneRegisterScreen.route
    val newDestination = SessionManager(LocalContext.current)
    val sharedDataRegister = LocalUserData(LocalContext.current)


    Column(
        Modifier
            .padding(padding)
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painterResource(id = R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier.size(200.dp),
                tint = SystemRed
            )
        }
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()

        ) {
            Text(
                text = Constants.REGISTER,
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
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text(text = Constants.FULLNAME) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                colors = outlinedCustomColors()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = Constants.EMAIL) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                ),
                colors = outlinedCustomColors()
            )

            Text(
                text = msgError,
                color = Color.Red,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    if (nome.isNotEmpty() && email.isNotEmpty()) {
                        if (email.contains("@") && email.contains(".com")) {
                            if (nome.contains(" ")) {
                                newDestination.saveAuthenticationStage(Routes.PhoneRegisterScreen.route)
                                sharedDataRegister.save(Constants.NAME, nome)
                                sharedDataRegister.save(Constants.EMAIL, email)
                                navController.navigate(destination)
                            }else msgError = "Digite seu nome completo"
                        } else msgError = "E-mail informado é invalido"
                    } else msgError = when {
                        nome.isEmpty() -> "Nome não preenchido"
                        else -> "E-mail não preenchido"
                    }

                    registerLocalData.save(Constants.NAME, nome)
                    registerLocalData.save(Constants.EMAIL, email)
                },
                colors = ButtonDefaults.buttonColors(SystemRed),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = Constants.CONTINUE, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}