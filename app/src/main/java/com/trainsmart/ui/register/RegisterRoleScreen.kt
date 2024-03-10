package com.trainsmart.ui.register

import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.trainsmart.R
import com.trainsmart.repository.clouddatabase.model.UserModel
import com.trainsmart.repository.sharedpreferences.RegisterLocalData
import com.trainsmart.systemsettings.Constants
import com.trainsmart.systemsettings.Routes
import com.trainsmart.ui.theme.SystemRed

@Composable
fun RoleRegisterScreen(
    cloudDB: FirebaseFirestore,
    auth: FirebaseAuth,
    padding: PaddingValues,
    navController: NavController,
    registerLocalData: RegisterLocalData
) {

    val email = registerLocalData.get(Constants.EMAIL)
    val nome = registerLocalData.get(Constants.NAME)
    val phone = registerLocalData.get(Constants.PHONE)
    val password = registerLocalData.get(Constants.PASSWORD)
    var personal by remember { mutableStateOf(false) }
    var aluno by remember { mutableStateOf(false) }
    var personalStatusButton by remember { mutableStateOf(true) }
    var alunoStatusButton by remember { mutableStateOf(true) }
    var msgError by remember { mutableStateOf("") }

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

            OutlinedButton(
                onClick = {
                    personal = true
                    alunoStatusButton = false
                },
                enabled = personalStatusButton,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = Constants.PERSONAL)
            }
            OutlinedButton(
                onClick = {
                    aluno = true
                    personalStatusButton = false
                },
                enabled = alunoStatusButton,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = Constants.STUDENT)
            }

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
                    if (personal || aluno) {
                        val papel =
                            if (personal) Constants.PERSONAL else Constants.STUDENT
                        val mapValues = UserModel(
                            nome = nome,
                            email = email,
                            telefone = phone,
                            papel = papel
                        )
                        cloudDB.collection(Constants.USERTABLENAME).add(mapValues)
                            .addOnSuccessListener {
                                Log.i(
                                    "information",
                                    "User register: Usuário $nome adicionado com sucesso"
                                )
                            }
                            .addOnFailureListener {
                                Log.w(
                                    "Erro",
                                    "User register: Ocorreu uma falha ao adicionar o usuário $nome !"
                                )
                            }
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnFailureListener { exception ->
                                msgError = when (exception) {
                                    is FirebaseAuthWeakPasswordException -> "Senha possui menos de 8 caracteres!"
                                    is FirebaseAuthUserCollisionException -> "Essa conta já está cadastrada!"
                                    is FirebaseNetworkException -> "Sem acesso a rede!"
                                    else -> "Erro ao cadastrar usuário!"
                                }
                            }
                            // TODO (Quando a pessoa chegar na tela final e já estiver cadastrada)
                            // Todo (Deletar os registros em base, e direciona-lo para o login)
                            // Todo (E idealmente notifica-lo dessa ação)
                            .addOnSuccessListener {
                                registerLocalData.save(Constants.ROLE, papel)
                                registerLocalData.delete(Constants.EMAIL)
                                registerLocalData.delete(Constants.PASSWORD)
                                registerLocalData.delete(Constants.NAME)
                                registerLocalData.delete(Constants.PHONE)
                                navController.navigate(Routes.RouteForPersonalOrStudent.route)
                            }
                    }
                },
                colors = ButtonDefaults.buttonColors(SystemRed),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = Constants.CREATEACCOUNT, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}