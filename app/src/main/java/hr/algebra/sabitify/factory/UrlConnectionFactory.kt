package hr.algebra.sabitify.factory

import java.net.URL
import javax.net.ssl.HttpsURLConnection

fun createHttpConnection(path: String): HttpsURLConnection {
    val url = URL(path)
    return (url.openConnection() as HttpsURLConnection).apply {
        connectTimeout = TIMEOUT
        readTimeout = TIMEOUT
        requestMethod = METHOD_GET
        addRequestProperty(USER_AGENT, MOZILLA)
    }
}