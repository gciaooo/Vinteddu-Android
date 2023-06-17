package it.unical.gciaoo.vinteddu_android.viewmodels

import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import it.unical.gciaoo.vinteddu_android.ApiService
import it.unical.gciaoo.vinteddu_android.RetrofitClient
import it.unical.gciaoo.vinteddu_android.SessionManager
import it.unical.gciaoo.vinteddu_android.model.Item

class ItemViewModel(private val localContext: Context) : ViewModel() {

    private val apiService: ApiService
    private val sessionManager: SessionManager

    init {
        sessionManager = SessionManager(localContext) // Create an instance of your SessionManager class
        apiService = RetrofitClient.create(sessionManager)
    }

    suspend fun getItem(id: Long): Item? {
        val token = sessionManager.getToken();
        val response = apiService.getItem("token", id)

        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}