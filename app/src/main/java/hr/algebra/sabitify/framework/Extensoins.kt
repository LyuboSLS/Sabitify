package hr.algebra.sabitify.framework

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.preference.PreferenceManager
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.getSystemService

fun View.applyAnimation(id: Int) {
    val anim = AnimationUtils.loadAnimation(
        context,
        id
    )
    startAnimation(anim)
}

inline fun <reified T : Activity> Context.startActivity() {
    startActivity(
        Intent(this, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    )
}

@Suppress("DEPRECATION")
fun Context.getBooleanPreferences(key: String) =
    PreferenceManager.getDefaultSharedPreferences(this)
        .getBoolean(key, false)

@Suppress("DEPRECATION")
fun Context.setBooleanPreferences(key: String, value: Boolean = true) =
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit()
        .putBoolean(key, value)
        .apply()

fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService<ConnectivityManager>()
    connectivityManager?.activeNetwork?.let { network ->
        connectivityManager.getNetworkCapabilities(network)?.let { capabilities ->
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }
    }
    return false
}