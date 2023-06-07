package it.unical.gciaoo.vinteddu_android
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.HttpURLConnection
import java.net.URL

fun main() = runBlocking<Unit> {
    try {
        val url = URL("http://localhost:8080") // Sostituisci con l'URL corretto del tuo server
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val responseCode = connection.responseCode
        val responseBody = connection.inputStream.bufferedReader().use { it.readText() }

        println("Response Code: $responseCode")
        println("Response Body: $responseBody")
    } catch (e: Exception) {
        println("Errore nella connessione al server: ${e.message}")
    }
}







