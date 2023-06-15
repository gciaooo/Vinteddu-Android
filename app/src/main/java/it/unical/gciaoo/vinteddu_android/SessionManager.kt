package it.unical.gciaoo.vinteddu_android

import android.content.Context
import android.content.SharedPreferences
import io.jsonwebtoken.Jwts

class SessionManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("SessionManager", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    private val secretKey=  "23778sah9021-12123-12s-as-12a-AS_12xoiJN-SHWQ98";

    fun saveUsername(username: String) {
        editor.putString("Username", username)
        editor.apply()
    }

    fun getUsername(token : String?): String? {
        val jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build()
        val claims = jwtParser.parseClaimsJws(token).body
        return claims["username"] as? String
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
