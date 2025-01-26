package hr.algebra.sabitify.factory

import java.net.HttpURLConnection
import java.net.URL


fun createGetHttpUrlConnection(path: String): HttpURLConnection {
    val url = URL(path)
    return (url.openConnection() as HttpURLConnection).apply {
        connectTimeout = TIMEOUT
        readTimeout = TIMEOUT
        requestMethod = METHOD_GET
        addRequestProperty(USER_AGENT, MOZILLA)
    }
}