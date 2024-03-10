package com.trainsmart.ui.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.trainsmart.R
import com.trainsmart.repository.sharedpreferences.LocalUserData
import com.trainsmart.repository.sharedpreferences.RegisterLocalData
import com.trainsmart.repository.sharedpreferences.SessionManager
import com.trainsmart.systemsettings.Constants
import com.trainsmart.systemsettings.Routes
import com.trainsmart.ui.theme.SystemGreen
import com.trainsmart.ui.theme.SystemRed
import com.trainsmart.ui.theme.outlinedCustomColors
import kotlinx.coroutines.delay

@Composable
fun PasswordCreationScreen(
    padding: PaddingValues,
    navController: NavController,
    registerLocalData: RegisterLocalData
) {

    val email = LocalUserData(LocalContext.current).get(Constants.EMAIL)
    var pass by remember { mutableStateOf(registerLocalData.get(Constants.PASSWORD)) }
    var rePass by remember { mutableStateOf(registerLocalData.get(Constants.PASSWORD)) }
    var msgError by remember { mutableStateOf("") }
    val destination = Routes.RoleRegisterScreen.route
    val newDestination = SessionManager(LocalContext.current)
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isRePasswordVisible by remember { mutableStateOf(false) }
    val hasLowercase = pass.hasLowercase()
    val hasNumber = pass.hasNumber()
    val hasMinimumSize = pass.hasMinimumSize()

    LaunchedEffect(key1 = msgError) {
        delay(3000L) // delay for 3 seconds
        msgError = ""
    }

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = pass,
                    onValueChange = {
                        pass = it
                    },
                    label = { Text(text = Constants.PASSWORD) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password
                    ),
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    colors = outlinedCustomColors(),
                    modifier = Modifier.weight(1f),
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = rePass,
                    onValueChange = { rePass = it },
                    label = { Text(text = Constants.RE_PASS) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password
                    ),
                    visualTransformation = if (isRePasswordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    colors = outlinedCustomColors(),
                    modifier = Modifier.weight(1f),
                    trailingIcon = {
                        IconButton(
                            onClick = { isRePasswordVisible = !isRePasswordVisible },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Icon(
                                painterResource(
                                    id =
                                    if (isRePasswordVisible) (R.drawable.visibility)
                                    else (R.drawable.invisible)
                                ),
                                contentDescription = "Toggle password visibility",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                )
            }
            if (msgError.isBlank()) {
                Column {
                    Text(text = "Sua senha deve ter:")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        CustomBox(text = "Minuscula", hasSomething = hasLowercase, width = 100.dp)
                        Spacer(modifier = Modifier.width(5.dp))
                        CustomBox(text = "Numero", hasSomething = hasNumber, width = 90.dp)
                        Spacer(modifier = Modifier.width(5.dp))
                        CustomBox(
                            text = "+ 8 caracteres",
                            hasSomething = hasMinimumSize,
                            width = 130.dp
                        )
                    }
                }
            } else {
                Text(
                    text = msgError,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))


            Button(// Todo (terminada a parte visual, corrigir a validação)
                onClick = {
                    if (email.isNotEmpty() && pass.isNotEmpty()) {
                        if (pass == rePass) {
                            if (hasLowercase && hasNumber && hasMinimumSize) {
                                newDestination.saveAuthenticationStage(destination)
                                registerLocalData.save(Constants.PASSWORD, pass)
                                navController.navigate(destination)
                            }
                        } else msgError = "Senhas não são iguais."
                    } else msgError = "Senha não informada"
                },
                colors = ButtonDefaults.buttonColors(SystemRed),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = Constants.CREATEACCOUNT, color = Color.White)
            }
        }

    }
}


@Composable
fun CustomBox(text: String, hasSomething: Boolean, width: Dp) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(width)
            .padding(top = 5.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(if (!hasSomething) SystemRed else SystemGreen)
    ) { Text(text = text, color = if (!hasSomething) Color.White else Color.Black) }
}

fun String.hasLowercase(): Boolean {
    val lowercaseRegex = Regex("[a-z]")
    return lowercaseRegex.containsMatchIn(this)
}

fun String.hasNumber(): Boolean {
    val numberRegex = Regex("\\d")
    return numberRegex.containsMatchIn(this)
}

fun String.hasMinimumSize(): Boolean {
    return length >= 8
}