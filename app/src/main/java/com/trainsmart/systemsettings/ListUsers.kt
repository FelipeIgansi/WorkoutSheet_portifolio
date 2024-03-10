package com.trainsmart.systemsettings

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.trainsmart.repository.clouddatabase.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ListUsers {

    //TODO(Função que lista todos os Alunos)
    @Composable
    fun returnListOfStudents(
        scope: CoroutineScope,
        clouDb: FirebaseFirestore,
    ): List<UserModel> {
        val listUsers = mutableListOf<UserModel>()
        var alunos: QuerySnapshot

        LaunchedEffect(key1 = Unit) {
            scope.launch {
                alunos = clouDb.collection(Constants.USERTABLENAME)
                    .whereEqualTo(Constants.ROLE, Constants.STUDENT)
                    .get()
                    .await() // await() é uma função de extensão do Kotlin Coroutines
                for (document in alunos) {
                    listUsers += listOf(document.toObject(UserModel::class.java))
                    Log.d("Debug:  ", "${document.id} => ${document.data}")
                }
            }
        }
        return listUsers
    }


    @Composable
    fun returnListOfPersonals(
        scope: CoroutineScope,
        clouDb: FirebaseFirestore,
    ): List<UserModel> {
        var listUsers = listOf<UserModel>()
        var alunos: QuerySnapshot

        LaunchedEffect(key1 = Unit) {
            scope.launch {
                alunos = clouDb.collection(Constants.USERTABLENAME)
                    .whereEqualTo(Constants.ROLE, Constants.PERSONAL)
                    .get()
                    .await() // await() é uma função de extensão do Kotlin Coroutines
                for (document in alunos) {
                    listUsers += listOf(document.toObject(UserModel::class.java))
                    Log.d("Debug:  ", "${document.id} => ${document.data}")
                }
            }
        }
        return listUsers
    }
}