package it.unical.gciaoo.vinteddu_android

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("SessionManager", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveToken(token: String) {
        editor.putString("Bearer", token)
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("Bearer", null)
    }

    fun clearToken() {
        editor.remove("Bearer")
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.contains("Bearer")
    }
}
