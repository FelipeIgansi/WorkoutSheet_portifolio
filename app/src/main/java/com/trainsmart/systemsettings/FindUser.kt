package com.trainsmart.systemsettings

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


suspend fun findEmailUser(cloudDB: FirebaseFirestore, email: String) =
    cloudDB.collection(Constants.USERTABLENAME)
        .whereEqualTo("email", email.lowercase())
        .get()
        .await()

