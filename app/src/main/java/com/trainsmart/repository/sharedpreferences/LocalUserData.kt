package com.trainsmart.repository.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

/**
 * Acesso a dados rápidos do projeto - SharedPreferences
 */
class LocalUserData(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("localuserdata", Context.MODE_PRIVATE)

    fun save(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }
    fun delete(key: String) {
        preferences.edit().remove(key).apply()
    }

    fun get(key: String): String {
        return preferences.getString(key, "") ?: ""
    }

    fun saveClickedState(key: String, clicked: Boolean) {
        preferences.edit().putBoolean(key, clicked).apply()
    }

    fun getClickedState(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

}