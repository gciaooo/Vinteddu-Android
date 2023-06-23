package it.unical.gciaoo.vinteddu_android.ApiConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val response = chain.proceed(originalRequest)
        val token = response.header("Authorization")

//        if (token != null) {
//            val username = extractUsernameFromToken(token)
//            if (username != null) {
//                sessionManager.saveUsername(username)
//            }
//        }

        if (token != null) {
            sessionManager.saveToken(token)
        }

        return response
    }


//    private fun extractUsernameFromToken(token: String): String? {
//        try {
//            val claims: Claims = Jwts.parser().setSigningKey("your-secret-key").parseClaimsJws(token).body
//            return claims.subject
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return null
//    }
}
