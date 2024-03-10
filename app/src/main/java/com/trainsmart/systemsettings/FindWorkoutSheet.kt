package com.trainsmart.systemsettings

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.trainsmart.repository.sharedpreferences.LocalUserData
import kotlinx.coroutines.tasks.await

suspend fun findWorkoutSheet(cloudDB: FirebaseFirestore, localUserData: LocalUserData): DocumentSnapshot =
    cloudDB.collection(Constants.Database.WORKOUTSHEETCOLLECTION)
        .document(localUserData.get(Constants.Database.FKID_USER))
        .get()
        .await()

