package com.trainsmart.systemsettings

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.trainsmart.repository.sharedpreferences.LocalUserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun findUserIDFromFirestore(
    cloudDB: FirebaseFirestore,
    email: String,
    localUserData: LocalUserData
) {
    CoroutineScope(Dispatchers.IO).launch {
        val querySnapshot = findEmailUser(cloudDB, email)

        val documentId: String = querySnapshot.documents.firstOrNull()?.id ?: ""
        Log.i("information", "FindUserIDFromFirestore: $documentId")
        localUserData.save(Constants.Database.FKID_USER, documentId)
    }
}