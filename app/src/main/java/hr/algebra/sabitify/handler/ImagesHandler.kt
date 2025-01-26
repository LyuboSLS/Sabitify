package hr.algebra.sabitify.handler

import android.content.Context
import android.util.Log
import hr.algebra.sabitify.factory.TIMEOUT
import hr.algebra.sabitify.factory.createGetHttpUrlConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.util.UUID


// ImagesHandler.kt
suspend fun downloadImage(context: Context, url: String): String? =
    withContext(Dispatchers.IO) {  // Execute in background thread
        val filename = UUID.randomUUID().toString() + ".jpg"
        val file: File = createFile(context, filename)
        var connection: HttpURLConnection? = null

        try {
            connection = createGetHttpUrlConnection(url).apply {
                connectTimeout = TIMEOUT  // 15 seconds
                readTimeout = TIMEOUT    // 10 seconds
            }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream.use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }
                file.absolutePath
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("IMAGES_HANDLER", "Error downloading $url", e)
            null
        } finally {
            connection?.disconnect()
        }
    }

fun createFile(context: Context, filename: String): File {
    val dir = context.applicationContext.getExternalFilesDir(null)
    val file = File(dir, filename)
    if (file.exists()) file.delete()
    return file
}