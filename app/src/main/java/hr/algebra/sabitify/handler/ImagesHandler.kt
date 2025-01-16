package hr.algebra.sabitify.handler

import android.content.Context
import android.util.Log
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID
import javax.net.ssl.HttpsURLConnection

fun downloadImage(
    context: Context,
    url: String,
    filename: String = UUID.randomUUID().toString()
): String? {
    val file = createFile(context, filename)
    try {
        val con: HttpsURLConnection = createHttpConnection(url)
        Files.copy(con.inputStream, Paths.get(file.toURI()))
        return file.absolutePath
    } catch (e: Exception) {
        Log.e("DOWNLOAD_IMAGE", e.message, e)
    }
    return null
}

fun createFile(context: Context, filename: String): File {
    val dir = context.getExternalFilesDir(null)
    val file = File(dir, filename)
    if (file.exists()) file.delete()
    return file
}
