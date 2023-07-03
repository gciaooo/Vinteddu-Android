package it.unical.gciaoo.vinteddu_android.ApiConfig

import android.content.Context
import android.content.SharedPreferences


class SessionManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("SessionManager", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    private val secretKey=  "23778sah9021-12123-12s-as-12a-AS_12xoiJN-SHWQ98";

    fun saveUsername(username : String) {
        editor.putString("Username", username)
        editor.apply()
    }

    fun saveId(id : Long) {
        editor.putLong("Id", id)
        editor.apply()
    }

    fun getUsername() : String? {
        val id = sharedPreferences.getString("Username", null)

        return id;
    }

    fun getId() : Long {
        val id = sharedPreferences.getLong("Id", 0)

        return id;
    }


    fun clearUsername() {
        editor.remove("Username")
        editor.apply()
    }

    fun saveToken(token: String) {
        editor.putString("Bearer", token)
        editor.apply()
    }

    fun getToken(): String? {
        val token = sharedPreferences.getString("Bearer", null)
        return token?.replace("Bearer ", "")
    }

    fun clearToken() {
        editor.remove("Bearer")
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.contains("Bearer")
    }
}
