package com.trainsmart.repository.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.trainsmart.systemsettings.Constants
import com.trainsmart.systemsettings.Routes

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences("AppPref", 0)

    fun saveAuthenticationStage(stage: String) {
        val editor = prefs.edit()
        editor.putString(Constants.AUTHKEY, stage)
        editor.apply()
    }
    fun delete(key: String) {
        prefs.edit().remove(key).apply()
    }

    fun fetchAuthenticationStage(): String? {
        return prefs.getString(Constants.AUTHKEY, Routes.EmailLoginScreen.route)
    }
}