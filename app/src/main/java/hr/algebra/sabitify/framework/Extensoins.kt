package hr.algebra.nasa.framework

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager

fun View.applyAnimation(id: Int) {
    startAnimation(AnimationUtils.loadAnimation(context, id))
}

inline fun <reified T : Activity> Context.startActivity() {
    startActivity(
        Intent(this, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    )
}

inline fun <reified T : BroadcastReceiver> Context.sendBroadcast() {
    sendBroadcast(Intent(this, T::class.java))
}

fun Context.setBooleanPreference(key: String, value: Boolean = true) {
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit()
        .putBoolean(key, value)
        .apply()
}

fun Context.getBooleanPreference(key: String) =
    PreferenceManager.getDefaultSharedPreferences(this)
        .getBoolean(key, false)

fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService<ConnectivityManager>()
    connectivityManager?.activeNetwork?.let { network ->
        connectivityManager.getNetworkCapabilities(network)?.let { cap ->
            return cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }
    }

    return false
}

fun callDelayed(delay: Long, work: Runnable) {
    Handler(Looper.getMainLooper()).postDelayed(
        work,
        delay
    )
}

/*@SuppressLint("Range")
fun Context.fetchItems(): MutableList<Item>{
    val items = mutableListOf<Item>()
    val cursor = contentResolver?.query(
        SABITIFY_PROVIDER_CONTENT_URI,
        null,
        null,
        null,
        null
    )



    while(cursor != null && cursor.moveToNext()) {
       val date =
            EventDate(
                /*_id = cursor.getLong(cursor.getColumnIndex(EventDate::_id.name)),
                start_date = cursor.getString(cursor.getColumnIndex(EventDate::start_date.name))*/
                null,""
                )

        val location =
            EventLocation(
               /* _id = cursor.getLong(cursor.getColumnIndex(EventLocation::_id.name)),
                link = cursor.getString(cursor.getColumnIndex(EventLocation::link.name))*/
                null,""
            )

        val addresses = mutableListOf<String>()

        val ticketInfo = mutableListOf<TicketInfo>()

        val venue = Venue(
            /*_id = cursor.getLong(cursor.getColumnIndex(Venue::_id.name)),
            name = cursor.getString(cursor.getColumnIndex(Venue::name.name)),
            rating = cursor.getDouble(cursor.getColumnIndex(Venue::rating.name)),
            reviews = cursor.getInt(cursor.getColumnIndex(Venue::reviews.name)),
            link = cursor.getString(cursor.getColumnIndex(Venue::link.name))*/
            null,"",5.0,5,""
        )
        items.add(
            Item(
                cursor.getLong(cursor.getColumnIndex(Item::_id.name)),
                cursor.getString(cursor.getColumnIndex(Item::title.name)),
                date,
                addresses,
                cursor.getString(cursor.getColumnIndex(Item::link.name)),
                location,
                cursor.getString(cursor.getColumnIndex(Item::description.name)),
                ticketInfo,
                venue,
                cursor.getString(cursor.getColumnIndex(Item::thumbnail.name)),
                cursor.getString(cursor.getColumnIndex(Item::image.name)),
                cursor.getInt(cursor.getColumnIndex(Item::read.name)) == 1
        )
        )
    }

    return items
}*/
