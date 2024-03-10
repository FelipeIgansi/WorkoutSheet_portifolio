package com.trainsmart.repository.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

/**
 * Acesso a dados rápidos do projeto - SharedPreferences
 */
class RegisterLocalData(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("registerlocaldata", Context.MODE_PRIVATE)

    fun save(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }
    fun delete(key: String) {
        preferences.edit().remove(key).apply()
    }

    fun get(key: String): String {
        return preferences.getString(key, "") ?: ""
    }
}